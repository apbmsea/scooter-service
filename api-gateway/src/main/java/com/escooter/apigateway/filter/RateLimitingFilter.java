package com.escooter.apigateway.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RateLimitingFilter extends AbstractGatewayFilterFactory<RateLimitingFilter.Config> {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RateLimitingFilter() {
        super(Config.class);
        scheduler.scheduleAtFixedRate(this::cleanupCache, 10, 10, TimeUnit.MINUTES);
    }

    private void cleanupCache() {
        if (cache.size() > 10000) {
            log.info("Cleaning up rate limit cache, current size: {}", cache.size());
            cache.clear();
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            
            String key = getRateLimitKey(request, path);
            
            Bucket bucket = resolveBucket(key, config);
            
            if (bucket.tryConsume(1)) {
                log.debug("Rate limit passed for key: {}, path: {}", key, path);
                return chain.filter(exchange);
            } else {
                log.warn("Rate limit exceeded for key: {}, path: {}", key, path);
                return onRateLimitExceeded(exchange, config);
            }
        };
    }

    private String getRateLimitKey(ServerHttpRequest request, String path) {
        // Если есть User ID в заголовках (пользователь аутентифицирован)
        String userId = request.getHeaders().getFirst("X-User-Id");
        if (userId != null) {
            return "user:" + userId;
        }
        
        String ipAddress = getClientIpAddress(request);
        return "ip:" + ipAddress;
    }

    private String getClientIpAddress(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        if (request.getRemoteAddress() != null) {
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
        
        return "unknown";
    }

    private Bucket resolveBucket(String key, Config config) {
        return cache.computeIfAbsent(key, k -> {
            Bandwidth limit = Bandwidth.classic(
                    config.getCapacity(),
                    Refill.intervally(config.getRefillTokens(), Duration.ofSeconds(config.getRefillPeriod()))
            );
            return Bucket.builder()
                    .addLimit(limit)
                    .build();
        });
    }

    private Mono<Void> onRateLimitExceeded(ServerWebExchange exchange, Config config) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().add("Content-Type", "application/json");
        response.getHeaders().add("X-RateLimit-Limit", String.valueOf(config.getCapacity()));
        response.getHeaders().add("Retry-After", String.valueOf(config.getRefillPeriod()));
        
        String body = String.format(
                "{\"error\":\"Too Many Requests\",\"message\":\"Rate limit exceeded. Maximum %d requests per %d seconds.\",\"retryAfter\":%d}",
                config.getCapacity(),
                config.getRefillPeriod(),
                config.getRefillPeriod()
        );
        
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    /**
     * Конфигурация фильтра
     */
    public static class Config {
        private int capacity = 100; // Максимальное количество запросов
        private int refillTokens = 100; // Количество токенов для пополнения
        private int refillPeriod = 60; // Период пополнения в секундах

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getRefillTokens() {
            return refillTokens;
        }

        public void setRefillTokens(int refillTokens) {
            this.refillTokens = refillTokens;
        }

        public int getRefillPeriod() {
            return refillPeriod;
        }

        public void setRefillPeriod(int refillPeriod) {
            this.refillPeriod = refillPeriod;
        }
    }
}


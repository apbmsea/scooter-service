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

    // Кэш для хранения bucket'ов по ключу (IP адрес или user ID)
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    
    // Планировщик для очистки неиспользуемых bucket'ов
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RateLimitingFilter() {
        super(Config.class);
        // Очищаем кэш каждые 10 минут от неиспользуемых bucket'ов
        scheduler.scheduleAtFixedRate(this::cleanupCache, 10, 10, TimeUnit.MINUTES);
    }

    /**
     * Очистка кэша от неиспользуемых bucket'ов
     * В реальном приложении можно использовать более сложную логику с TTL
     */
    private void cleanupCache() {
        // Простая очистка - в production можно добавить логику с временем последнего использования
        if (cache.size() > 10000) {
            log.info("Cleaning up rate limit cache, current size: {}", cache.size());
            // Очищаем половину кэша (можно улучшить логику)
            cache.clear();
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            
            // Определяем ключ для rate limiting (IP адрес или user ID)
            String key = getRateLimitKey(request, path);
            
            // Получаем или создаем bucket для этого ключа
            Bucket bucket = resolveBucket(key, config);
            
            // Пытаемся получить токен из bucket
            if (bucket.tryConsume(1)) {
                // Токен получен - пропускаем запрос
                log.debug("Rate limit passed for key: {}, path: {}", key, path);
                return chain.filter(exchange);
            } else {
                // Лимит превышен - возвращаем 429
                log.warn("Rate limit exceeded for key: {}, path: {}", key, path);
                return onRateLimitExceeded(exchange, config);
            }
        };
    }

    /**
     * Определяет ключ для rate limiting:
     * - Для аутентифицированных пользователей - используем User ID
     * - Для публичных endpoints - используем IP адрес
     */
    private String getRateLimitKey(ServerHttpRequest request, String path) {
        // Если есть User ID в заголовках (пользователь аутентифицирован)
        String userId = request.getHeaders().getFirst("X-User-Id");
        if (userId != null) {
            return "user:" + userId;
        }
        
        // Иначе используем IP адрес
        String ipAddress = getClientIpAddress(request);
        return "ip:" + ipAddress;
    }

    /**
     * Получает IP адрес клиента с учетом прокси-заголовков
     */
    private String getClientIpAddress(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Берем первый IP из списка
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        // Fallback на remote address
        if (request.getRemoteAddress() != null) {
            return request.getRemoteAddress().getAddress().getHostAddress();
        }
        
        return "unknown";
    }

    /**
     * Получает или создает bucket для ключа
     */
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

    /**
     * Обработка превышения лимита
     */
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


package com.escooter.apigateway.config;

import com.escooter.apigateway.filter.RateLimitingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @Value("${gateway.services.user-service.url}")
    private String userServiceUrl;

    @Value("${gateway.services.scooter-service.url}")
    private String scooterServiceUrl;

    @Value("${gateway.services.rental-service.url}")
    private String rentalServiceUrl;

    @Value("${gateway.services.payment-service.url}")
    private String paymentServiceUrl;

    // Rate Limiting конфигурацияя
    @Value("${rate.limit.auth.capacity:10}")
    private int authCapacity;

    @Value("${rate.limit.auth.refill:10}")
    private int authRefill;

    @Value("${rate.limit.auth.period:60}")
    private int authPeriod;

    @Value("${rate.limit.user.capacity:100}")
    private int userCapacity;

    @Value("${rate.limit.user.refill:100}")
    private int userRefill;

    @Value("${rate.limit.user.period:60}")
    private int userPeriod;

    @Value("${rate.limit.admin.capacity:200}")
    private int adminCapacity;

    @Value("${rate.limit.admin.refill:200}")
    private int adminRefill;

    @Value("${rate.limit.admin.period:60}")
    private int adminPeriod;

    @Value("${rate.limit.scooter.capacity:50}")
    private int scooterCapacity;

    @Value("${rate.limit.scooter.refill:50}")
    private int scooterRefill;

    @Value("${rate.limit.scooter.period:60}")
    private int scooterPeriod;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                
                .route("user-service-auth", r -> r
                        .path("/auth/**")
                        .filters(f -> f
                                .filter(rateLimitingFilter.apply(createRateLimitConfig(authCapacity, authRefill, authPeriod)))
                                .circuitBreaker(c -> c
                                        .setName("user-service-auth")
                                        .setFallbackUri("forward:/fallback/user-service")
                                )
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, org.springframework.http.HttpMethod.POST)
                                        .setBackoff(
                                                java.time.Duration.ofMillis(1000),
                                                java.time.Duration.ofMillis(2000),
                                                2,
                                                false
                                        )
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                )
                        )
                        .uri(userServiceUrl)
                )

                .route("user-service-users", r -> r
                        .path("/users/**")
                        .filters(f -> f
                                .filter(rateLimitingFilter.apply(createRateLimitConfig(userCapacity, userRefill, userPeriod)))
                                .circuitBreaker(c -> c
                                        .setName("user-service-users")
                                        .setFallbackUri("forward:/fallback/user-service")
                                )
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, org.springframework.http.HttpMethod.POST, org.springframework.http.HttpMethod.PUT, org.springframework.http.HttpMethod.DELETE)
                                        .setBackoff(
                                                java.time.Duration.ofMillis(1000),
                                                java.time.Duration.ofMillis(2000),
                                                2,
                                                false
                                        )
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                )
                        )
                        .uri(userServiceUrl)
                )
                
                .route("scooter-service", r -> r
                        .path("/scooters/**")
                        .and()
                        .not(p -> p.path("/scooters/*/full"))
                        .filters(f -> f
                                .filter(rateLimitingFilter.apply(createRateLimitConfig(scooterCapacity, scooterRefill, scooterPeriod)))
                                .circuitBreaker(c -> c
                                        .setName("scooter-service")
                                        .setFallbackUri("forward:/fallback/scooter-service")
                                )
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, org.springframework.http.HttpMethod.POST)
                                        .setBackoff(
                                                java.time.Duration.ofMillis(1000),
                                                java.time.Duration.ofMillis(2000),
                                                2,
                                                false
                                        )
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                )
                        )
                        .uri(scooterServiceUrl)
                )
                
                .route("rental-service", r -> r
                        .path("/rentals/**")
                        .filters(f -> f
                                .filter(rateLimitingFilter.apply(createRateLimitConfig(userCapacity, userRefill, userPeriod)))
                                .circuitBreaker(c -> c
                                        .setName("rental-service")
                                        .setFallbackUri("forward:/fallback/rental-service")
                                )
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, org.springframework.http.HttpMethod.POST, org.springframework.http.HttpMethod.PUT)
                                        .setBackoff(
                                                java.time.Duration.ofMillis(1000),
                                                java.time.Duration.ofMillis(2000),
                                                2,
                                                false
                                        )
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                )
                        )
                        .uri(rentalServiceUrl)
                )
                
                .route("payment-service", r -> r
                        .path("/payment-methods/**")
                        .filters(f -> f
                                .filter(rateLimitingFilter.apply(createRateLimitConfig(userCapacity, userRefill, userPeriod)))
                                .circuitBreaker(c -> c
                                        .setName("payment-service")
                                        .setFallbackUri("forward:/fallback/payment-service")
                                )
                                .retry(retryConfig -> retryConfig
                                        .setRetries(3)
                                        .setMethods(org.springframework.http.HttpMethod.GET, org.springframework.http.HttpMethod.POST, org.springframework.http.HttpMethod.DELETE)
                                        .setBackoff(
                                                java.time.Duration.ofMillis(1000),
                                                java.time.Duration.ofMillis(2000),
                                                2,
                                                false
                                        )
                                        .setStatuses(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.BAD_GATEWAY, HttpStatus.SERVICE_UNAVAILABLE)
                                )
                        )
                        .uri(paymentServiceUrl)
                )
                .build();
    }

    private RateLimitingFilter.Config createRateLimitConfig(int capacity, int refill, int period) {
        RateLimitingFilter.Config config = new RateLimitingFilter.Config();
        config.setCapacity(capacity);
        config.setRefillTokens(refill);
        config.setRefillPeriod(period);
        return config;
    }
}

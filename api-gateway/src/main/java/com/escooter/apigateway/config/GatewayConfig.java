package com.escooter.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${gateway.services.user-service.url}")
    private String userServiceUrl;

    @Value("${gateway.services.scooter-service.url}")
    private String scooterServiceUrl;

    @Value("${gateway.services.rental-service.url}")
    private String rentalServiceUrl;

    @Value("${gateway.services.payment-service.url}")
    private String paymentServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes - без аутентификации для /auth/**
                .route("user-service-auth", r -> r
                        .path("/auth/**")
                        .uri(userServiceUrl)
                )
                // User Service Routes - JWT проверка выполняется глобально через JwtAuthenticationFilter
                .route("user-service-users", r -> r
                        .path("/users/**")
                        .uri(userServiceUrl)
                )
                // Scooter Service Routes - исключаем composite endpoint
                .route("scooter-service", r -> r
                        .path("/scooters/**")
                        .and()
                        .not(p -> p.path("/scooters/*/full"))
                        .uri(scooterServiceUrl)
                )
                // Rental Service Routes
                .route("rental-service", r -> r
                        .path("/rentals/**")
                        .uri(rentalServiceUrl)
                )
                // Payment Service Routes
                .route("payment-service", r -> r
                        .path("/payment-methods/**")
                        .uri(paymentServiceUrl)
                )
                .build();
    }
}

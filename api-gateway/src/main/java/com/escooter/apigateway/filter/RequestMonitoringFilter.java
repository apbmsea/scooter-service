package com.escooter.apigateway.filter;

import com.escooter.apigateway.dto.RequestDto;
import com.escooter.apigateway.service.MonitoringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RequestMonitoringFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {

    @Autowired
    private MonitoringService monitoringService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod().toString();
        
        long startTime = System.currentTimeMillis();

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            org.springframework.http.HttpStatusCode statusCode = response.getStatusCode();
            long duration = System.currentTimeMillis() - startTime;

            // Определяем тип запроса
            RequestDto.Type type = RequestDto.Type.SUCCESS;
            int statusValue = 200;
            if (statusCode != null) {
                statusValue = statusCode.value();
                if (statusValue >= 500) {
                    type = RequestDto.Type.ERROR;
                } else if (statusValue >= 400) {
                    type = RequestDto.Type.WARNING;
                }
            }

            // Определяем сервис по пути
            String service = determineService(path);

            // Логируем запрос
            monitoringService.logRequest(
                    method,
                    String.valueOf(statusValue),
                    String.format("Request completed in %dms", duration),
                    service,
                    type
            );

            log.debug("Request {} {} completed with status {} in {}ms", method, path, statusValue, duration);
        }));
    }

    private String determineService(String path) {
        if (path.startsWith("/auth/") || path.startsWith("/users/")) {
            return "user-service";
        } else if (path.startsWith("/scooters/")) {
            return "scooter-service";
        } else if (path.startsWith("/rentals/")) {
            return "rental-service";
        } else if (path.startsWith("/payment-methods/")) {
            return "payment-service";
        } else if (path.startsWith("/admin/")) {
            return "admin-service";
        }
        return "api-gateway";
    }

    @Override
    public int getOrder() {
        return -1; // Выполняется первым
    }
}


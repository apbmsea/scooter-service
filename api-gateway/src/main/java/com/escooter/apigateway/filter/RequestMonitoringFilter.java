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

            String service = determineService(path);

            String operationMessage = determineOperationMessage(method, path, service);

            monitoringService.logRequest(
                    method,
                    String.valueOf(statusValue),
                    operationMessage,
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

    private String determineOperationMessage(String method, String path, String service) {
        // Для user-service
        if ("user-service".equals(service)) {
            if ("POST".equals(method)) {
                if (path.equals("/auth/register")) {
                    return "Создание пользователя";
                } else if (path.equals("/auth/login")) {
                    return "Вход в систему";
                } else if (path.equals("/auth/logout")) {
                    return "Выход из системы";
                } else if (path.equals("/auth/refresh")) {
                    return "Обновление токена";
                } else if (path.equals("/auth/forgot-password")) {
                    return "Сброс пароля";
                }
            } else if ("GET".equals(method)) {
                if (path.equals("/users/me")) {
                    return "Получение текущего пользователя";
                } else if (path.equals("/users")) {
                    return "Получение списка пользователей";
                }
            } else if ("PUT".equals(method)) {
                if (path.equals("/users/me")) {
                    return "Обновление пользователя";
                }
            } else if ("DELETE".equals(method)) {
                if (path.startsWith("/users/") && path.length() > 7) {
                    String rest = path.substring(7);
                    if (rest.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
                        return "Удаление пользователя по ID";
                    } else if (rest.startsWith("by-email/")) {
                        return "Удаление пользователя по email";
                    } else if (rest.startsWith("by-phone/")) {
                        return "Удаление пользователя по телефону";
                    }
                }
            }
        }
        
        return "Выполнение запроса";
    }

    @Override
    public int getOrder() {
        return -1; // Выполняется первым
    }
}


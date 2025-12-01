package com.escooter.apigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    /**
     * HTTP метод запроса (GET, POST, PUT, DELETE и т.д.)
     */
    private String method;

    /**
     * Статус: HTTP-код или краткое текстовое имя статуса.
     */
    private String status;

    /**
     * Сообщение ошибки или описание результата.
     */
    private String message;

    /**
     * Время запроса сервиса в ISO‑формате (например, 2025-12-01T12:34:56Z).
     */
    private String time;

    /**
     * Название сервиса (user-service, scooter-service, rental-service и т.п.).
     */
    private String service;

    /**
     * Тип результата.
     * SUCCESS – запрос прошёл успешно;
     * WARNING – запрос не прошёл из-за некорректных данных пользователя, валидации и т.п.;
     * ERROR – внутренняя ошибка сервиса (5xx) или недоступность.
     */
    private Type type;

    public enum Type {
        SUCCESS,
        WARNING,
        ERROR
    }
}



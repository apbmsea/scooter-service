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

    private String method;

    private String status;

    private String message;

    private String time;

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



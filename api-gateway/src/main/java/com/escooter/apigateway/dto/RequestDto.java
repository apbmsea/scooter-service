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
    
    private Type type;

    public enum Type {
        SUCCESS,
        WARNING,
        ERROR
    }
}



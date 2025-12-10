package test.weewee.userservice.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ErrorResponse {
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse of(String message, Map<String, String> errors) {
        return new ErrorResponse(message, errors);
    }

    public static ErrorResponse of(String message, String key, String value) {
        return new ErrorResponse(message, Map.of(key, value));
    }

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message, Map.of("error", message));
    }
}
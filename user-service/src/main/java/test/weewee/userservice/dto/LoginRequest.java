package test.weewee.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import test.weewee.userservice.validation.ValidEmail;

@Data
public class LoginRequest {
    @NotBlank(message = "Email обязателен для заполнения")
    @ValidEmail
    private String email;

    @NotBlank(message = "Пароль обязателен для заполнения")
    private String password;
}
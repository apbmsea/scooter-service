package test.weewee.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import test.weewee.userservice.validation.ValidEmail;
import test.weewee.userservice.validation.ValidPassword;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Email обязателен для заполнения")
    @ValidEmail
    private String email;

    @NotBlank(message = "Новый пароль обязателен для заполнения")
    @ValidPassword
    private String newPassword;
}
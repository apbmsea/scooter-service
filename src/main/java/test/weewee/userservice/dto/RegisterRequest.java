package test.weewee.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import test.weewee.userservice.validation.ValidEmail;
import test.weewee.userservice.validation.ValidPassword;
import test.weewee.userservice.validation.ValidPhone;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email обязателен для заполнения")
    @ValidEmail
    private String email;

    @NotBlank(message = "Пароль обязателен для заполнения")
    @ValidPassword
    private String password;

    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна для заполнения")
    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;

    @NotBlank(message = "Телефон обязателен для заполнения")
    @ValidPhone
    private String phone;
}
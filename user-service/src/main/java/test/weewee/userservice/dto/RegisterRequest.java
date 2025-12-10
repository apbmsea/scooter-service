package test.weewee.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import test.weewee.userservice.validation.ValidEmail;
import test.weewee.userservice.validation.ValidFirstName;
import test.weewee.userservice.validation.ValidLastName;
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
    @ValidFirstName
    private String firstName;

    @NotBlank(message = "Фамилия обязательна для заполнения")
    @ValidLastName
    private String lastName;

    @NotBlank(message = "Телефон обязателен для заполнения")
    @ValidPhone
    private String phone;
}
package test.weewee.userservice.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import test.weewee.userservice.validation.ValidEmail;
import test.weewee.userservice.validation.ValidPassword;
import test.weewee.userservice.validation.ValidPhone;

@Data
public class UpdateUserRequest {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
    private String firstName;

    @Size(min = 2, max = 50, message = "Фамилия должна быть от 2 до 50 символов")
    private String lastName;

    @ValidPhone
    private String phone;
}
package test.weewee.userservice.dto;

import lombok.Data;
import test.weewee.userservice.validation.ValidEmail;
import test.weewee.userservice.validation.ValidFirstName;
import test.weewee.userservice.validation.ValidLastName;
import test.weewee.userservice.validation.ValidPassword;
import test.weewee.userservice.validation.ValidPhone;

@Data
public class UpdateUserRequest {
    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

    @ValidFirstName
    private String firstName;

    @ValidLastName
    private String lastName;

    @ValidPhone
    private String phone;
}
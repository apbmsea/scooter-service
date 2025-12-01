package test.weewee.userservice.dto;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String createdAt;
    private String updatedAt;
}
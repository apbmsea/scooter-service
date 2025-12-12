package ru.college.PublicMessager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserEvent {
    private LocalDateTime timestamp;
    private String userId;
    private String eventType; // REGISTERED, UPDATED, DELETED
    private String email;
    private String city;
    private Integer age;
}

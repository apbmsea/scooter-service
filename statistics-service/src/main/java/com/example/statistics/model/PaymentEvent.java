package com.example.statistics.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentEvent {
    private LocalDateTime timestamp;
    private String paymentId;
    private String rentalId;
    private String userId;
    private Double amount;
    private String currency;
    private PaymentStatus status;
    private String paymentMethod;
    
    public enum PaymentStatus {
        SUCCEEDED, FAILED, PENDING
    }
}
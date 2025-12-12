package ru.college.PublicMessager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentEvent {
    private LocalDateTime timestamp;
    private String paymentId;
    private String rentalId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String paymentMethod;

    public enum PaymentStatus { SUCCEEDED, FAILED, PENDING }
}

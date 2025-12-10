package com.escooter.apigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/user-service")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        log.warn("User service fallback triggered - Circuit Breaker is OPEN");
        return createFallbackResponse("user-service", "User service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/scooter-service")
    public ResponseEntity<Map<String, Object>> scooterServiceFallback() {
        log.warn("Scooter service fallback triggered - Circuit Breaker is OPEN");
        return createFallbackResponse("scooter-service", "Scooter service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/rental-service")
    public ResponseEntity<Map<String, Object>> rentalServiceFallback() {
        log.warn("Rental service fallback triggered - Circuit Breaker is OPEN");
        return createFallbackResponse("rental-service", "Rental service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/payment-service")
    public ResponseEntity<Map<String, Object>> paymentServiceFallback() {
        log.warn("Payment service fallback triggered - Circuit Breaker is OPEN");
        return createFallbackResponse("payment-service", "Payment service is temporarily unavailable. Please try again later.");
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String service, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "Service Unavailable");
        body.put("message", message);
        body.put("service", service);
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("timestamp", System.currentTimeMillis());
        body.put("circuitBreaker", "OPEN");

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(body);
    }
}


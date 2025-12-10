package com.example.statistics.service;

import com.example.statistics.model.*;
import com.example.statistics.repository.StatisticsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageConsumer {
    
    private final StatisticsRepository repository;
    private final ObjectMapper objectMapper;
    
    @RabbitListener(queues = "rental.events", containerFactory = "rabbitListenerContainerFactory")
    public void handleRentalEvent(RentalEvent event) {
        try {
            repository.saveRentalEvent(event);
            log.info("Processed rental event: {}", event.getRentalId());
        } catch (Exception e) {
            log.error("Error processing rental event: {}", e.getMessage());
        }
    }
    
    @RabbitListener(queues = "payment.events", containerFactory = "rabbitListenerContainerFactory")
    public void handlePaymentEvent(PaymentEvent event) {
        try {
            repository.savePaymentEvent(event);
            log.info("Processed payment event: {}", event.getPaymentId());
        } catch (Exception e) {
            log.error("Error processing payment event: {}", e.getMessage());
        }
    }
    
    @RabbitListener(queues = "user.events", containerFactory = "rabbitListenerContainerFactory")
    public void handleUserEvent(UserEvent event) {
        try {
            repository.saveUserEvent(event);
            log.info("Processed user event: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Error processing user event: {}", e.getMessage());
        }
    }
}

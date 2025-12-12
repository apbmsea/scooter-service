package ru.college.PublicMessager.service;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.college.PublicMessager.dto.PaymentEvent;
import ru.college.PublicMessager.dto.RentalEvent;
import ru.college.PublicMessager.dto.UserEvent;

@Service
public class PublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;


    public PublisherService(RabbitTemplate rabbitTemplate, DirectExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendRentalEvent(RentalEvent event){
        System.out.println(event);
        rabbitTemplate.convertAndSend(exchange.getName(), "rental.events", event);
    }

    public void sendPaymentEvent(PaymentEvent event){
        System.out.println(event);
        rabbitTemplate.convertAndSend(exchange.getName(), "payment.events", event);
    }

    public void sendUserEvent(UserEvent event){
        System.out.println(event);
        rabbitTemplate.convertAndSend(exchange.getName(), "user.events", event);
    }
}

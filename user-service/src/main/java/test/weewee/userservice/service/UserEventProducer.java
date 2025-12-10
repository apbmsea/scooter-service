package test.weewee.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import test.weewee.userservice.dto.UserEvent;
import test.weewee.userservice.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Отправка событий пользователей в RabbitMQ
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final String USER_EXCHANGE = "user.exchange";

    public void sendUserCreatedEvent(User user) {
        try {
            UserEvent event = UserEvent.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phone(user.getPhone())
                    .role(user.getRole().name())
                    .timestamp(LocalDateTime.now())
                    .eventType(UserEvent.EventType.CREATED)
                    .build();

            rabbitTemplate.convertAndSend(USER_EXCHANGE, "user.created", event);
            log.info("User created event sent for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send user created event for user: {}", user.getEmail(), e);
        }
    }

    public void sendUserUpdatedEvent(User user) {
        try {
            UserEvent event = UserEvent.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phone(user.getPhone())
                    .role(user.getRole().name())
                    .timestamp(LocalDateTime.now())
                    .eventType(UserEvent.EventType.UPDATED)
                    .build();

            rabbitTemplate.convertAndSend(USER_EXCHANGE, "user.updated", event);
            log.info("User updated event sent for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send user updated event for user: {}", user.getEmail(), e);
        }
    }

    public void sendUserDeletedEvent(UUID userId, String email) {
        try {
            UserEvent event = UserEvent.builder()
                    .userId(userId)
                    .email(email)
                    .timestamp(LocalDateTime.now())
                    .eventType(UserEvent.EventType.DELETED)
                    .build();

            rabbitTemplate.convertAndSend(USER_EXCHANGE, "user.deleted", event);
            log.info("User deleted event sent for user: {}", email);
        } catch (Exception e) {
            log.error("Failed to send user deleted event for user: {}", email, e);
        }
    }
}


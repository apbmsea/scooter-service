package ru.max.geolocationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.max.geolocationservice.models.ScooterUpdateEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScooterUpdateReceiver {
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${geo.key}")
    private String geoKey;

    @RabbitListener(queues = "${scooter.queue}")
    public void handleScooterUpdate(ScooterUpdateEvent event) {
        redisTemplate.opsForGeo().add(
                geoKey,
                new Point(event.getLng(), event.getLat()),
                event.getScooterId()
        );
        //TODO спросить нужно ли что бы сюда приходил так же уровень батареи
        log.info("Scooter update received: {}", event.getScooterId() + event.getLng() + event.getLat());
    }
}

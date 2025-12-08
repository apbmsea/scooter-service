package ru.max.geolocationservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.max.geolocationservice.models.ScooterDTO;
import ru.max.geolocationservice.service.ScooterSensorService;

@Service
@RequiredArgsConstructor
public class ScooterSensorServiceImpl implements ScooterSensorService {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${geo.key}")
    private String geoKey;

    public void updateScooter(Long scooterId, ScooterDTO scooterJson){
        redisTemplate.opsForGeo().add(geoKey, new Point(scooterJson.getLng(), scooterJson.getLat()),
                String.valueOf(scooterId));

        String scooterHash = "scooter" + scooterId;

        redisTemplate.opsForHash().put(scooterHash, "battery", String.valueOf(scooterJson.getBatteryLevel()));
        redisTemplate.opsForHash().put(scooterHash, "status", scooterJson.getStatus().name());
        redisTemplate.opsForHash().put(scooterHash, "lat", String.valueOf(scooterJson.getLat()));
        redisTemplate.opsForHash().put(scooterHash, "lng", String.valueOf(scooterJson.getLng()));
        redisTemplate.opsForHash().put(scooterHash, "updated", String.valueOf(System.currentTimeMillis()));
    }
}

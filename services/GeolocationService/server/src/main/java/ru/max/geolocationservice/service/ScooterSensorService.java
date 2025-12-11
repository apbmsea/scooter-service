package ru.max.geolocationservice.service;

import ru.max.geolocationservice.models.ScooterDTO;

public interface ScooterSensorService {
    void updateScooter(Long scooterId, ScooterDTO scooterJson);
}

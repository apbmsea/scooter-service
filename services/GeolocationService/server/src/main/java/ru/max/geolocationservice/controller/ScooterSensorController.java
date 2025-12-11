package ru.max.geolocationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.max.geolocationservice.models.ScooterDTO;
import ru.max.geolocationservice.service.ScooterSensorService;

@RestController
@RequestMapping("/sensor")
@RequiredArgsConstructor
public class ScooterSensorController {
    private final ScooterSensorService scooterSensorService;
    @PostMapping("/update")
    public void updateScooterGeoFromSensor(@RequestBody ScooterDTO scooterJson) {
        scooterSensorService.updateScooter(scooterJson.getId(), scooterJson);
    }
}

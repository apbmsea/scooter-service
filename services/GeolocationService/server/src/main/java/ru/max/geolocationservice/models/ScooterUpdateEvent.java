package ru.max.geolocationservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScooterUpdateEvent {
    private String scooterId;
    private double lat;
    private double lng;
    private double batteryLevel;
    private String status;
    private String timestamp;
}

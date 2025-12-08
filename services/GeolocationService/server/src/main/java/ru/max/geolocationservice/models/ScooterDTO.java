package ru.max.geolocationservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.max.geolocationservice.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScooterDTO {
    private Long id;

    private double lat;

    private double lng;

    private double batteryLevel;

    private Status status;
}

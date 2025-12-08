package ru.max.geolocationservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Polygon;
import ru.max.geolocationservice.enums.Type;

@Entity
@Getter
@Setter
public class ParkingZone {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(columnDefinition = "geometry(Polygon, 4326)")
    private Polygon area;


}

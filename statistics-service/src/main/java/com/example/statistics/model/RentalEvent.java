package com.example.statistics.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RentalEvent {
    private LocalDateTime timestamp;
    private String rentalId;
    private String userId;
    private String scooterId;
    private EventType eventType;
    private Location location;
    private Integer batteryLevel;
    private Long durationSeconds;
    private Double revenue;
    private String city;
    
    public enum EventType {
        STARTED, ENDED
    }
    
    @Data
    public static class Location {
        private Double lat;
        private Double lon;
    }
}

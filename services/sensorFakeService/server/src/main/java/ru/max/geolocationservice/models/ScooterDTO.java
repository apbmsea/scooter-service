package ru.max.geolocationservice.models;

import ru.max.geolocationservice.enums.Status;

public class ScooterDTO {
    private Long id;

    private double lat;

    private double lng;

    private double batteryLevel;


    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public String toString() {
        return "ScooterDTO{" +
                "id=" + id +
                ", lat=" + lat +
                ", lng=" + lng +
                ", batteryLevel=" + batteryLevel +
                ", status=" + status +
                '}';
    }
}

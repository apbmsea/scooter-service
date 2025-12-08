package ru.max.geolocationservice.service;

import ru.max.geolocationservice.models.ScooterInfo;

import java.util.List;

public interface ZoneService {
    String checkPoint(double lat, double lng);
    List<String> getScootersNearby(double lat, double lng, double radius);
    List<String> getAllScooters();
    List<ScooterInfo> getAllScootersWithCoords();
}

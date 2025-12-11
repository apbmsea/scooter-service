package ru.max.geolocationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.max.geolocationservice.models.ScooterInfo;
import ru.max.geolocationservice.service.ZoneService;

import java.util.List;

@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class ZoneController {
    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<List<String>> getScootersNearby(@RequestParam double lat,
                                                              @RequestParam double lng,
                                                              @RequestParam int radius) {
        return ResponseEntity.ok(zoneService.getScootersNearby(lat, lng, radius));
    }

    @GetMapping("/scooters")
    public ResponseEntity<List<String>> getScooters(){
        return ResponseEntity.ok(zoneService.getAllScooters());
    }

    @GetMapping("/scooters/coords")
    public ResponseEntity<List<ScooterInfo>> getScootersWithCoords(){
        return ResponseEntity.ok(zoneService.getAllScootersWithCoords());
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkZone(@RequestParam double lat, @RequestParam double lng) {
        return ResponseEntity.ok(zoneService.checkPoint(lat, lng));
    }
}

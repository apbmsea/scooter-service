package com.example.statistics.controller;

import com.example.statistics.model.*;
import com.example.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticsController {
    
    private final StatisticsRepository repository;
    
    @GetMapping("/trips")
    public ResponseEntity<StatisticResponse> getTripsStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "daily") String aggregation) {
        
        StatisticResponse response = new StatisticResponse();
        response.setData(repository.getTripsStatistics(start, end, aggregation));
        response.setPeriod(start + " to " + end);
        response.setMetric("trips_count");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/revenue")
    public ResponseEntity<StatisticResponse> getRevenueStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "daily") String aggregation) {
        
        StatisticResponse response = new StatisticResponse();
        response.setData(repository.getRevenueStatistics(start, end, aggregation));
        response.setPeriod(start + " to " + end);
        response.setMetric("revenue");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/hotspots")
    public ResponseEntity<StatisticResponse> getHotspots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "10") int limit) {
        
        StatisticResponse response = new StatisticResponse();
        response.setData(repository.getHotspots(start, end, limit));
        response.setPeriod(start + " to " + end);
        response.setMetric("hotspots");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/operational")
    public ResponseEntity<Map<String, Object>> getOperationalStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("average_rental_duration_minutes", repository.getAverageRentalDuration(start, end));
        stats.put("period", start + " to " + end);
        
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/retention")
    public ResponseEntity<Map<String, Object>> getRetentionRate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodEnd) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("retention_rate", repository.getRetentionRate(periodStart, periodEnd));
        response.put("period", periodStart + " to " + periodEnd);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/basic")
    public ResponseEntity<Map<String, Object>> getBasicStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<String, Object> stats = repository.getBasicStats(start, end);
        stats.put("period", start + " to " + end);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/active-users")
    public ResponseEntity<Map<String, Object>> getActiveUsers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("active_users", repository.getActiveUsersCount(start, end));
        response.put("period", start + " to " + end);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/total-revenue")
    public ResponseEntity<Map<String, Object>> getTotalRevenue(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("total_revenue", repository.getTotalRevenue(start, end));
        response.put("period", start + " to " + end);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/trips/city")
    public ResponseEntity<StatisticResponse> getTripsByCity(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String city) {
        
        StatisticResponse response = new StatisticResponse();
        response.setData(repository.getTripsByCity(start, end, city));
        response.setPeriod(start + " to " + end);
        response.setMetric("trips_by_city_" + city);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/scooter-utilization")
    public ResponseEntity<Map<String, Object>> getScooterUtilization(
            @RequestParam String scooterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("scooter_id", scooterId);
        response.put("utilization_rate", repository.getScooterUtilization(scooterId, start, end));
        response.put("period", start + " to " + end);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("service", "Statistics Service");
        return ResponseEntity.ok(response);
    }
}


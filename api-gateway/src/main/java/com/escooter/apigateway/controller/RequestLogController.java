package com.escooter.apigateway.controller;

import com.escooter.apigateway.dto.RequestDto;
import com.escooter.apigateway.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitoring")
@RequiredArgsConstructor
public class RequestLogController {

    private final MonitoringService monitoringService;

    @GetMapping("/get")
    public ResponseEntity<List<RequestDto>> getRequests(
            @RequestParam(required = false, defaultValue = "100") int limit) {
        List<RequestDto> requests = monitoringService.getRecentRequests(limit);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = Map.of(
                "totalRequests", monitoringService.getAllRequests().size(),
                "errorCount", monitoringService.getErrorCount("all")
        );
        return ResponseEntity.ok(stats);
    }
}

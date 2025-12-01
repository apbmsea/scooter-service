package com.escooter.apigateway.service;

import com.escooter.apigateway.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
public class MonitoringService {

    private final ConcurrentLinkedQueue<RequestDto> requestLogs = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<String, Long> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> errorCounts = new ConcurrentHashMap<>();
    private static final int MAX_LOGS = 1000;

    public void logRequest(String method, String status, String message, String service, RequestDto.Type type) {
        RequestDto request = RequestDto.builder()
                .method(method)
                .status(status)
                .message(message)
                .time(OffsetDateTime.now(ZoneOffset.UTC).toString())
                .service(service)
                .type(type)
                .build();

        // Добавляем в очередь
        requestLogs.offer(request);
        
        // Ограничиваем размер
        while (requestLogs.size() > MAX_LOGS) {
            requestLogs.poll();
        }

        // Обновляем счетчики
        String key = service + ":" + status;
        requestCounts.merge(key, 1L, Long::sum);
        
        if (type == RequestDto.Type.ERROR) {
            errorCounts.merge(service, 1L, Long::sum);
        }

        log.debug("Logged request: {} {} {} - {}", method, status, service, message);
    }

    public List<RequestDto> getRecentRequests(int limit) {
        List<RequestDto> recent = new ArrayList<>();
        int count = 0;
        for (RequestDto request : requestLogs) {
            if (count >= limit) break;
            recent.add(0, request); // Добавляем в начало для обратного порядка
            count++;
        }
        return recent;
    }

    public List<RequestDto> getAllRequests() {
        return new ArrayList<>(requestLogs);
    }

    public long getRequestCount(String service, String status) {
        return requestCounts.getOrDefault(service + ":" + status, 0L);
    }

    public long getErrorCount(String service) {
        return errorCounts.getOrDefault(service, 0L);
    }
}

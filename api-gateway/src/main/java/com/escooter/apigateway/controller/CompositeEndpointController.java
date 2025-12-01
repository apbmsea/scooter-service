package com.escooter.apigateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/scooters")
public class CompositeEndpointController {

    private final WebClient scooterServiceClient;
    private final WebClient rentalServiceClient;

    public CompositeEndpointController(
            @Value("${gateway.services.scooter-service.url}") String scooterServiceUrl,
            @Value("${gateway.services.rental-service.url}") String rentalServiceUrl) {
        this.scooterServiceClient = WebClient.builder()
                .baseUrl(scooterServiceUrl)
                .build();
        this.rentalServiceClient = WebClient.builder()
                .baseUrl(rentalServiceUrl)
                .build();
    }

    @GetMapping("/{id}/full")
    public Mono<ResponseEntity<Map<String, Object>>> getScooterDetails(@PathVariable String id) {
        log.info("Fetching full details for scooter: {}", id);

        // Шаг 1: Получаем данные самоката
        Mono<Map<String, Object>> scooterMono = scooterServiceClient
                .get()
                .uri("/scooters/{id}", id)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (Map<String, Object>) map)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.error("Error fetching scooter: {}", e.getMessage());
                    return Mono.just(new HashMap<String, Object>());
                });

        // Шаг 2: Получаем активную аренду (если самокат в аренде)
        Mono<Map<String, Object>> rentalMono = scooterServiceClient
                .get()
                .uri("/scooters/{id}", id)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (Map<String, Object>) map)
                .flatMap(scooter -> {
                    String status = (String) scooter.get("status");
                    if ("rented".equals(status)) {
                        return rentalServiceClient
                                .get()
                                .uri("/rentals/scooter/{id}/active", id)
                                .retrieve()
                                .bodyToMono(Map.class)
                                .map(map -> (Map<String, Object>) map)
                                .timeout(Duration.ofSeconds(5))
                                .onErrorResume(e -> {
                                    log.warn("No active rental found for scooter: {}", id);
                                    return Mono.just(new HashMap<String, Object>());
                                });
                    }
                    return Mono.just(new HashMap<String, Object>());
                })
                .onErrorResume(e -> Mono.just(new HashMap<String, Object>()));

        // Композиция результатов
        return Mono.zip(scooterMono, rentalMono)
                .map(tuple -> {
                    Map<String, Object> scooter = tuple.getT1();
                    Map<String, Object> rental = tuple.getT2();

                    Map<String, Object> result = new HashMap<>(scooter);
                    if (!rental.isEmpty()) {
                        result.put("activeRental", rental);
                    }
                    return ResponseEntity.ok(result);
                })
                .onErrorResume(e -> {
                    log.error("Error composing scooter details: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Failed to fetch scooter details")));
                });
    }
}


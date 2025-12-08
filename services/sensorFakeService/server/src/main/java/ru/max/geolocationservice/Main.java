package ru.max.geolocationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.max.geolocationservice.models.ScooterDTO;
import ru.max.geolocationservice.enums.Status;
import ru.max.geolocationservice.sensor.FakeSensorStub;

import java.util.*;

public class Main {
    private static final List<Long> scootersId = List.of(1L, 2L, 3L, 4L, 5L);
    private static final List<Status> statuses = List.of(Status.AVAILABLE, Status.RENTED, Status.MAINTENANCE);
    private static Map<Long, Double> lat = new HashMap<>();
    private static Map<Long, Double> lng = new HashMap<>();
    private static final String BACKEND_URL = "http://localhost:8081";

    public static void main(String[] args) {
        FakeSensorStub stub = new FakeSensorStub(new ObjectMapper());
        for (Long scooterId: scootersId) {
            lat.put(scooterId, 56.838011);
            lng.put(scooterId, 60.597465);
        }

        ScooterDTO scooterDto = new ScooterDTO();
        Random random = new Random();

        while (true) {
            for (Long scooterId: scootersId) {

                lat.put(scooterId, lat.get(scooterId) + (random.nextDouble() - 0.5) / 1000);
                lng.put(scooterId, lng.get(scooterId) + (random.nextDouble() - 0.5) / 1000);

                double currentLat = lat.get(scooterId);
                double currentLng = lng.get(scooterId);

                scooterDto.setId(scooterId);
                scooterDto.setLat(currentLat);
                scooterDto.setLng(currentLng);
                scooterDto.setBatteryLevel(random.nextInt(20) + 80);
                scooterDto.setStatus(statuses.get(random.nextInt(statuses.size())));

                stub.sendPost(BACKEND_URL + "/sensor/update", scooterDto);
                System.out.println("Sent for scooter" + scooterDto.getId() + ": " + scooterDto);
            }
            try {
                Thread.sleep(30_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
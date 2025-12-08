package ru.max.geolocationservice.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.max.geolocationservice.models.ScooterDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FakeSensorStub {

    private final ObjectMapper objectMapper;

    public FakeSensorStub(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void sendPost(String url, ScooterDTO scooterJson){
        try {
            URL geoUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) geoUrl.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try(OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(objectMapper.writeValueAsString(scooterJson).getBytes());
            }

            connection.getResponseCode();

        }catch (IOException e){
            throw new RuntimeException("Network error while sending post", e);
        }
    }
}

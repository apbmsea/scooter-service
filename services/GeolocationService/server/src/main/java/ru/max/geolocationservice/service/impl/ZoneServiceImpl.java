package ru.max.geolocationservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.max.geolocationservice.models.ParkingZone;
import ru.max.geolocationservice.models.ScooterInfo;
import ru.max.geolocationservice.repos.ParkingZoneRepository;
import ru.max.geolocationservice.service.ZoneService;
import org.springframework.data.geo.*;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {
    private final ParkingZoneRepository parkingZoneRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${geo.key}")
    private String geoKey;

    @Override
    public List<String> getScootersNearby(double lat, double lng, double radius){
        Circle area = new Circle(new org.springframework.data.geo.Point(lat, lng), new Distance(radius, Metrics.KILOMETERS));

        GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                redisTemplate.opsForGeo().radius(geoKey, area);

        return results.getContent().stream()
                .map(r -> r.getContent().getName())
                .toList();
    }

    @Override
    public List<String> getAllScooters() {
        List<RedisGeoCommands.GeoLocation<String>> all = redisTemplate.opsForGeo().radius(
                        geoKey,
                        new org.springframework.data.geo.Circle(new org.springframework.data.geo.Point(0,0),
                                new org.springframework.data.geo.Distance(20000, org.springframework.data.geo.Metrics.KILOMETERS))
                ).getContent().stream()
                .map(r -> r.getContent())
                .toList();

        return all.stream().map(RedisGeoCommands.GeoLocation::getName).toList();
    }

    @Override
    public List<ScooterInfo> getAllScootersWithCoords(){
        List<RedisGeoCommands.GeoLocation<String>> all = redisTemplate.opsForGeo()
                .radius(
                        geoKey,
                        new org.springframework.data.geo.Circle(
                                new org.springframework.data.geo.Point(0, 0),
                                new org.springframework.data.geo.Distance(20000, org.springframework.data.geo.Metrics.KILOMETERS)
                        )
                ).getContent()
                .stream()
                .map(r -> r.getContent())
                .filter(loc -> loc.getName() != null && !"null".equals(loc.getName()))
                .toList();

        return all.stream()
                .map(loc -> {
                    org.springframework.data.geo.Point point = loc.getPoint();
                    return new ScooterInfo(loc.getName(), point.getX(), point.getY());
                })
                .collect(Collectors.toList());
    }

    @Override
    public String checkPoint(double lat, double lng) {
        Point point = geometryFactory.createPoint(new Coordinate(lat, lng));
        Optional<ParkingZone> zone = parkingZoneRepository.findAll()
                .stream()
                .filter(z -> z.getArea() != null && z.getArea().contains(point))
                .findFirst();

        return zone.map(z -> "Point is in " + z.getType() + " zone: " + z.getName())
                .orElse("Point is not in any zone");
    }
}

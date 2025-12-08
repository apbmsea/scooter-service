package ru.max.geolocationservice.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.max.geolocationservice.models.ParkingZone;

@Repository
public interface ParkingZoneRepository extends JpaRepository<ParkingZone, Long> {
}

package brickboy.Infrastructure.Implimentation.v1;

import brickboy.Infrastructure.Entity.v1.Logi.EventDataLogi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface    RepositoryEventDataLogiIml extends JpaRepository<EventDataLogi, UUID> {
}

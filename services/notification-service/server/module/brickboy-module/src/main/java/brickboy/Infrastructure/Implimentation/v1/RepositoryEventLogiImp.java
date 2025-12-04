package brickboy.Infrastructure.Implimentation.v1;

import brickboy.Infrastructure.Entity.v1.Logi.EventLogi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepositoryEventLogiImp extends JpaRepository<EventLogi, UUID> {
}

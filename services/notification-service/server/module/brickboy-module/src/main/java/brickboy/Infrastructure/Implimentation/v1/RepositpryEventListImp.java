package brickboy.Infrastructure.Implimentation.v1;

import brickboy.Infrastructure.Entity.v1.EventListA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepositpryEventListImp extends JpaRepository<EventListA, UUID> {
}

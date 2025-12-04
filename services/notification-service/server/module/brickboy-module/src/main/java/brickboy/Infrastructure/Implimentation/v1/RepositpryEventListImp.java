package brickboy.Infrastructure.Implimentation.v1;

import brickboy.Infrastructure.Entity.v1.EventDataAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepositpryEventListImp extends JpaRepository<EventDataAdmin, UUID> {
    Page<EventDataAdmin> findAll(Pageable pageable);
}

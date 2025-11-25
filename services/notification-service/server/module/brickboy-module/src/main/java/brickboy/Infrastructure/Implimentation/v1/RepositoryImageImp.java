package brickboy.Infrastructure.Implimentation.v1;


import brickboy.Infrastructure.Entity.v1.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepositoryImageImp extends JpaRepository<Image, UUID> {
}

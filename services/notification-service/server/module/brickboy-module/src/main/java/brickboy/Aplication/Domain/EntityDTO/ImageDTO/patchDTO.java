package brickboy.Aplication.Domain.EntityDTO.ImageDTO;

import java.util.UUID;

public class patchDTO {
    private UUID id;
    private String patch;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }
}

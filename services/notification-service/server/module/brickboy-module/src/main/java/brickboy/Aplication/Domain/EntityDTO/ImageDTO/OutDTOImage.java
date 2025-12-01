package brickboy.Aplication.Domain.EntityDTO.ImageDTO;

import java.util.UUID;

public class OutDTOImage {

    private UUID id;

    private String filename;

    private String patchToImage;

    private UUID creator;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPatchToImage() {
        return patchToImage;
    }

    public void setPatchToImage(String patchToImage) {
        this.patchToImage = patchToImage;
    }

    public UUID getCreator() {
        return creator;
    }

    public void setCreator(UUID creator) {
        this.creator = creator;
    }
}

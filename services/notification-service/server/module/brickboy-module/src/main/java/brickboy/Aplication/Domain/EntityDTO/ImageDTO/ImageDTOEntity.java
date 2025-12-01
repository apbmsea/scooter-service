package brickboy.Aplication.Domain.EntityDTO.ImageDTO;

import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class ImageDTOEntity {
//    private UUID id;
    private String filename;
    private String patchTOImge;
    private UUID creatorId;



    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPatchTOImge() {
        return patchTOImge;
    }

    public void setPatchTOImge(String patchTOImge) {
        this.patchTOImge = patchTOImge;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }
}

package brickboy.Aplication.Domain.EntityDTO.ImageDTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class FileFrontDtoSave {
    private UUID CreatorId;
    private MultipartFile file;

    public UUID getCreatorId() {
        return CreatorId;
    }

    public void setCreatorId(UUID creatorId) {
        CreatorId = creatorId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

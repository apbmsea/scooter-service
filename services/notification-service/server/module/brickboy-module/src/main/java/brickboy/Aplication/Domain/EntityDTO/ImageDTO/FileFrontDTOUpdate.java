package brickboy.Aplication.Domain.EntityDTO.ImageDTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class FileFrontDTOUpdate {

    private UUID IdOld;
    private MultipartFile newfile;
    private UUID Creatorid;

    public UUID getIdOld() {
        return IdOld;
    }

    public void setIdOld(UUID idOld) {
        IdOld = idOld;
    }

    public MultipartFile getNewfile() {
        return newfile;
    }

    public void setNewfile(MultipartFile newfile) {
        this.newfile = newfile;
    }

    public UUID getCreatorid() {
        return Creatorid;
    }

    public void setCreatorid(UUID creatorid) {
        Creatorid = creatorid;
    }
}

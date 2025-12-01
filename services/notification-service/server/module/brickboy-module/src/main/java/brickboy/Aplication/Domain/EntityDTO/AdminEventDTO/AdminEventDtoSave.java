package brickboy.Aplication.Domain.EntityDTO.AdminEventDTO;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AdminEventDtoSave {
    private String name;
    private String header;
    private String body;
    private UUID idtemplate;
    private String patchToFile;
    private UUID creator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UUID getIdtemplate() {
        return idtemplate;
    }

    public void setIdtemplate(UUID idtemplate) {
        this.idtemplate = idtemplate;
    }

    public String getPatchToFile() {
        return patchToFile;
    }

    public void setPatchToFile(String patchToFile) {
        this.patchToFile = patchToFile;
    }

    public UUID getCreator() {
        return creator;
    }

    public void setCreator(UUID creator) {
        this.creator = creator;
    }
}

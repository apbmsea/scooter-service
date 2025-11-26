package brickboy.Aplication.Domain.EntityDTO.AdminEventDTO;

import java.util.UUID;

public class EventAdminDTO {
    private String title;
    private String body;
    private UUID creator;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UUID getCreator() {
        return creator;
    }

    public void setCreator(UUID creator) {
        this.creator = creator;
    }
}

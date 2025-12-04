package brickboy.Aplication.Domain.EntityDTO.EventDTO;

import java.util.List;
import java.util.UUID;

public class FrontStartEvent {
    private UUID template;
    private UUID templateData;
    private List<String> segments;

    public FrontStartEvent(UUID template, UUID templateData, List<String> segments) {
        this.template = template;
        this.templateData = templateData;
        this.segments = segments;
    }

    // Геттеры и сеттеры
    public UUID getTemplate() {
        return template;
    }

    public void setTemplate(UUID template) {
        this.template = template;
    }

    public UUID getTemplateData() {
        return templateData;
    }

    public void setTemplateData(UUID templateData) {
        this.templateData = templateData;
    }

    public List<String> getSegments() {
        return segments;
    }

    public void setSegments(List<String> segments) {
        this.segments = segments;
    }
}

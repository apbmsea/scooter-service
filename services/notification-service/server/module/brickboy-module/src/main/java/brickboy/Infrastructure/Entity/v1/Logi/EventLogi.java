package brickboy.Infrastructure.Entity.v1.Logi;

import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.util.List;
import java.util.UUID;

@Entity // обозначаем класс как сущность базы данных
@Table(name = "event_logi") // задаём название таблицы
public class EventLogi {

    @Id // определяем ID объекта
    private UUID id;

    @Column(name = "template_id")
    private UUID template;

    @Column(name = "template_data_id")
    private UUID templateData;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "segments", joinColumns = @JoinColumn(name = "event_logi_id"))
    @Column(name = "segment_value")
    private List<String> segments;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

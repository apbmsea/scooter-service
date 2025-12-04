package brickboy.Infrastructure.Mappers.v1.EventLigi;

import brickboy.Aplication.Domain.EntityDTO.EventDTO.FrontStartEvent;
import brickboy.Infrastructure.Entity.v1.Logi.EventLogi;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class EventLogStartMapper {

    public EventLogi map(FrontStartEvent dto) {
        EventLogi eventLogi = new EventLogi();
        eventLogi.setId(UUID.randomUUID());
        eventLogi.setTemplateData(dto.getTemplateData());
        eventLogi.setTemplate(dto.getTemplate());
        eventLogi.setSegments(dto.getSegments());
        return eventLogi;
    }
}

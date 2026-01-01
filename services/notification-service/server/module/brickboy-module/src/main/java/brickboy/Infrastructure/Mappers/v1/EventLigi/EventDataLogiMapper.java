package brickboy.Infrastructure.Mappers.v1.EventLigi;

import brickboy.Infrastructure.Entity.v1.EventDataAdmin;
import brickboy.Infrastructure.Entity.v1.Logi.EventDataLogi;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventDataLogiMapper {

    public EventDataLogi map(EventDataAdmin eventDataAdmin) {
        EventDataLogi eventDataLogi = new EventDataLogi();
        eventDataLogi.setId(eventDataAdmin.getId());
        eventDataLogi.setName(eventDataAdmin.getName());
        eventDataLogi.setHeader(eventDataAdmin.getHeader());
        eventDataLogi.setBody(eventDataAdmin.getBody());
        eventDataLogi.setId_temlate(eventDataAdmin.getId_temlate());
        eventDataLogi.setCreator(eventDataAdmin.getCreator());
        eventDataLogi.setPatchToImage(eventDataAdmin.getPatchToImage());

        return eventDataLogi;
    }
}

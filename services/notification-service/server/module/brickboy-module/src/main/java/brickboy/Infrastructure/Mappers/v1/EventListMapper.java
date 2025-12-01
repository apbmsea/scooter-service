package brickboy.Infrastructure.Mappers.v1;

import brickboy.Aplication.Domain.EntityDTO.AdminEventDTO.AdminEventDtoSave;
import brickboy.Infrastructure.Entity.v1.EventListA;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
public class EventListMapper {

    public static EventListA map(AdminEventDtoSave adminDTO) {
        EventListA eventListA = new EventListA();
        eventListA.setId(UUID.randomUUID());
        eventListA.setName(adminDTO.getName());
        eventListA.setHeader(adminDTO.getHeader());
        eventListA.setBudy(adminDTO.getBody());
        eventListA.setId_temlate(adminDTO.getIdtemplate());
        eventListA.setCreator(adminDTO.getCreator());
        eventListA.setPatchToImage(adminDTO.getPatchToFile());
        return eventListA;
    }
}

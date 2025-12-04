package brickboy.Infrastructure.Mappers.v1;

import brickboy.Aplication.Domain.EntityDTO.AdminEventDTO.AdminEventDtoSave;
import brickboy.Infrastructure.Entity.v1.EventDataAdmin;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
public class EventListMapper {

    public static EventDataAdmin map(AdminEventDtoSave adminDTO) {
        EventDataAdmin eventDataAdmin = new EventDataAdmin();
        eventDataAdmin.setId(UUID.randomUUID());
        eventDataAdmin.setName(adminDTO.getName());
        eventDataAdmin.setHeader(adminDTO.getHeader());
        eventDataAdmin.setBudy(adminDTO.getBody());
        eventDataAdmin.setId_temlate(adminDTO.getIdtemplate());
        eventDataAdmin.setCreator(adminDTO.getCreator());
        eventDataAdmin.setPatchToImage(adminDTO.getPatchToFile());
        return eventDataAdmin;
    }
}

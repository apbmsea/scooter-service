package brickboy.Infrastructure.Mappers.v1;

import brickboy.Aplication.Domain.EntityDTO.AdminEventDTO.EventAdminDTO;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.patchDTO;
import brickboy.Infrastructure.Entity.v1.EventListA;


import java.util.UUID;

public class EventListMapper {

    public static EventListA toEventList(EventAdminDTO adminDTO, patchDTO patchDTO) {
        EventListA eventListA = new EventListA();
        eventListA.setId(UUID.randomUUID());
        eventListA.setTitle(adminDTO.getTitle());
        eventListA.setBudy(adminDTO.getBody());
        eventListA.setCreator(adminDTO.getCreator());
//        eventList.setDateCreated();
        eventListA.setPatchToImage(patchDTO.getPatch());
        return eventListA;
    }
}

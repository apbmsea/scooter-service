package brickboy.Infrastructure.Mappers.v1;

import brickboy.Aplication.Domain.EntityDTO.EventAdminDTO;
import brickboy.Aplication.Domain.EntityDTO.patchDTO;
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

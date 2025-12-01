package brickboy.Aplication.useCase.v1;

import brickboy.Aplication.Domain.EntityDTO.AdminEventDTO.AdminEventDtoSave;
import brickboy.Infrastructure.Entity.v1.EventListA;
import brickboy.Infrastructure.Mappers.v1.EventListMapper;
import brickboy.Infrastructure.Repository.v1.EventRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAndCreatedNewAdminEvent {
    @Autowired
    private EventListMapper eventListMapper;
    @Autowired
    private final EventRepoService eventRepoService;

    public GetAndCreatedNewAdminEvent(EventRepoService eventRepoService) {
        this.eventRepoService = eventRepoService;
    }

    public EventListA newAdminEvent(AdminEventDtoSave adminEventDtoSave) {
        // то тут должна быть проверка есть ли тут этот файл по пришедшему пути и есть ли он в базе

        EventListA eventListA = EventListMapper.map(adminEventDtoSave);

        // seve in LOG table

        return eventRepoService.save(eventListA);
    }


}


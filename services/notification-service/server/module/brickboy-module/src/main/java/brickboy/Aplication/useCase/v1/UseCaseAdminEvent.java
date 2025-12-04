package brickboy.Aplication.useCase.v1;

import brickboy.Aplication.Domain.EntityDTO.AdminEventDTO.AdminEventDtoSave;
import brickboy.Infrastructure.Entity.v1.EventDataAdmin;
import brickboy.Infrastructure.Entity.v1.Logi.EventDataLogi;
import brickboy.Infrastructure.Mappers.v1.EventLigi.EventDataLogiMapper;
import brickboy.Infrastructure.Mappers.v1.EventListMapper;
import brickboy.Infrastructure.Repository.Logi.EventDataLogiRepoService;
import brickboy.Infrastructure.Repository.v1.EventRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UseCaseAdminEvent {
    @Autowired
    private EventListMapper eventListMapper;
    @Autowired
    private EventRepoService eventRepoService;
    @Autowired
    private EventDataLogiRepoService eventDataLogiRepoService;
    @Autowired
    private EventDataLogiMapper eventDataLogiMapper;


    public EventDataAdmin newAdminEvent(AdminEventDtoSave adminEventDtoSave) {
        // то тут должна быть проверка есть ли тут этот файл по пришедшему пути и есть ли он в базе
        // но будет тупая проверка на не пустой ли путь до файла (да халтура :( )
        if (adminEventDtoSave.getPatchToFile() != "") {
            EventDataAdmin eventDataAdmin = eventListMapper.map(adminEventDtoSave);

            try {
                saveLog(eventDataAdmin);
                return eventRepoService.save(eventDataAdmin);
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        } else {
            adminEventDtoSave.setPatchToFile(null);
            EventDataAdmin eventDataAdmin = eventListMapper.map(adminEventDtoSave);

            try {

                saveLog(eventDataAdmin);
                return eventRepoService.save(eventDataAdmin);
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
        }

    }


    public Page<EventDataAdmin> findAllEvents(int page, int size) {
        return eventRepoService.findAll(page, size);
    }

    public Void dellAdminEvent(UUID eventId) {
        try {
            eventRepoService.delete(eventId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveLog(EventDataAdmin eventDataAdmin) {
        EventDataLogi eventDataLogi = eventDataLogiMapper.map(eventDataAdmin);

        try {
            eventDataLogiRepoService.SaveLogEvent(eventDataLogi);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


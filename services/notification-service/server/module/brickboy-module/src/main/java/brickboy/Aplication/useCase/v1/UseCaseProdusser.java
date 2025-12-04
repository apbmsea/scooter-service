package brickboy.Aplication.useCase.v1;


import brickboy.Aplication.Domain.EntityDTO.EventDTO.FrontStartEvent;
import brickboy.Infrastructure.Entity.v1.Logi.EventLogi;
import brickboy.Infrastructure.Mappers.v1.EventLigi.EventLogStartMapper;
import brickboy.Infrastructure.Repository.Logi.EventLogiRepoService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Service
public class UseCaseProdusser {
    private String producerJson(UUID templateId, UUID templateDataId, List<String> segments) {
        return "{"
                + "\"message_id\": \"" + UUID.randomUUID().toString() + "\", "
                + "\"timestamp\": \"" + Instant.now().toString() + "Z\", "
                + "\"type\": \"adds\", "
                + "\"data\": {"
                + "\"template_id_event_data\": \"" + templateDataId.toString() + "\", "
                + "\"template_id\": \"" + templateId.toString() + "\", "
                + "\"segments\": [" + String.join(",", segments) + "]"
                + "}"
                + "}";
    }


public void produserPublic(FrontStartEvent dto)
{
//    пишем в логи в базы даных
saveLog(dto);

   String json = producerJson(dto.getTemplate(), dto.getTemplateData(), dto.getSegments());

//    здесь происходит отправка и публикация через и на другом сервисе
}
private EventLogiRepoService eventLogiRepoService;
private EventLogStartMapper logStartMapper;
private void saveLog(FrontStartEvent dto){
      EventLogi logi =  logStartMapper.map(dto);
       try {
           eventLogiRepoService.save(logi);
       } catch (Exception e) {
           e.printStackTrace();
       }

}
}

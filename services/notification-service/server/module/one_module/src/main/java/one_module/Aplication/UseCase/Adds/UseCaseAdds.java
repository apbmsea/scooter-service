package one_module.Aplication.UseCase.Adds;

import one_module.Aplication.DTO.Adds.AdditionalData;
import one_module.Aplication.DTO.Adds.DataDTORebbitINeventAdds;
import one_module.Aplication.DTO.Adds.Segments;
import one_module.Aplication.DTO.addsBufer.BatchDataDTOadds;
import one_module.Infrastructure.Service.v1.Redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UseCaseAdds {
    @Autowired
    private RedisService redisService;

    public void adds(BatchDataDTOadds dtoadds) {
        List<DataDTORebbitINeventAdds> events = dtoadds.getEventAdds();
        for (var event : events) {
            AdditionalData data = event.getData();
            UUID id_mass = event.getMassageRebbitID();
            //взять id  getMassageRebbitID() и использовать их в запросе данных сегмента затем записать 

            if (data != null) {
                UUID idData = data.getId_add_data();
                UUID idTemplate = data.getId_add_template();
                List<Segments> segmentsList = data.getSegmentList();

                processSegments(id_mass, segmentsList);

                String datales = "{\"template_id_event_data\":" + idData + "\", \"template_id\":" + idTemplate + "\"" + "}";
                redisService.setValue(id_mass, datales);
                redisService.setTTloneDay(id_mass);
            }
        }
    }

    private void processSegments(UUID id_Uuid, List<Segments> segmentsList) {
        int count = segmentsList.size();
        if (count != 0) {
            String name = segmentsList.get(0).getName();
            sendByRestApiOnUserServiceSegment(id_Uuid, name);
        }
    }


    private void sendByRestApiOnUserServiceSegment(UUID for_id, String segment) {
// вроде там нужен токен (спроси)
// передать то для какого id нужны эти данные
    }


}
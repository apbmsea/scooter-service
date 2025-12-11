package one_module.Aplication.UseCase.Users;

import one_module.Aplication.DTO.BufferUsers.BatchDataDTOusers;
import one_module.Aplication.DTO.DtoTempldata;
import one_module.Aplication.DTO.Users.AdditionalUsersData;
import one_module.Aplication.DTO.Users.DataDTORebbitINusersData;
import one_module.Infrastructure.Service.v1.Redis.RedisService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UseCaseUsers {

    @Autowired
    private RedisService redisService;

    @Autowired
    private AmqpTemplate amqpTemplate;


    public String users(BatchDataDTOusers dataDTOusers) {
        List<DataDTORebbitINusersData> users = dataDTOusers.getUsers();
        for (var user : users) {
            List<AdditionalUsersData> data = user.getDatas();
            //сравнить id поля forPacage с id сообщения админа

            //для связи чтобы не перепутать надобы взять ID рекламного эвента и сравнивать с записью
            for (var userData : data) {
                String email = userData.getEmail();
                String phoneNumber = userData.getPhoneNumber();
                if (phoneNumber == null) {
                    phoneNumber = "noPhoneNumber";
                }
                sendData(user.getMassageID(), email, phoneNumber);
            }
        }
        return "Processed Successfully";
    }

    public void sendData(UUID id, String email, String phoneNumber) {
        String jsonTemplate = redisService.getValueUUID(id).toString();
        if (jsonTemplate == null) {
            // ЛООООООООги
        }
        DtoTempldata dto = new DtoTempldata();
        dto.setEmail(email);
        dto.setPhone(phoneNumber);
        dto.setJsonTempldata(jsonTemplate);
        String stringSend = jsonData(dto);
        sendByRestApiInModule(stringSend);
    }

    private String jsonData(DtoTempldata dto) {
        return "{" + "\"message_id\": \"" + UUID.randomUUID().toString() + "\", "
                + "\"timestamp\": \"" + Instant.now().toString() + "Z\", "
                + "\"type\": \"adds\", "
                + "\"data\": {"
                + "\"email\": \"" + dto.getEmail().toString() + "\", "
                + "\"phone\": \"" + dto.getPhone().toString() + "\", "
                + "\"To\":[" + dto.getJsonTempldata() + "]" + "\", "
                + "}"
                + "}";
    }

    private void sendByRestApiInModule(String message) {
        String routingKey = "XXXXXX";
        amqpTemplate.convertAndSend(routingKey, message);
    }

}
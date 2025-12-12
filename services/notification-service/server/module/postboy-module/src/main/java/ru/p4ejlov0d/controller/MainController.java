package ru.p4ejlov0d.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.p4ejlov0d.dto.NotficationDto;
import ru.p4ejlov0d.service.MailService;

/**
 * <p>Главный API контроллер</p>
 *
 * @author p4eJlov0d
 */
@RestController
@RequiredArgsConstructor
public class MainController {
    private final MailService mailService;

    /**
     * <p>Эндпоинт для отправки уведомлений на почту</p>
     *
     * @param notficationDto объект, который создается из переданного json
     */
    @PostMapping("/api/mail/send")
    public void sendMail(@RequestBody NotficationDto notficationDto) {
        mailService.send(notficationDto);
    }
}

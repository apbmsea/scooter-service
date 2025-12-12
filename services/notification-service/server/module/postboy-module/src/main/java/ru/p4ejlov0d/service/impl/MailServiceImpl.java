package ru.p4ejlov0d.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.p4ejlov0d.dto.NotficationDto;
import ru.p4ejlov0d.dto.Type;
import ru.p4ejlov0d.service.MailService;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Класс отвечает за отправку писем до пользователя</p>
 * <p>Является имплементацией интерфейса {@link MailService}</p>
 *
 * @author p4eJlov0d
 * @see MailService
 */
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final Map<Type, String> typeMail = new HashMap<>();

    @Value("${spring.mail.username}")
    private String from;

    /**
     * <p>Метод отправляет письмо пользователю</p>
     *
     * @param notficationDto объект, передающий данные: кому отправить, тема письма и содержание письма
     */
    @Override
    public void send(NotficationDto notficationDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String body = notficationDto.type().name().toLowerCase().replace("_", " ");

        mailMessage.setTo(notficationDto.to());
        mailMessage.setSubject(notficationDto.subject());
        mailMessage.setText(body);
        mailMessage.setFrom(from);

        mailSender.send(mailMessage);
    }
}

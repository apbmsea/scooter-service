package ru.p4ejlov0d.service;

import ru.p4ejlov0d.dto.NotficationDto;

/**
 * <p>Интерфейс для отправки писем</p>
 *
 * @author p4eJlov0d
 * @see ru.p4ejlov0d.service.impl.MailServiceImpl
 */
public interface MailService {
    /**
     * <p>Метод должен отправлять письмо адресату</p>
     *
     * @param notficationDto объект, передающий данные: кому отправить, тема письма и содержание письма
     */
    void send(NotficationDto notficationDto);
}

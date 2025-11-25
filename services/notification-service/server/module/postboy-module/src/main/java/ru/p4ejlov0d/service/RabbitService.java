package ru.p4ejlov0d.service;

/**
 * Интерфейс сервиса для работы с <b>RabbitMQ</b> очередью
 *
 * @author p4eJlov0d
 * @see ru.p4ejlov0d.service.impl.RabbitServiceImpl
 */
public interface RabbitService {
    /**
     * Метод должен обрабатывать получаемое сообщение из очереди
     *
     * @param message сообщение, полученное из очереди
     */
    void receive(String message);
}

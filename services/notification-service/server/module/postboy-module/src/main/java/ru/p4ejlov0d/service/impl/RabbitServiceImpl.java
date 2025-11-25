package ru.p4ejlov0d.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.p4ejlov0d.service.RabbitService;

/**
 * Имплементация интерфейса {@code RabbitService} для работы с <b>RabbitMQ</b> очередью
 *
 * <p>
 * Аннотация {@code @EnableRabbit} и {@code @RabbitListener}
 * в {@link RabbitServiceImpl#receive(String)} позволяют прослушивать очередь из <b>RabbitMQ</b>
 * </p>
 *
 * @author p4eJlov0d
 * @see RabbitService
 */
@Slf4j
@EnableRabbit
@Service
public class RabbitServiceImpl implements RabbitService {
    /**
     * <p>Метод получает сообщение из очереди и обрабатывает его</p>
     *
     * @param message сообщение, полученное из очереди
     */
    @RabbitListener(queues = {"test"})
    @Override
    public void receive(String message) {
        log.info(message);
    }
}

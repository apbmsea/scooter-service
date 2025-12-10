package test.weewee.userservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация RabbitMQ для User-Service
 */
@Configuration
public class RabbitMQConfig {

    // ========== Exchanges ==========
    
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange("user.exchange");
    }

    // ========== Queues ==========
    
    @Bean
    public Queue userCreatedQueue() {
        return QueueBuilder.durable("user.created.queue").build();
    }

    @Bean
    public Queue userUpdatedQueue() {
        return QueueBuilder.durable("user.updated.queue").build();
    }

    @Bean
    public Queue userDeletedQueue() {
        return QueueBuilder.durable("user.deleted.queue").build();
    }

    // ========== Bindings ==========
    
    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(userExchange())
                .with("user.created");
    }

    @Bean
    public Binding userUpdatedBinding() {
        return BindingBuilder
                .bind(userUpdatedQueue())
                .to(userExchange())
                .with("user.updated");
    }

    @Bean
    public Binding userDeletedBinding() {
        return BindingBuilder
                .bind(userDeletedQueue())
                .to(userExchange())
                .with("user.deleted");
    }

    // ========== Message Converter ==========
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}


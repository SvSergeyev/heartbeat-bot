package tech.sergeyev.heartbeatbot.config.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitMqConfig {
    public static final String CORE_QUEUE_NAME = "core_status_queue";
    private final RabbitMqParameters rabbitMqParameters;

    @Bean
    public Queue queue() {
        return new Queue(CORE_QUEUE_NAME);
    }

    @Bean
    public ConnectionFactory rabbitMqConnectionFactory() {
        var connectionFactory = new CachingConnectionFactory();
        connectionFactory.setVirtualHost(rabbitMqParameters.getVirtualHost());
        connectionFactory.setHost(rabbitMqParameters.getHost());
        connectionFactory.setUsername(rabbitMqParameters.getUsername());
        connectionFactory.setPassword(rabbitMqParameters.getPassword());
        return connectionFactory;
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setDefaultReceiveQueue(CORE_QUEUE_NAME);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setReplyAddress(queue().getName());
        rabbitTemplate.setReplyTimeout(rabbitMqParameters.getReplyTimeout());
        rabbitTemplate.setUseDirectReplyToContainer(Boolean.FALSE);
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(rabbitMqConnectionFactory());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitMqConnectionFactory());
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}

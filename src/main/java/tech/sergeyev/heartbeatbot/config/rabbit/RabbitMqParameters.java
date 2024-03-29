package tech.sergeyev.heartbeatbot.config.rabbit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rabbitmq")
@Getter
@Setter
public class RabbitMqParameters {
//    @Value("${rabbitmq.username}")
    private String username;
//    @Value("${rabbitmq.password}")
    private String password;
//    @Value("${rabbitmq.host}")
    private String host;
//    @Value("${rabbitmq.virtualhost}")
    private String virtualHost;
//    @Value("${rabbitmq.replyTimeout}")
    private long replyTimeout;
}

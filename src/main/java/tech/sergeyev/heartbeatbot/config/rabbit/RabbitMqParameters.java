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
    private String username;
    private String password;
    private String host;
    private String virtualHost;
    private long replyTimeout;
}

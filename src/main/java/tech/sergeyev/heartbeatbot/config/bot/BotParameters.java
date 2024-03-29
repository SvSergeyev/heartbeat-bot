package tech.sergeyev.heartbeatbot.config.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bot")
@Getter
@Setter
public class BotParameters {
    private String name;
    private String token;
}

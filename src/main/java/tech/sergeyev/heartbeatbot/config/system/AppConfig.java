package tech.sergeyev.heartbeatbot.config.system;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import tech.sergeyev.heartbeatbot.config.bot.BotParameters;
import tech.sergeyev.heartbeatbot.service.bot.BotService;
import tech.sergeyev.heartbeatbot.service.subscription.SubscriptionRestoreService;
import tech.sergeyev.heartbeatbot.service.update.UpdatesFacade;

import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final BotParameters botParameters;

    public @Bean MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        return messageSource;
    }

    public @Bean BotService botService(UpdatesFacade facade,
                                       SubscriptionRestoreService subscriptionRestoreService) {
        return new BotService(
                botParameters.getName(),
                botParameters.getToken(),
                facade,
                subscriptionRestoreService);
    }
}
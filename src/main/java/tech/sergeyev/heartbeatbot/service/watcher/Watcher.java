package tech.sergeyev.heartbeatbot.service.watcher;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import tech.sergeyev.heartbeatbot.dto.DevStageStatus;
import tech.sergeyev.heartbeatbot.exception.SubscriptionException;
import tech.sergeyev.heartbeatbot.service.util.Messages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import static tech.sergeyev.heartbeatbot.config.rabbit.RabbitMqConfig.CORE_QUEUE_NAME;

@Slf4j
public class Watcher implements Runnable {
    private static final String URL_TEMPLATE = "http://%s/rest/version";
    private static final int IS_ACTIVE_RESPONSE_CODE = 200;
    private static final String REQUEST_METHOD = "GET";
    private boolean isActive = Boolean.TRUE;

    @Getter
    @Setter
    private ScheduledFuture scheduledTask;

    @Getter
    private final String url;

    @Getter
    private final Set<Long> subscribers;

    private final AmqpTemplate rabbitTemplate;

    public Watcher(String url, AmqpTemplate rabbitTemplate) {
        this.url = url;
        this.rabbitTemplate = rabbitTemplate;
        this.subscribers = new HashSet<>();
    }

    @Override
    public void run() {
        try {
            var connection = (HttpURLConnection) new URL(getFormattedUrl()).openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            updateStateIfRequired(connection.getResponseCode());
        } catch (IOException e) {
            log.error("Cannot connect to {}", url);
            log.error("Error: ", e);
        }
    }

    private String getFormattedUrl() {
        return String.format(URL_TEMPLATE, url);
    }

    private void updateStateIfRequired(int responseCode) {
        var currentState = IS_ACTIVE_RESPONSE_CODE == responseCode;
        if (isActive != currentState) {
            isActive = currentState;
            rabbitTemplate.convertAndSend(CORE_QUEUE_NAME, new DevStageStatus(currentState, url));
        }
    }

    public void addSubscriber(long chatId) throws SubscriptionException {
        if (!subscribers.add(chatId)) {
            throw new SubscriptionException(Messages.SUBSCRIPTION_ALREADY_EXISTS);
        }
    }

    public void removeSubscriber(long chatId) {
        subscribers.remove(chatId);
    }

}

package tech.sergeyev.heartbeatbot.service.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.dto.DevStageStatus;
import tech.sergeyev.heartbeatbot.dto.ReleaseInfo;
import tech.sergeyev.heartbeatbot.exception.ReleaseInfoParsingException;
import tech.sergeyev.heartbeatbot.model.subscription.Subscription;
import tech.sergeyev.heartbeatbot.service.bot.BotService;
import tech.sergeyev.heartbeatbot.service.subscription.SubscriptionManager;
import tech.sergeyev.heartbeatbot.service.util.*;

import java.util.stream.Collectors;

import static tech.sergeyev.heartbeatbot.config.rabbit.RabbitMqConfig.CORE_QUEUE_NAME;

@Service
@RabbitListener(queues = CORE_QUEUE_NAME)
@RequiredArgsConstructor
@Slf4j
public class RabbitMqUpdatesListener {
    private static final Services SERVICE_NAME = Services.CORE;
    private static final String ERROR_STUB = "UNKNOWN";

    private final Messages messageService;
    private final BotService bot;
    private final SubscriptionManager subscriptionManager;

    @RabbitHandler
    public void processCoreQueue(DevStageStatus message) throws TelegramApiException {
        var chatIds = subscriptionManager.findAllByUrl(message.getUrl()).stream()
                .map(Subscription::getChatId)
                .collect(Collectors.toList());
        for (var chatId : chatIds) {
            var reply = new SendMessage();
            reply.setText(getFormattedMessage(message.getUrl(), message.isActive()));
            if (!message.isActive()) {
                reply.setReplyMarkup(ReplyMarkupCreator.getLogRequestButton(
                        message.getUrl(), SERVICE_NAME));
            } else {
                reply.setReplyMarkup(ReplyMarkupCreator.getReleaseInfoButton(message.getUrl()));
            }
            reply.setChatId(chatId);
            bot.execute(reply);
        }
    }

    // todo to common class/interface ?
    private String getFormattedMessage(String url, boolean isActive) {
        if (isActive) {
            var mainMessage = messageService.getMessage("state_changed_template",
                    Emojis.WHITE_CHECK_MARK, SERVICE_NAME.getNameInReleaseInfo(), url, Messages.STARTED);
            var additionalMessage = messageService.getMessage("request_release_info");
            ReleaseInfo releaseInfo;
            try {
                releaseInfo = ReleaseInfoCollector.collectOne(url, Services.CORE);
            } catch (ReleaseInfoParsingException e) {
                log.error(e.getMessage());
                releaseInfo = new ReleaseInfo(SERVICE_NAME, ERROR_STUB, ERROR_STUB, ERROR_STUB);
            }
            var infoMessage = messageService.getMessage("release_info_template",
                    releaseInfo.getBranch(), releaseInfo.getCommit(), releaseInfo.getTime());
            return mainMessage + "\n\n" + infoMessage + "\n\n" + additionalMessage;
        }
        var mainMessage = messageService.getMessage("state_changed_template",
                Emojis.RED_X, SERVICE_NAME.getNameInReleaseInfo(), url, Messages.STOPPED);
        var additionalMessage = messageService.getMessage("request_log");
        return mainMessage + "\n\n" + additionalMessage;
    }
}

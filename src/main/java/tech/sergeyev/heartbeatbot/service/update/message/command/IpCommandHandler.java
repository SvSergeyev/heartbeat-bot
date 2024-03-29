package tech.sergeyev.heartbeatbot.service.update.message.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.sergeyev.heartbeatbot.exception.SubscriptionException;
import tech.sergeyev.heartbeatbot.service.subscription.SubscriptionManager;
import tech.sergeyev.heartbeatbot.service.update.message.TextCommandHandler;
import tech.sergeyev.heartbeatbot.service.util.Messages;

import java.util.Arrays;

import static tech.sergeyev.heartbeatbot.service.bot.BotService.ADMIN_CHAT_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpCommandHandler implements TextCommandHandler {
    private final SubscriptionManager subscriptionManager;
    private final Messages messageService;

    @Override
    public SendMessage handle(Message message) {
        var chatId = message.getChatId();
        var url = message.getText();
        try {
            if (subscriptionManager.existsByChatIdAndUrl(chatId, url)) {
                subscriptionManager.unsubscribe(chatId, message.getText());
                return new SendMessage(
                        String.valueOf(chatId),
                        messageService.getMessage(Messages.SUBSCRIPTION_REMOVED, message.getText()));
            }
            subscriptionManager.subscribe(chatId, url);
            return new SendMessage(
                    String.valueOf(chatId),
                    messageService.getMessage(Messages.SUBSCRIPTION_CREATED, message.getText()));
        } catch (Exception e) {
            log.error("An error occurred: ", e);
            return new SendMessage(String.valueOf(chatId), "An error occurred: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "ip";
    }
}

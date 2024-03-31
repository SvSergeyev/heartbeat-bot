package tech.sergeyev.heartbeatbot.service.update.message.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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
    public void handle(Message message, AbsSender sender) throws TelegramApiException {
        var chatId = message.getChatId();
        var url = message.getText();
        var reply = new SendMessage();
        reply.setChatId(String.valueOf(chatId));
        try {
            if (subscriptionManager.existsByChatIdAndUrl(chatId, url)) {
                subscriptionManager.unsubscribe(chatId, message.getText());
                reply.setText(messageService.getMessage(Messages.SUBSCRIPTION_REMOVED, message.getText()));
            } else {
                subscriptionManager.subscribe(chatId, url);
                reply.setText(messageService.getMessage(Messages.SUBSCRIPTION_CREATED, message.getText()));
            }
            sender.execute(reply);
        } catch (Exception e) {
            log.error("Internal error when processing ip", e);
            log.error("Error: ", e);
        }
    }

    @Override
    public String getName() {
        return "ip";
    }
}

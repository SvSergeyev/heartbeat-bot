package tech.sergeyev.heartbeatbot.service.update.callback.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.exception.SubscriptionException;
import tech.sergeyev.heartbeatbot.service.subscription.SubscriptionManager;
import tech.sergeyev.heartbeatbot.service.update.callback.CallbackType;
import tech.sergeyev.heartbeatbot.service.util.IpResolver;
import tech.sergeyev.heartbeatbot.service.util.Messages;

import static tech.sergeyev.heartbeatbot.service.update.callback.CallbackHandler.CALLBACK_DATA_SEPARATOR;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionCallbackProcessor implements CallbackTypeProcessor {
    private final SubscriptionManager subscriptionManager;
    private final Messages messageService;
    private final AbsSender sender;

    @Override
    public void process(CallbackQuery query) throws TelegramApiException {
        var chatId = query.getMessage().getChatId();
        var url = query.getData().split(CALLBACK_DATA_SEPARATOR)[1];
        var reply = new SendMessage();
        reply.setChatId(chatId.toString());
        if (!IpResolver.isIpAddress(url)) {
            reply.setText(messageService.getMessage(
                    Messages.INTERNAL_ERROR,
                    Messages.INVALID_CALLBACK_DATA));
            sender.execute(reply);
        }
        try {
            subscriptionManager.unsubscribe(chatId, url);
            reply.setText(messageService.getMessage(Messages.SUBSCRIPTION_REMOVED, url));
        } catch (SubscriptionException e) {
            log.error("An error occurred: ", e);
            reply.setText("An error occurred: " + e.getMessage());
        }
        sender.execute(reply);
    }

    @Override
    public CallbackType getType() {
        return CallbackType.UNSUBSCRIBE;
    }
}

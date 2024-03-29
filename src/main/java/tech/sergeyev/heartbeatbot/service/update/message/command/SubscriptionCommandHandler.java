package tech.sergeyev.heartbeatbot.service.update.message.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.sergeyev.heartbeatbot.service.subscription.SubscriptionManager;
import tech.sergeyev.heartbeatbot.service.update.message.TextCommandHandler;
import tech.sergeyev.heartbeatbot.service.util.Messages;
import tech.sergeyev.heartbeatbot.service.util.ReplyMarkupCreator;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionCommandHandler implements TextCommandHandler {
    private final SubscriptionManager subscriptionManager;

    @Override
    public SendMessage handle(Message message) {
        var chatId = message.getChatId();
        var subscriptions = subscriptionManager.getAllUserSubscriptions(chatId);
        var reply = new SendMessage();
        reply.setChatId(chatId.toString());
        if (subscriptions.isEmpty()) {
            reply.setText(Messages.NO_ACTIVE_SUBSCRIPTIONS);
            return reply;
        }
        reply.setText(Messages.ALL_SUBSCRIPTIONS);
        reply.setReplyMarkup(ReplyMarkupCreator.convertSubscriptionsToMarkupKeyboard(subscriptions));
        return reply;
    }

    @Override
    public String getName() {
        return "/subs";
    }

}

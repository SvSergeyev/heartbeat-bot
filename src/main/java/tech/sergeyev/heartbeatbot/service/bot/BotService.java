package tech.sergeyev.heartbeatbot.service.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.sergeyev.heartbeatbot.exception.SubscriptionException;
import tech.sergeyev.heartbeatbot.service.subscription.SubscriptionRestoreService;
import tech.sergeyev.heartbeatbot.service.update.UpdatesFacade;

@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class BotService extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;
    private final UpdatesFacade updatesFacade;
    private final SubscriptionRestoreService subscriptionRestoreService;

    public static final int ADMIN_CHAT_ID = 406384276;

    @Override
    public void onUpdateReceived(Update update) {
        updatesFacade.handle(update, this);
    }

    @Override
    public void onRegister() {
        try {
            subscriptionRestoreService.restoreSubscriptions();
            super.onRegister();
            notifyAdmin();
        } catch (SubscriptionException e) {
            log.error("An error occurred on register: ", e);
        }
    }

    private void notifyAdmin() {
        var reply = new SendMessage();
        reply.setText("Bot started");
        reply.setChatId(String.valueOf(ADMIN_CHAT_ID));
    }
}

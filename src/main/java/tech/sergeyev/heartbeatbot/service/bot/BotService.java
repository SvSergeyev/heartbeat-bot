package tech.sergeyev.heartbeatbot.service.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaBotMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.exception.SubscriptionException;
import tech.sergeyev.heartbeatbot.service.subscription.SubscriptionRestoreService;
import tech.sergeyev.heartbeatbot.service.update.common.UpdatesFacade;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
    private static final BlockingQueue<BotApiMethod<Message>> MESSAGE_QUEUE = new LinkedBlockingQueue<>();
    private static final BlockingQueue<SendMediaBotMethod<?>> MEDIA_QUEUE = new LinkedBlockingQueue<>();

//    public BotService(String botToken,
//                      String botUsername,
//                      UpdatesFacade updatesFacade,
//                      SubscriptionRestoreService subscriptionRestoreService) {
//        this.botUsername = botUsername;
//        this.botToken = botToken;
//        this.updatesFacade = updatesFacade;
//        this.subscriptionRestoreService = subscriptionRestoreService;
//    }

    @Override
    public void onUpdateReceived(Update update) {
        updatesFacade.handle(update);
//        if (reply instanceof BotApiMethod) {
//
//        }
//        if (reply instanceof SendMediaBotMethod) {
//
//        }
//        offerToQueue(updatesFacade.handle(update));
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onRegister() {
        try {
            subscriptionRestoreService.restoreSubscriptions();
            super.onRegister();
        } catch (SubscriptionException e) {
            log.error("An error occurred on register: ", e);
        }
    }

//    public void offerToQueue(BotApiMethod<Message> reply) {
//        MESSAGE_QUEUE.offer(reply);
//    }

//    @Scheduled(fixedRate = 500)
//    private void send() throws InterruptedException {
//        var reply = MESSAGE_QUEUE.take();
//        try {
//            execute(reply);
//        } catch (TelegramApiException e) {
//            log.error("Cannot send message, message will be return to queue");
//            backObjectToQueue(reply);
//        }
//    }

//    private void backObjectToQueue(BotApiMethod<Message> object) {
//        try (var scheduler = Executors.newSingleThreadScheduledExecutor()) {
//            scheduler.schedule(() -> MESSAGE_QUEUE.offer(object), 1000, TimeUnit.MILLISECONDS);
//        }
//    }
}

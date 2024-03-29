package tech.sergeyev.heartbeatbot.service.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.sergeyev.heartbeatbot.exception.SubscriptionException;
import tech.sergeyev.heartbeatbot.service.watcher.WatcherManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionRestoreService {
    private final SubscriptionManager subscriptionManager;
    private final WatcherManager watcherManager;

    public void restoreSubscriptions() throws SubscriptionException {
        var subscriptions = subscriptionManager.findAll();
        log.info("Restored subscriptions={}", subscriptions);
        for (var subscription : subscriptions) {
            var url = subscription.getUrl();
            var chatId = subscription.getChatId();
            if (watcherManager.getWatcherForUrl(url) == null) {
                watcherManager.create(url, chatId);
            } else {
                watcherManager.update(url, chatId);
            }
        }
        log.info("Active watchers for urls:{}", watcherManager.getAllActiveWatcherUrls());
    }
}

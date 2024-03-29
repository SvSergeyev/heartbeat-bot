package tech.sergeyev.heartbeatbot.service.watcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import tech.sergeyev.heartbeatbot.config.scheduled.WatcherTaskScheduler;
import tech.sergeyev.heartbeatbot.exception.SubscriptionException;
import tech.sergeyev.heartbeatbot.service.util.Messages;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatcherManager {
    private final Map<String, Watcher> activeWatchers = new ConcurrentHashMap<>();
    private final AmqpTemplate rabbitTemplate;
    private final WatcherTaskScheduler watcherTaskScheduler;

    public void create(String url, Long chatId) throws SubscriptionException {
        if (activeWatchers.containsKey(url)) {
            throw new SubscriptionException(Messages.SUBSCRIPTION_ALREADY_EXISTS);
        }
        var watcher = new Watcher(url, rabbitTemplate);
        watcher.addSubscriber(chatId);
        watcherTaskScheduler.scheduleAtFixedRate(watcher);
        activeWatchers.put(url, watcher);
    }

    public Watcher getForUrl(@NonNull String url) {
        return activeWatchers.get(url);
    }

    public void update(String url, Long chatId) throws SubscriptionException {
        var watcher = activeWatchers.get(url);
        watcher.addSubscriber(chatId);
    }

    public void delete(String url) throws SubscriptionException {
        if (!activeWatchers.containsKey(url)) {
            log.error("There is no active watchers for {}", url);
            throw new SubscriptionException(Messages.INTERNAL_ERROR);
        }
        var watcher = activeWatchers.get(url);
        if (!watcher.getSubscribers().isEmpty()) {
            throw new SubscriptionException("Cannot delete watcher that has more than one subscriber");
        }
        watcherTaskScheduler.cancelScheduledTask(watcher);
        activeWatchers.remove(url);
    }

    public Watcher getWatcherForUrl(String url) {
        return activeWatchers.get(url);
    }

    public Set<String> getAllActiveWatcherUrls() {
        return activeWatchers.keySet();
    }
}

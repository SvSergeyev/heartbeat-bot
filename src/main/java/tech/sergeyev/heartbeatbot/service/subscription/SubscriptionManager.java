package tech.sergeyev.heartbeatbot.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.sergeyev.heartbeatbot.exception.SubscriptionException;
import tech.sergeyev.heartbeatbot.model.subscription.Subscription;
import tech.sergeyev.heartbeatbot.service.watcher.WatcherManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionManager {
    private final SubscriptionCRUDManager subscriptionCRUDManager;
    private final WatcherManager watcherManager;

    @Transactional
    public void subscribe(Long chatId, String url) throws SubscriptionException {
        createSubscription(chatId, url);
    }

    @Transactional
    public void unsubscribe(Long chatId, String url) throws SubscriptionException {
        var subscription = subscriptionCRUDManager.findByChatIdAndUrl(chatId, url);
        deleteSubscription(subscription);
    }


    private void createSubscription(Long chatId, String url) throws SubscriptionException {
        var subscription = new Subscription();
        subscription.setUrl(url);
        subscription.setChatId(chatId);
        var watcher = watcherManager.getForUrl(url);
        if (watcher == null) {
            watcherManager.create(url, chatId);
        } else {
            watcherManager.update(url, chatId);
        }
        subscriptionCRUDManager.save(subscription);
    }

    private void deleteSubscription(Subscription subscription) throws SubscriptionException {
        var watcher = watcherManager.getForUrl(subscription.getUrl());
        watcher.removeSubscriber(subscription.getChatId());
        if (watcher.getSubscribers().isEmpty()) {
            watcherManager.delete(subscription.getUrl());
        }
        subscriptionCRUDManager.delete(subscription);
    }

    public List<Subscription> getAllUserSubscriptions(Long chatId) {
        return subscriptionCRUDManager.findAllByChatId(chatId);
    }

    public boolean existsByChatIdAndUrl(Long chatId, String url) {
        return subscriptionCRUDManager.existsByChatIdAndUrl(chatId, url);
    }

    public List<Subscription> findAllByUrl(String url) {
        return subscriptionCRUDManager.findAllByUrl(url);
    }

    public List<Subscription> findAll() {
        return subscriptionCRUDManager.findAll();
    }

    public void deleteAll() {
        subscriptionCRUDManager.deleteAll();
    }
}

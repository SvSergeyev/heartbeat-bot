package tech.sergeyev.heartbeatbot.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.sergeyev.heartbeatbot.dao.SubscriptionRepository;
import tech.sergeyev.heartbeatbot.model.subscription.Subscription;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionCRUDManager {
    private final SubscriptionRepository subscriptionRepository;

    public void save(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    public void delete(Subscription subscription) {
        subscriptionRepository.delete(subscription);
    }

    public Subscription findByChatIdAndUrl(long chatId, String url) {
        return subscriptionRepository.findByChatIdAndUrl(chatId, url).orElseThrow();
    }

    public List<Subscription> findAllByChatId(long chatId) {
        return subscriptionRepository.findAllByChatId(chatId);
    }

    public boolean existsByChatIdAndUrl(Long chatId, String url) {
        return subscriptionRepository.existsByChatIdAndUrl(chatId, url);
    }

    public List<Subscription> findAllByUrl(String url) {
        return subscriptionRepository.findAllByUrl(url);
    }

    public void deleteAll() {
        subscriptionRepository.deleteAll();
    }

    public List<Subscription> findAll() {
        var result = new ArrayList<Subscription>();
        var fromDb = subscriptionRepository.findAll();
        fromDb.forEach(result::add);
        return result;
    }
}

package tech.sergeyev.heartbeatbot.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tech.sergeyev.heartbeatbot.model.subscription.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, String> {
    Optional<Subscription> findByChatIdAndUrl(long chatId, String url);
    List<Subscription> findAllByChatId(long chatId);
    boolean existsByChatIdAndUrl(Long chatId, String url);
    List<Subscription> findAllByUrl(String url);
}

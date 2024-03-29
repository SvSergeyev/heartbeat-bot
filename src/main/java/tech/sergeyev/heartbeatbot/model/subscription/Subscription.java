package tech.sergeyev.heartbeatbot.model.subscription;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("subscriptions")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Subscription {
    @Id
    private String id;

    @Indexed
    @NonNull
    private Long chatId;

    @Indexed
    @NonNull
    private String url;
}

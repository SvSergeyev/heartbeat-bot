package tech.sergeyev.heartbeatbot.config.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import tech.sergeyev.heartbeatbot.service.watcher.Watcher;

@Slf4j
public class WatcherTaskScheduler extends ThreadPoolTaskScheduler {
    @Value("${scheduler.fixed-rate}")
    private final int fixedRateMs = 3000;


    public void scheduleAtFixedRate(Watcher task) {
        log.info("New task started: {}", task.getUrl());
        var future = super.scheduleAtFixedRate(task, fixedRateMs);
        task.setScheduledTask(future);
    }

    public void cancelScheduledTask(Watcher watcher) {
        var future = watcher.getScheduledTask();
        if (future != null) {
            future.cancel(true);
            log.info("Task for {} is cancelled ({})", watcher.getUrl(), future.isCancelled());
        }
    }
}

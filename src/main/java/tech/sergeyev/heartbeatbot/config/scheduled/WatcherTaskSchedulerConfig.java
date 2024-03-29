package tech.sergeyev.heartbeatbot.config.scheduled;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WatcherTaskSchedulerConfig {
    @Bean(destroyMethod = "shutdown")
    public WatcherTaskScheduler watcherTaskScheduler() {
        var scheduler = new WatcherTaskScheduler();
        scheduler.setPoolSize(100);
        scheduler.initialize();
        return scheduler;
    }
}

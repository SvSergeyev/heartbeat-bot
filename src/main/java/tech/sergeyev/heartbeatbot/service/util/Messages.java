package tech.sergeyev.heartbeatbot.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class Messages {
    private final Locale locale;
    private final MessageSource messageSource;

    public Messages(@Value("${localeTag}") String localeTag,
                    MessageSource messageSource) {
        this.messageSource = messageSource;
        this.locale = Locale.forLanguageTag(localeTag);
    }

    public static final String TEXT_IP = "Text IP for subscription:";
    public static final String UNKNOWN_COMMAND = "Unknown command";
    public static final String NO_ACTIVE_SUBSCRIPTIONS = "No active subscriptions";
    public static final String ALL_SUBSCRIPTIONS = "Active subscriptions";
    public static final String SUBSCRIPTION_ALREADY_EXISTS = "You are already subscribed to {0}";
    public static final String INTERNAL_ERROR = "internal_error";
    public static final String NO_ACTIVE_WATCHERS = "There are no active watchers for {0}";
    public static final String INVALID_CALLBACK_DATA = "Invalid callback data";
    public static final String STARTED = "started";
    public static final String STOPPED = "stopped";
    public static final String SUBSCRIPTION_REMOVED = "subscription_removed";
    public static final String SUBSCRIPTION_CREATED = "subscription_created";
    public static final String RELEASE_INFO_TEMPLATE = "release_info_template";

    public String getMessage(String message, Object... args) {
        return messageSource.getMessage(message, args, locale);
    }

}

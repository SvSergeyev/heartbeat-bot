package tech.sergeyev.heartbeatbot.service.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import tech.sergeyev.heartbeatbot.model.subscription.Subscription;
import tech.sergeyev.heartbeatbot.service.update.callback.CallbackType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static tech.sergeyev.heartbeatbot.service.update.callback.CallbackHandler.CALLBACK_DATA_SEPARATOR;

@UtilityClass
public class ReplyMarkupCreator {

    public InlineKeyboardMarkup convertSubscriptionsToMarkupKeyboard(List<Subscription> subscriptions) {
        var markup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        for (var subscription : subscriptions) {
            var row = Collections.singletonList(InlineKeyboardButton.builder()
                    .text(subscription.getUrl())
                    .callbackData(getFormattedCallbackData(CallbackType.UNSUBSCRIBE, subscription.getUrl()))
                    .build());
            keyboard.add(row);
        }
        markup.setKeyboard(keyboard);
        return markup;
    }

    public InlineKeyboardMarkup getLogRequestButton(String url, Services name) {
        var markup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var button = InlineKeyboardButton.builder()
                .text("Get log")
                .callbackData(getFormattedCallbackData(CallbackType.LOG, url, name.getNameInReleaseInfo()))
                .build();
        keyboard.add(Collections.singletonList(button));
        markup.setKeyboard(keyboard);
        return markup;
    }

    private String getFormattedCallbackData(CallbackType type, String... args) {
        var builder = new StringBuilder();
        builder.append(type.name());
        for (String arg : args) {
            builder.append(CALLBACK_DATA_SEPARATOR).append(arg);
        }
        return builder.toString();
    }

    public InlineKeyboardMarkup getReleaseInfoButton(String url) {
        var markup = new InlineKeyboardMarkup();
        var keyboard = new ArrayList<List<InlineKeyboardButton>>();
        var button = InlineKeyboardButton.builder()
                .text("Get release info")
                .callbackData(getFormattedCallbackData(CallbackType.RELEASE_INFO, url))
                .build();
        keyboard.add(Collections.singletonList(button));
        markup.setKeyboard(keyboard);
        return markup;
    }
}

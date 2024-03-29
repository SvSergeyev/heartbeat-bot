package tech.sergeyev.heartbeatbot.service.update.callback.query;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.service.update.callback.CallbackType;

public interface CallbackTypeProcessor {
    void process(CallbackQuery query) throws TelegramApiException;
    CallbackType getType();
}

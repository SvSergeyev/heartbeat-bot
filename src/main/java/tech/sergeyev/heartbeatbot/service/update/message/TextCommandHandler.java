package tech.sergeyev.heartbeatbot.service.update.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TextCommandHandler {
    void handle(Message message, AbsSender sender) throws TelegramApiException;
    String getName();
}

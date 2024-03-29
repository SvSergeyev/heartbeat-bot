package tech.sergeyev.heartbeatbot.service.update.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TextCommandHandler {
    SendMessage handle(Message message);
    String getName();
}

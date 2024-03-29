package tech.sergeyev.heartbeatbot.service.update.message.command;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.sergeyev.heartbeatbot.service.update.message.TextCommandHandler;
import tech.sergeyev.heartbeatbot.service.util.Messages;

@Service
public class StartCommandHandler implements TextCommandHandler {
    @Override
    public SendMessage handle(Message message) {
        var chatId = message.getChatId();
        return new SendMessage(
                String.valueOf(chatId),
                Messages.TEXT_IP);
    }

    @Override
    public String getName() {
        return "/start";
    }
}

package tech.sergeyev.heartbeatbot.service.update.message.command;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.service.update.message.TextCommandHandler;
import tech.sergeyev.heartbeatbot.service.util.Messages;

@Service
public class UnknownCommandHandler implements TextCommandHandler {
    @Override
    public void handle(Message message, AbsSender sender) throws TelegramApiException {
        var chatId = message.getChatId();
        var reply = new SendMessage(
                String.valueOf(chatId),
                Messages.UNKNOWN_COMMAND);
        sender.execute(reply);
    }

        @Override
    public String getName() {
        return null;
    }
}

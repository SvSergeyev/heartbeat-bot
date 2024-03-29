package tech.sergeyev.heartbeatbot.service.update.message;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import tech.sergeyev.heartbeatbot.service.util.IpResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageHandler {
    private final Map<String, TextCommandHandler> handlers = new HashMap<>();
    private final TextCommandHandler defaultHandler;
    private static final String IP_HANDLER_NAME = "ip";

    public MessageHandler(@Qualifier("unknownCommandHandler") TextCommandHandler defaultHandler,
                          List<TextCommandHandler> commandHandlers) {
        this.defaultHandler = defaultHandler;
        commandHandlers.forEach(handler -> handlers.put(handler.getName(), handler));
    }

    public SendMessage handleInputMessage(Message message) {
        if (IpResolver.isIpAddress(message.getText())) {
            return handlers.get(IP_HANDLER_NAME).handle(message);
        }
        return handlers.getOrDefault(message.getText(), defaultHandler).handle(message);
    }
}

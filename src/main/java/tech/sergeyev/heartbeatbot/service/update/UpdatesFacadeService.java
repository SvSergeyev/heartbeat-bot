package tech.sergeyev.heartbeatbot.service.update;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.exception.UpdateValidationException;
import tech.sergeyev.heartbeatbot.service.update.callback.CallbackHandler;
import tech.sergeyev.heartbeatbot.service.update.message.MessageHandler;
import tech.sergeyev.heartbeatbot.service.util.UpdateValidator;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdatesFacadeService implements UpdatesFacade {
    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;

    @Override
    public void handle(Update update, AbsSender sender) {
        try {
            if (update.hasCallbackQuery()) {
                callbackHandler.handleCallbackQuery(update.getCallbackQuery(), sender);
            } else {
                UpdateValidator.checkHasValidMessage(update);
                messageHandler.handleInputMessage(update.getMessage(), sender);
            }
        } catch (UpdateValidationException | TelegramApiException e) {
            log.error("Internal error when processing message: {}", e.getMessage());
            log.error("Error: ", e);
        }
    }
}

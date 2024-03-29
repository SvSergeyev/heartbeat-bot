package tech.sergeyev.heartbeatbot.service.update.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
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
    public void handle(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                callbackHandler.handleCallbackQuery(update.getCallbackQuery());
            }
            UpdateValidator.checkHasValidMessage(update);
            messageHandler.handleInputMessage(update.getMessage());
        } catch (UpdateValidationException e) {
            log.error("Cannot process update");
        }
    }
}

package tech.sergeyev.heartbeatbot.service.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.Update;
import tech.sergeyev.heartbeatbot.exception.UpdateValidationException;

@UtilityClass
public class UpdateValidator {
    public void checkHasValidMessage(Update update) throws UpdateValidationException {
        if (!update.hasMessage()) {
            throw new UpdateValidationException("Update must contain message. See '/help'");
        }
        if (!update.getMessage().hasText()) {
            throw new UpdateValidationException("Message must contain text. See '/help'");
        }
    }
}

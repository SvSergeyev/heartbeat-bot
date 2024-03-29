package tech.sergeyev.heartbeatbot.service.update.callback;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.exception.CallbackTypeConversionError;
import tech.sergeyev.heartbeatbot.service.update.callback.query.CallbackTypeProcessor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class CallbackHandler {
    public static final String CALLBACK_DATA_SEPARATOR = "::";
    private final Map<CallbackType, CallbackTypeProcessor> processors = new EnumMap<>(CallbackType.class);

    public CallbackHandler(List<CallbackTypeProcessor> processors) {
        processors.forEach(processor -> this.processors.put(processor.getType(), processor));
    }

    public void handleCallbackQuery(CallbackQuery query) {
        try {
            var type = extractTypeFromQuery(query);
            processors.getOrDefault(type, processors.get(CallbackType.UNKNOWN)).process(query);
        } catch (CallbackTypeConversionError e) {
//            return new SendMessage(query.getMessage().getChatId().toString(), e.getMessage());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private CallbackType extractTypeFromQuery(CallbackQuery query) throws CallbackTypeConversionError {
        var type = query.getData().split(CALLBACK_DATA_SEPARATOR)[0];
        return CallbackType.convertToCallbackType(type);
    }
}

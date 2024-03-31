package tech.sergeyev.heartbeatbot.service.update.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.exception.CallbackTypeConversionError;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CallbackHandler {
    public static final String CALLBACK_DATA_SEPARATOR = "::";
    private final Map<CallbackType, CallbackTypeProcessor> processors = new EnumMap<>(CallbackType.class);

    public CallbackHandler(List<CallbackTypeProcessor> processors) {
        processors.forEach(processor -> this.processors.put(processor.getType(), processor));
    }

    public void handleCallbackQuery(CallbackQuery query, AbsSender sender) {
        try {
            var type = extractTypeFromQuery(query);
            processors.getOrDefault(type, processors.get(CallbackType.UNKNOWN)).process(query, sender);
        } catch (CallbackTypeConversionError | TelegramApiException e) {
            log.error("Internal error when processing callback: {}", e.getMessage());
            log.error("Error: ", e);
        }
    }

    private CallbackType extractTypeFromQuery(CallbackQuery query) throws CallbackTypeConversionError {
        var type = query.getData().split(CALLBACK_DATA_SEPARATOR)[0];
        return CallbackType.convertToCallbackType(type);
    }
}

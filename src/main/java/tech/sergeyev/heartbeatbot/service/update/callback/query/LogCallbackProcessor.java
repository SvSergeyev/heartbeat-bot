package tech.sergeyev.heartbeatbot.service.update.callback.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.exception.ServiceNameConversionException;
import tech.sergeyev.heartbeatbot.service.update.callback.CallbackType;
import tech.sergeyev.heartbeatbot.service.util.LogFilesCollector;
import tech.sergeyev.heartbeatbot.service.util.Services;

import static tech.sergeyev.heartbeatbot.service.update.callback.CallbackHandler.CALLBACK_DATA_SEPARATOR;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogCallbackProcessor implements CallbackTypeProcessor {
    private final AbsSender sender;

    @Override
    public void process(CallbackQuery query) throws TelegramApiException {
        var chatId = query.getMessage().getChatId();
        var reply = new SendDocument();
        reply.setChatId(chatId);
        try {
            var url = query.getData().split(CALLBACK_DATA_SEPARATOR)[1];
            var service = Services.covertNameFromReleaseInfo(
                    query.getData().split(CALLBACK_DATA_SEPARATOR)[2]);
            reply.setDocument(getLogFiles(url, service));
        } catch (ServiceNameConversionException e) {
            log.error("An error occurred: ", e);
        }
        sender.execute(reply);
    }

    @Override
    public CallbackType getType() {
        return CallbackType.LOG;
    }

    private InputFile getLogFiles(String url, Services service) {
        var file = LogFilesCollector.getLogFilesAsZip(url, service);
        return new InputFile(file);
    }
}

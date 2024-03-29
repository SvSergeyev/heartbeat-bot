package tech.sergeyev.heartbeatbot.service.update.callback.query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tech.sergeyev.heartbeatbot.dto.ReleaseInfo;
import tech.sergeyev.heartbeatbot.exception.ReleaseInfoParsingException;
import tech.sergeyev.heartbeatbot.service.update.callback.CallbackType;
import tech.sergeyev.heartbeatbot.service.util.Messages;
import tech.sergeyev.heartbeatbot.service.util.ReleaseInfoCollector;

import java.util.List;

import static tech.sergeyev.heartbeatbot.service.update.callback.CallbackHandler.CALLBACK_DATA_SEPARATOR;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReleaseInfoProcessor implements CallbackTypeProcessor {
    private final Messages messages;
    private final AbsSender sender;

    @Override
    public void process(CallbackQuery query) throws TelegramApiException {
        var chatId = query.getMessage().getChatId();
        var url = query.getData().split(CALLBACK_DATA_SEPARATOR)[1];
        var reply = new SendMessage();
        try {
            var infoList = ReleaseInfoCollector.collectAll(url);
            reply.setText(getFormattedMessage(infoList));
        } catch (ReleaseInfoParsingException e) {
            log.error("Error: ", e);
            reply.setText(messages.getMessage(Messages.INTERNAL_ERROR, e.getMessage()));
        }
        reply.setChatId(String.valueOf(chatId));
        sender.execute(reply);
    }

    @Override
    public CallbackType getType() {
        return CallbackType.RELEASE_INFO;
    }

    public String getFormattedMessage(List<ReleaseInfo> releaseInfoList) {
        var builder = new StringBuilder();
        builder.append("All services:").append("\n\n");
        for (var releaseInfo : releaseInfoList) {
            builder.append(releaseInfo.getName().getNameInReleaseInfo()).append("\n");
            builder.append(messages.getMessage(
                    Messages.RELEASE_INFO_TEMPLATE,
                    releaseInfo.getBranch(),
                    releaseInfo.getCommit(),
                    releaseInfo.getTime())).append("\n\n");
        }
        return builder.toString();
    }
}

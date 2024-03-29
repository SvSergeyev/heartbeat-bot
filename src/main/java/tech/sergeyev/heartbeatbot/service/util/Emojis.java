package tech.sergeyev.heartbeatbot.service.util;

import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Emojis {
    WHITE_CHECK_MARK(EmojiParser.parseToUnicode(":white_check_mark:")),
    RED_X(EmojiParser.parseToUnicode(":x:"));

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}

package team.isaz.dmbot.domain.talk;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.common.exception.NoNeedResponseException;
import team.isaz.dmbot.domain.common.model.Random;
import team.isaz.dmbot.domain.common.repository.StickerRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TalkModule {
    private final Random random;
    private final StickerRepository stickerRepository;

    public List<PartialBotApiMethod<Message>> migrate(Message message) {
        if (!random.chanceOf(4)) {
            throw new NoNeedResponseException();
        }
        String chatId = String.valueOf(message.getChatId());
        List<PartialBotApiMethod<Message>> values = new ArrayList<>();
        int count = getManulCount();
        String text = count == 1 ? "М А Н У Л!" : "МИГРАЦИЯ МАНУЛОВ!";
        values.add(SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text(text)
                .build());
        InputFile sticker = new InputFile(stickerRepository.getManul());
        for (int i = 0; i < count; i++) {
            values.add(SendSticker.builder()
                    .replyToMessageId(message.getMessageId())
                    .chatId(chatId)
                    .sticker(sticker)
                    .build());
        }
        return values;
    }

    public PartialBotApiMethod<Message> war(Message message) {
        String chatId = String.valueOf(message.getChatId());
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text("Ужасы войны, ужасы войны, ужасы войны...")
                .build();
    }

    public PartialBotApiMethod<Message> coin(Message message) {
        String chatId = String.valueOf(message.getChatId());
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text("Монетка-монетка!")
                .build();
    }

    private int getManulCount() {
        int v = random.nextInt(11);
        return switch (v) {
            case 0, 1, 2 -> 2;
            case 3, 4 -> 3;
            case 5 -> 4;
            case 6 -> 5;
            default -> 1;
        };
    }


}

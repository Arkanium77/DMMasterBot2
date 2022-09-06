package team.isaz.dmbot.domain.gaston;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.common.exception.NoNeedResponseException;
import team.isaz.dmbot.domain.common.repository.StringBasedRepository;

@Component
@RequiredArgsConstructor
public class GastonModule {
    @Qualifier("gastonFirstBase")
    private final StringBasedRepository firstRepository;

    @Qualifier("gastonSecondBase")
    private final StringBasedRepository secondRepository;

    public PartialBotApiMethod<Message> gastonFirst(Message message) {
        String chatId = String.valueOf(message.getChatId());
        String text = firstRepository.find(chatId)
                .orElseThrow(NoNeedResponseException::new);
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text(text)
                .build();
    }


    public PartialBotApiMethod<Message> gastonSecond(Message message) {
        String chatId = String.valueOf(message.getChatId());
        String text = secondRepository.find(chatId)
                .orElseThrow(NoNeedResponseException::new);
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text(text)
                .build();
    }
}

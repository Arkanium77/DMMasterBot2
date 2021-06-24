package team.isaz.dmbot.domain.rofl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.common.repository.StringBasedRepository;

@Component
@RequiredArgsConstructor
public class RoflModule {
    @Qualifier("roflBase")
    private final StringBasedRepository repository;

    public PartialBotApiMethod<Message> rofl(Message message) {
        String chatId = String.valueOf(message.getChatId());
        String text = repository.find(chatId);
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text(text)
                .build();
    }
}

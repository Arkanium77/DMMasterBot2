package team.isaz.dmbot.domain.rofl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.rofl.repository.RoflRepository;

@Component
@RequiredArgsConstructor
public class RoflModule {
    private final RoflRepository repository;

    public PartialBotApiMethod<Message> rofl(Message message) {
        String chatId = String.valueOf(message.getChatId());
        String text = repository.findRofl(chatId);
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text(text)
                .build();
    }
}

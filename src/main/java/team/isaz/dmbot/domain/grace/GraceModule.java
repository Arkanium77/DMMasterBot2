package team.isaz.dmbot.domain.grace;

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
public class GraceModule {
    @Qualifier("graceBase")
    private final StringBasedRepository repository;

    public PartialBotApiMethod<Message> grace(Message message) {
        String chatId = String.valueOf(message.getChatId());
        String text = repository.find(chatId)
                .orElseThrow(NoNeedResponseException::new);
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text(text)
                .build();
    }


}

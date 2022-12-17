package team.isaz.dmbot.domain.excuse;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.common.exception.NoNeedResponseException;
import team.isaz.dmbot.domain.common.model.Random;
import team.isaz.dmbot.domain.common.repository.StringBasedRepository;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ExcuseModule {
    private final Random random;
    @Qualifier("excuseStartBase")
    private final StringBasedRepository excuseStartRepository;
    @Qualifier("excuseReasonBase")
    private final StringBasedRepository excuseReasonRepository;
    @Qualifier("excuseAdditionBase")
    private final StringBasedRepository excuseAdditionRepository;
    @Qualifier("excuseEndBase")
    private final StringBasedRepository excuseEndRepository;

    public PartialBotApiMethod<Message> excuse(Message message) {
        String chatId = String.valueOf(message.getChatId());
        StringBuilder text = new StringBuilder(excuseStartRepository.find(chatId).orElseThrow(NoNeedResponseException::new))
                .append(" ")
                .append(capitalize(excuseReasonRepository.find(chatId).orElseThrow(NoNeedResponseException::new)));
        int i = 2;
        while (random.chanceOf(i)) {
            text.append(excuseAdditionRepository.find(chatId).orElseThrow(NoNeedResponseException::new))
                    .append(" ")
                    .append(excuseReasonRepository.find(chatId).orElseThrow(NoNeedResponseException::new));
            i++;
        }
        text.append(excuseEndRepository.find(chatId).orElseThrow(NoNeedResponseException::new));
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text(text.toString())
                .build();
    }

    private String capitalize(String text) {
        if (text == null) {
            return text;
        }
        if (text.length() < 2) {
            return text.toUpperCase(Locale.ROOT);
        }
        String l1 = text.substring(0, 1).toUpperCase(Locale.ROOT);
        return l1 + text.substring(1);
    }


}

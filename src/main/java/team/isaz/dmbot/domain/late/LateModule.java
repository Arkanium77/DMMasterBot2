package team.isaz.dmbot.domain.late;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.common.exception.NoNeedResponseException;
import team.isaz.dmbot.domain.common.model.Random;
import team.isaz.dmbot.domain.common.repository.StringBasedRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LateModule {
    private final Random random;
    @Qualifier("lateBase")
    private final StringBasedRepository repository;

    public PartialBotApiMethod<Message> late(Message message) {
        String chatId = String.valueOf(message.getChatId());
        List<String> values = new ArrayList<>();
        String text = repository.find(chatId)
                .orElseThrow(NoNeedResponseException::new);
        values.add(minuteMessage());
        if (random.chanceOf(3)) {
            values.add(hourMessage());
            if (random.chanceOf(5)) {
                values.add(dayMessage());
            }
        }
        text = formResponse(text, values);
        return SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(chatId)
                .text(text)
                .build();
    }

    private String formResponse(String start, List<String> values) {
        Collections.reverse(values);
        StringBuilder message = new StringBuilder(start).append(" ");
        if (values.size() > 1) {
            message
                    .append(String.join(", ", values.subList(0, values.size() - 1)))
                    .append(" и ");
        }

        message.append(values.get(values.size() - 1));
        return message.toString();
    }

    private int genMinutes() {
        return random.nextInt(1, 60);
    }

    private int genHours() {
        return random.nextInt(1, 24);
    }

    private int genDays() {
        return random.nextInt(1, 32);
    }

    private String minuteMessage() {
        int c = genMinutes();
        StringBuilder s = new StringBuilder()
                .append(c).append(" мин");
        if (c > 20) {
            c %= 10;
        } else {
            c %= 20;
        }
        if (c == 1) {
            s.append("уту");
        } else if (c > 1 && c < 5) {
            s.append("уты");
        } else {
            s.append("ут");
        }
        return s.toString();
    }


    private String hourMessage() {
        int c = genHours();
        StringBuilder s = new StringBuilder()
                .append(c).append(" час");
        if (c > 20) {
            c %= 10;
        } else {
            c %= 20;
        }

        if (c > 1 && c < 5) {
            s.append("а");
        } else {
            s.append("ов");
        }
        return s.toString();
    }


    private String dayMessage() {
        int c = genDays();
        StringBuilder s = new StringBuilder()
                .append(c).append(" ");
        if (c > 20) {
            c %= 10;
        } else {
            c %= 20;
        }
        if (c == 1) {
            s.append("день");
        } else if (c > 1 && c < 5) {
            s.append("дня");
        } else {
            s.append("дней");
        }
        return s.toString();
    }

}

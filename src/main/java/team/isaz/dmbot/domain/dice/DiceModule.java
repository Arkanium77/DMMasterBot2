package team.isaz.dmbot.domain.dice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.common.exception.NoNeedResponseException;
import team.isaz.dmbot.domain.common.utils.DataUtils;
import team.isaz.dmbot.domain.dice.model.DiceThrower;
import team.isaz.dmbot.domain.dice.service.DiceThrowingService;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiceModule {
    private final DiceThrowingService diceThrowingService;

    public PartialBotApiMethod<Message> throwDice(Message message) {
        if (message == null || message.getText() == null) {
            throw new NoNeedResponseException();
        }
        String chatId = String.valueOf(message.getChatId());
        DiceThrower diceThrower = diceThrowingService.parseDiceThrower(message.getText());
        if (diceThrower.getCount() < 1) {
            throw new NoNeedResponseException();
        }
        if (diceThrower.getCount() > 500) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .replyToMessageId(message.getMessageId())
                    .text(String.format(oversizeMessage, diceThrower.getCount()))
                    .build();
        }
        if (diceThrowingService.isD20Sticker(diceThrower)) {
            InputFile sticker = diceThrowingService.d20Sticker(diceThrower.getDiceRoller().get());
            return SendSticker.builder()
                    .chatId(chatId)
                    .replyToMessageId(message.getMessageId())
                    .sticker(sticker)
                    .build();
        }
        if (diceThrowingService.isCoinSticker(diceThrower)) {
            InputFile sticker = diceThrowingService.coinSticker(diceThrower.getDiceRoller().get());
            return SendSticker.builder()
                    .chatId(chatId)
                    .replyToMessageId(message.getMessageId())
                    .sticker(sticker)
                    .build();
        }
        String text = diceThrowingService.formMultipleDiceThrowAnswer(DataUtils.getName(message.getFrom()), diceThrower);
        return SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(message.getMessageId())
                .text(text)
                .build();
    }

    private final String oversizeMessage = "Никак не могу этого сделать! Телеграм не позволит мне запостить результаты броска %d дайсов!";

    public PartialBotApiMethod<Message> throwD20(Message message) {
        message.setText("d20");
        return throwDice(message);
    }

    public PartialBotApiMethod<Message> flipCoin(Message message) {
        message.setText("dC");
        return throwDice(message);
    }
}

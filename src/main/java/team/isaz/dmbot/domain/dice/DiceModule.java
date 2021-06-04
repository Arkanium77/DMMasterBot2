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
import team.isaz.dmbot.domain.dice.service.MessageFormingService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiceModule {
    private final DiceThrowingService diceThrowingService;
    private final MessageFormingService messageFormingService;

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
        if (messageFormingService.isD20Sticker(diceThrower)) {
            InputFile sticker = messageFormingService.d20Sticker(diceThrower.getDiceRoller().get());
            return SendSticker.builder()
                    .chatId(chatId)
                    .replyToMessageId(message.getMessageId())
                    .sticker(sticker)
                    .build();
        }
        if (messageFormingService.isCoinSticker(diceThrower)) {
            InputFile sticker = messageFormingService.coinSticker(diceThrower.getDiceRoller().get());
            return SendSticker.builder()
                    .chatId(chatId)
                    .replyToMessageId(message.getMessageId())
                    .sticker(sticker)
                    .build();
        }
        String text = messageFormingService.formDiceThrowAnswer(DataUtils.getName(message.getFrom()), diceThrower);
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

    public PartialBotApiMethod<Message> multiDiceThrowing1(Message message) {
        if (message == null || message.getText() == null) {
            throw new NoNeedResponseException();
        }
        String chatId = String.valueOf(message.getChatId());
        List<DiceThrower> diceThrowers = Arrays.stream(message.getText()
                .split(" "))
                .map(diceThrowingService::parseDiceThrower)
                .collect(Collectors.toList());
        return multiDiceAnswer(message, chatId, diceThrowers);
    }

    public PartialBotApiMethod<Message> multiDiceThrowing2(Message message) {
        if (message == null || message.getText() == null) {
            throw new NoNeedResponseException();
        }
        String chatId = String.valueOf(message.getChatId());
        String[] array = message.getText().split(" ");
        int modifier = 0;
        if (array.length > 2 && array[array.length - 1].matches("[+-][1-9]\\d*")) {
            modifier = Integer.parseInt(array[array.length - 1]);
            array = Arrays.copyOfRange(array, 0, array.length - 1);
        }

        int finalModifier = modifier;
        List<DiceThrower> diceThrowers = Arrays.stream(array)
                .map(diceThrowingService::parseDiceThrower)
                .map(dt -> dt.withModifier(finalModifier))
                .collect(Collectors.toList());
        return multiDiceAnswer(message, chatId, diceThrowers);
    }

    private PartialBotApiMethod<Message> multiDiceAnswer(Message message, String chatId, List<DiceThrower> diceThrowers) {
        if (diceThrowers.stream().anyMatch(d -> d.getCount() < 1)) {
            throw new NoNeedResponseException();
        }
        int diceCount = diceThrowers.stream().mapToInt(DiceThrower::getCount).sum();
        if (diceCount > 500) {
            return SendMessage.builder()
                    .chatId(chatId)
                    .replyToMessageId(message.getMessageId())
                    .text(String.format(oversizeMessage, diceCount))
                    .build();
        }

        String text = messageFormingService.formMultiDiceThrowAnswer(DataUtils.getName(message.getFrom()), diceThrowers);
        return SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(message.getMessageId())
                .text(text)
                .build();
    }
}

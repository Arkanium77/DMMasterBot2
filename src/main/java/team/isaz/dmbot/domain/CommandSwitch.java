package team.isaz.dmbot.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.common.repository.RegexRepository;
import team.isaz.dmbot.domain.dice.DiceModule;
import team.isaz.dmbot.domain.rofl.RoflModule;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandSwitch {
    private final RegexRepository regexRepository;
    private final DiceModule diceModule;
    private final RoflModule roflModule;

    public List<PartialBotApiMethod<Message>> fetchResponse(Message message) {
        List<PartialBotApiMethod<Message>> responseMessage = new ArrayList<>();
        if (message.getText().startsWith("/")) {
            responseMessage.addAll(parseCommandMessage(message));
        } else if (regexRepository.diceThrowing().test(message.getText())) {
            responseMessage.add(diceModule.throwDice(message));
        } else if (regexRepository.multiDiceThrowing1().test(message.getText())) {
            responseMessage.add(diceModule.multiDiceThrowing1(message));
        } else if (regexRepository.multiDiceThrowing2().test(message.getText())) {
            responseMessage.add(diceModule.multiDiceThrowing2(message));
        }
        return responseMessage;
    }

    private List<PartialBotApiMethod<Message>> parseCommandMessage(Message message) {
        List<PartialBotApiMethod<Message>> responseMessage = new ArrayList<>();
        if (regexRepository.commandThrow().test(message.getText())) {
            responseMessage.add(diceModule.throwD20(message));
        } else if (regexRepository.commandFlip().test(message.getText())) {
            responseMessage.add(diceModule.flipCoin(message));
        } else if (regexRepository.commandRofl().test(message.getText())) {
            responseMessage.add(roflModule.rofl(message));
        }
        return responseMessage;
    }

}

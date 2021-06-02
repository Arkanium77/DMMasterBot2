package team.isaz.dmbot.domain.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.domain.common.repository.RegexRepository;
import team.isaz.dmbot.domain.dice.DiceThrowingService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandSwitchService {
    private final RegexRepository regexRepository;
    private final DiceThrowingService diceThrowingService;

    public List<PartialBotApiMethod<Message>> fetchResponse(Message message) {
        List<PartialBotApiMethod<Message>> responseMessage = new ArrayList<>();
        if (regexRepository.diceThrowing().test(message.getText())) {
            responseMessage.add(diceThrowingService.throwDice(message));
        }
        return responseMessage;
    }

}

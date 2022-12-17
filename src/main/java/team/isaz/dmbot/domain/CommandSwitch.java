package team.isaz.dmbot.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import team.isaz.dmbot.configuration.BotConfig;
import team.isaz.dmbot.domain.common.repository.RegexRepository;
import team.isaz.dmbot.domain.dice.DiceModule;
import team.isaz.dmbot.domain.excuse.ExcuseModule;
import team.isaz.dmbot.domain.gaston.GastonModule;
import team.isaz.dmbot.domain.grace.GraceModule;
import team.isaz.dmbot.domain.late.LateModule;
import team.isaz.dmbot.domain.rofl.RoflModule;
import team.isaz.dmbot.domain.talk.TalkModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandSwitch {
    private final BotConfig botConfig;
    private final RegexRepository regexRepository;
    private final DiceModule diceModule;
    private final RoflModule roflModule;
    private final LateModule lateModule;
    private final TalkModule talkModule;
    private final GraceModule graceModule;
    private final ExcuseModule excuseModule;
    private final GastonModule gastonModule;

    public List<PartialBotApiMethod<Message>> fetchResponse(Message message) {
        List<PartialBotApiMethod<Message>> responseMessage = new ArrayList<>();
        String route = fetchRouteString(message.getText());
        log.debug(route);
        if (route.startsWith("/")) {
            responseMessage.addAll(parseCommandMessage(message, route));
        } else if (regexRepository.diceThrowing().test(route)) {
            responseMessage.add(diceModule.throwDice(message));
        } else if (regexRepository.multiDiceThrowing1().test(route)) {
            responseMessage.add(diceModule.multiDiceThrowing1(message));
        } else if (regexRepository.multiDiceThrowing2().test(route)) {
            responseMessage.add(diceModule.multiDiceThrowing2(message));
        } else if (regexRepository.late().test(route)) {
            responseMessage.add(lateModule.late(message));
        } else {
            responseMessage.addAll(parseTalk(message, route));
        }
        return responseMessage;
    }

    private String fetchRouteString(String text) {
        return text == null || text.trim().isBlank() ? ""
                : text.toLowerCase(Locale.ROOT).replace("\n", " ");
    }

    private List<PartialBotApiMethod<Message>> parseTalk(Message message, String route) {
        List<PartialBotApiMethod<Message>> responseMessage = new ArrayList<>();
        if (regexRepository.manul().test(route)) {
            responseMessage.addAll(talkModule.migrate(message));
        } else if (regexRepository.war().test(route)) {
            responseMessage.add(talkModule.war(message));
        } else if (regexRepository.coin().test(route)) {
            responseMessage.add(talkModule.coin(message));
        } else if (regexRepository.grace().test(route)) {
            responseMessage.add(graceModule.grace(message));
        } else if (!isReplyToMe(message) && regexRepository.gastonFirst().test(route)) {
            responseMessage.add(gastonModule.gastonFirst(message));
        } else if (isReplyToMe(message) && regexRepository.gastonSecond().test(route)) {
            responseMessage.add(gastonModule.gastonSecond(message));
        }
        return responseMessage;
    }

    private List<PartialBotApiMethod<Message>> parseCommandMessage(Message message, String route) {
        List<PartialBotApiMethod<Message>> responseMessage = new ArrayList<>();
        if (regexRepository.commandThrow().test(route)) {
            responseMessage.add(diceModule.throwD20(message));
        } else if (regexRepository.commandFlip().test(route)) {
            responseMessage.add(diceModule.flipCoin(message));
        } else if (regexRepository.commandRofl().test(route)) {
            responseMessage.add(roflModule.rofl(message));
        } else if (regexRepository.commandLate().test(route)) {
            responseMessage.add(lateModule.late(message));
        } else if (regexRepository.commandExcuse().test(route)) {
            responseMessage.add(excuseModule.excuse(message));
        }
        return responseMessage;
    }

    private boolean isReplyToMe(Message message) {
        if (!message.isReply()) {
            return false;
        }
        return message.getReplyToMessage().getFrom().getUserName().equals(botConfig.getName());
    }

}

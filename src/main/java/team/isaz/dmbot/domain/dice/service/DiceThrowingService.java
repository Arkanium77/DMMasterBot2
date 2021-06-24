package team.isaz.dmbot.domain.dice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.isaz.dmbot.domain.common.model.Random;
import team.isaz.dmbot.domain.common.utils.DataUtils;
import team.isaz.dmbot.domain.dice.model.DiceThrower;
import team.isaz.dmbot.domain.dice.model.DiceTypeEnum;

import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiceThrowingService {
    private final Random random;

    public static int getAdvantage(int d1, int d2, int modifier) {
        int max = d1;
        if (d2 > max) {
            max = d2;
        }
        return max + modifier;
    }

    public static int getDisadvantage(int d1, int d2, int modifier) {
        int min = d1;
        if (d2 < min) {
            min = d2;
        }
        return min + modifier;
    }

    public DiceThrower parseDiceThrower(String text) {
        String[] args = text.split("d");
        if (args[0].isBlank()) {
            args[0] = "1";
        }
        DiceThrower.ThrowRequestBuilder builder = DiceThrower.builder()
                .count(args[0]);
        if (args[1].contains("+") || args[1].contains("-")) {
            String[] mod = args[1].split("\\+");
            if (mod.length == 1) {
                mod = args[1].split("-");
                mod[1] = "-" + mod[1];
            }
            builder.modifier(mod[1]);
            args[1] = mod[0];
        } else {
            builder.modifier(0);
        }

        args[1] = normalizeDiceType(args[1]);
        DiceTypeEnum type = DiceTypeEnum.formByValue(args[1]);

        return builder
                .type(type)
                .name(args[1])
                .dice(formDice(type, args[1]))
                .build();
    }

    private String normalizeDiceType(String arg) {
        arg = arg.toUpperCase().strip();
        if (arg.equals("D")) {
            arg = "20";
        }
        return arg;
    }

    public Supplier<Integer> formDice(DiceTypeEnum type, String arg) {
        switch (type) {
            case D20:
            case COMMON:
                int bound = DataUtils.fetchInt(arg).orElseThrow() + 1;
                return () -> random.nextInt(1, bound);
            case COIN:
                return () -> random.nextInt(2);
            case FUDGE:
                return () -> random.nextInt(-1, 2);
            default:
                throw new RuntimeException("No action for type " + type.name());
        }
    }
}

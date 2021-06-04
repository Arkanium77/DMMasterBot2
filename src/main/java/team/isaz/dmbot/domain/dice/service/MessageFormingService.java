package team.isaz.dmbot.domain.dice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import team.isaz.dmbot.domain.common.model.Pair;
import team.isaz.dmbot.domain.common.repository.StickerRepository;
import team.isaz.dmbot.domain.dice.model.DiceThrower;
import team.isaz.dmbot.domain.dice.model.DiceTypeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageFormingService {

    private final StickerRepository stickerRepository;

    public boolean isCoinSticker(DiceThrower request) {
        return request.getCount() == 1 && request.getModifier() == 0 && request.getType().equals(DiceTypeEnum.COIN);
    }

    public boolean isD20Sticker(DiceThrower request) {
        return request.getCount() == 1 && request.getModifier() == 0 && request.getType().equals(DiceTypeEnum.D20);
    }

    public InputFile coinSticker(Integer integer) {
        return new InputFile(stickerRepository.getCoin(integer));
    }

    public InputFile d20Sticker(Integer integer) {
        return new InputFile(stickerRepository.getD20(integer));
    }

    public String formDiceThrowAnswer(String username, DiceThrower thrower) {
        int[] throwResult = new int[thrower.getCount()];
        for (int i = 0; i < thrower.getCount(); i++) {
            throwResult[i] = thrower.getDiceRoller().get();
        }
        long sum = sum(throwResult, thrower.getModifier());
        return formText(username, thrower, throwResult, sum);
    }

    public String formMultiDiceThrowAnswer(String username, List<DiceThrower> diceThrowers) {
        List<Pair<Long, String>> res = diceThrowers.stream()
                .map(thrower -> {
                    int[] throwResult = new int[thrower.getCount()];
                    for (int i = 0; i < thrower.getCount(); i++) {
                        throwResult[i] = thrower.getDiceRoller().get();
                    }
                    long sum = sum(throwResult, thrower.getModifier());
                    return Pair.of(sum, formText(username, thrower, throwResult, sum));
                }).collect(Collectors.toList());
        long superSum = res.stream()
                .mapToLong(Pair::getLeft)
                .sum();
        String text = res.stream()
                .map(Pair::getRight)
                .map(s -> s.replace("Total score:", "Score:"))
                .collect(Collectors.joining("\n\n"));
        return text + "\n\nTotal score: " + superSum;
    }

    private long sum(int[] throwResult, int modifier) {
        return Arrays.stream(throwResult).sum() + modifier;
    }

    private String formText(String userName, DiceThrower request, int[] throwResult, long sum) {
        StringBuilder builder = new StringBuilder(userName)
                .append(" throws ")
                .append(formDiceName(request))
                .append(":\n");

        String result = Arrays.stream(throwResult)
                .mapToObj(i -> stringValue(i, request.getType()))
                .collect(Collectors.joining(" | "));

        builder.append("|").append(result).append("|").append("\n");
        if (!DiceTypeEnum.COIN.equals(request.getType())) {
            if (request.getModifier() != 0) {
                builder.append("Score modifier: ").append(request.getModifier() > 0 ? "+" : "")
                        .append(request.getModifier()).append("\n");
            }
            if (request.getType().equals(DiceTypeEnum.D20) && request.getCount() == 2) {
                builder.append("Advantage result: ")
                        .append(DiceThrowingService.getAdvantage(throwResult[0], throwResult[1], request.getModifier())).append("\n");
                builder.append("Disadvantage result: ")
                        .append(DiceThrowingService.getDisadvantage(throwResult[0], throwResult[1], request.getModifier())).append("\n");
            }
            builder.append("Total score: ").append(sum);
        }
        return builder.toString();
    }

    private String formDiceName(DiceThrower request) {
        if (request.getType().equals(DiceTypeEnum.COIN)) {
            return request.getCount() + " coins";
        }
        return request.getCount() + "d" + request.getName();
    }

    private String stringValue(int value, DiceTypeEnum type) {
        if (type.equals(DiceTypeEnum.COMMON) || type.equals(DiceTypeEnum.D20)) {
            return String.valueOf(value);
        }
        if (type.equals(DiceTypeEnum.COIN)) {
            if (value == 0) {
                return "tails";
            } else if (value == 1) {
                return "heads";
            }
            throw new RuntimeException("Unknown value \"" + value + "\" to COIN dice type");
        }
        if (type.equals(DiceTypeEnum.FUDGE)) {
            if (value == -1) {
                return "-";
            } else if (value == 0) {
                return " ";
            } else if (value == 1) {
                return "+";
            }
            throw new RuntimeException("Unknown value \"" + value + "\" to FUDGE dice type");
        }
        throw new RuntimeException("Unknown dice type " + type.name());
    }
}

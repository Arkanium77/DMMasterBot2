package team.isaz.dmbot.domain.dice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import team.isaz.dmbot.domain.common.exception.NoNeedResponseException;
import team.isaz.dmbot.domain.common.repository.StickerRepository;
import team.isaz.dmbot.domain.common.utils.DataUtils;
import team.isaz.dmbot.domain.dice.model.DiceTypeEnum;
import team.isaz.dmbot.domain.dice.model.ThrowRequest;

import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiceThrowingService {
    private final SplittableRandom random;
    private final StickerRepository stickerRepository;

    public PartialBotApiMethod<Message> throwDice(Message message) {
        if (message == null || message.getText() == null) {
            throw new NoNeedResponseException();
        }
        ThrowRequest request = fetchThrowRequest(message.getText());
        String chatId = String.valueOf(message.getChatId());
        if (canAnswerWithSticker(request)) {
            InputFile sticker = getSticker(request);
            return SendSticker.builder()
                    .chatId(chatId)
                    .replyToMessageId(message.getMessageId())
                    .sticker(sticker)
                    .build();
        }

        int[] throwResult = new int[request.getCount()];
        for (int i = 0; i < request.getCount(); i++) {
            throwResult[i] = request.getDiceRoller().get();
        }
        long sum = sum(throwResult, request.getModifier());
        return SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(message.getMessageId())
                .text(formText(getName(message.getFrom()), request, throwResult, sum))
                .build();
    }

    private boolean canAnswerWithSticker(ThrowRequest request) {
        return isD20Sticker(request) || isCoinSticker(request);
    }

    private InputFile getSticker(ThrowRequest request) {
        if (isD20Sticker(request)) {
            return d20Sticker(request.getDiceRoller().get());
        }
        return coin(request.getDiceRoller().get());
    }

    private InputFile coin(Integer integer) {
        return new InputFile(stickerRepository.getCoin(integer));
    }

    private InputFile d20Sticker(Integer integer) {
        return new InputFile(stickerRepository.getD20(integer));
    }

    private String getName(User user) {
        String name = user.getUserName();
        if (name == null || name.isBlank()) {
            name = String.join(" ", new String[]{user.getFirstName(), user.getLastName()});
        }
        return name;
    }

    private boolean isCoinSticker(ThrowRequest request) {
        return request.getCount() == 1 && request.getModifier() == 0 && request.getType().equals(DiceTypeEnum.COIN);
    }

    private boolean isD20Sticker(ThrowRequest request) {
        return request.getCount() == 1 && request.getModifier() == 0 && request.getType().equals(DiceTypeEnum.D20);
    }

    private String formText(String userName, ThrowRequest request, int[] throwResult, long sum) {
        StringBuilder builder = new StringBuilder(userName).append(" throws:\n");

        String result = Arrays.stream(throwResult)
                .sorted()
                .mapToObj(i -> stringValue(i, request.getType()))
                .collect(Collectors.joining(" | "));

        builder.append(result).append("\n");
        if (!DiceTypeEnum.COIN.equals(request.getType())) {
            if (request.getModifier() != 0) {
                builder.append("Score modifier: ").append(request.getModifier() > 0 ? "+" : "")
                        .append(request.getModifier()).append("\n");
            }
            if (request.getType().equals(DiceTypeEnum.D20) && request.getCount() == 2) {
                builder.append("Advantage result: ")
                        .append(getAdvantage(throwResult[0], throwResult[1], request.getModifier())).append("\n");
                builder.append("Disadvantage result: ")
                        .append(getDisadvantage(throwResult[0], throwResult[1], request.getModifier())).append("\n");
            }
            builder.append("Total score: ").append(sum);
        }
        return builder.toString();
    }

    private int getAdvantage(int d1, int d2, int modifier) {
        int max = d1;
        if (d2 > max) {
            max = d2;
        }
        return max + modifier;
    }

    private int getDisadvantage(int d1, int d2, int modifier) {
        int min = d1;
        if (d2 < min) {
            min = d2;
        }
        return min + modifier;
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

    private long sum(int[] throwResult, int modifier) {
        return Arrays.stream(throwResult).sum() + modifier;
    }

    private ThrowRequest fetchThrowRequest(String message) {
        String[] args = message.split("d");
        if (args[0].isBlank()) {
            args[0] = "1";
        }
        ThrowRequest.ThrowRequestBuilder builder = ThrowRequest.builder()
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
        DiceTypeEnum type = identifyType(args[1]);
        builder.type(type);
        return builder
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

    private DiceTypeEnum identifyType(String arg) {
        if (arg.equals("F")) {
            return DiceTypeEnum.FUDGE;
        }
        if (arg.equals("C") || arg.equals("С")) {
            return DiceTypeEnum.COIN;
        }
        if (arg.equals("20")) {
            return DiceTypeEnum.D20;
        }
        return DiceTypeEnum.COMMON;
    }

    private Supplier<Integer> formDice(DiceTypeEnum type, String arg) {
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

    private void updateWithMetaData(SendSticker responseMessage, String chatId, Integer messageId) {
        responseMessage.setChatId(String.valueOf(chatId));
        if (messageId != null) {
            responseMessage.setReplyToMessageId(messageId);
        }
    }

    private void updateWithMetaData(SendMessage responseMessage, String chatId, Integer messageId) {
        responseMessage.setChatId(String.valueOf(chatId));
        if (messageId != null) {
            responseMessage.setReplyToMessageId(messageId);
        }
    }
}

package team.isaz.dmbot.domain.common.repository;

import org.springframework.stereotype.Repository;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Repository
public class RegexRepository {


    private final static String argsRegex = "( \\S{1,})*";
    private final Pattern diceThrowingPattern =
            regex("(|[1-9][0-9]*)d(2|C|c|3|F|4|6|8|10|12|20|D|d|100)($|[+-][1-9][0-9]*)");
    private final Pattern commandThrowPattern = command("/throw", false);
    private final Pattern commandFlipPattern = command("/flip", false);

    public Predicate<String> diceThrowing() {
        return diceThrowingPattern.asMatchPredicate();
    }

    public Predicate<String> commandThrow() {
        return commandThrowPattern.asMatchPredicate();
    }

    public Predicate<String> commandFlip() {
        return commandFlipPattern.asMatchPredicate();
    }

    private Pattern regex(String regex) {
        return Pattern.compile("^" + regex + "$");
    }

    private Pattern command(String command, boolean withArgs) {
        command = "(" + command + "|" + command + "@\\w{1,})";
        if (withArgs) {
            command += argsRegex;
        }
        return regex(command);
    }
}

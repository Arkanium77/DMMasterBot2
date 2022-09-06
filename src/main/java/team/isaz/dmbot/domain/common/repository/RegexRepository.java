package team.isaz.dmbot.domain.common.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Slf4j
@Repository
public class RegexRepository {


    private final static String argsRegex = "( \\S{1,})*";
    private final Pattern diceThrowingPattern =
            regex("(|[1-9][0-9]*)d(2|C|c|3|F|4|6|8|10|12|20|D|d|100)(|[+-][1-9][0-9]*)");
    private final Pattern commandThrowPattern = command("/throw", false);
    private final Pattern commandFlipPattern = command("/flip", false);
    private final Pattern commandRoflPattern = command("/rofl", false);
    private final Pattern commandLatePattern = command("/late", false);
    private final Pattern multiDiceThrowingPattern1 =
            regex("^((|[1-9][0-9]*)d(2|c|3|f|4|6|8|10|12|20|d|100)(|[+-][1-9][0-9]*) )*((|[1-9][0-9]*)d(2|c|3|f|4|6|8|10|12|20|d|100)(|[+-][1-9][0-9]*))$");
    private final Pattern multiDiceThrowingPattern2 =
            regex("((|[1-9][0-9]*)d(2|C|c|3|F|4|6|8|10|12|20|D|d|100) ){1,}(|[+-][1-9][0-9]*)");
    private final Pattern latePattern = regex(".*(опазд|опозд|задержу|задержим).*");
    private final Pattern manulPattern = regex(".*[мm]+[\\s ]*[аa]+[\\s ]*[нnh]+[\\s ]*[уyu]+[\\s ]*[лl].*");
    private final Pattern warPattern = regex(".*(ужасы войны|ужасывойны).*");
    private final Pattern coinPattern = regex(".*(монет|coin|золот|серебрян|медн).*");
    private final Pattern gracePattern = regex(".*(поздрав|грац|молодцы|молодец|молодцо).*");
    private final Pattern gastonFirstPattern = regex(".*(гастон|gaston).*");
    private final Pattern gastonSecondPattern = regex(".*(я.*не.*даром.*являюсь.*для.*всех.*примером|z.*yt.*lfhjv.*zdkz.cm.*lkz.*dct\\[.*ghbvthjv).*");

    public Predicate<String> diceThrowing() {
        return diceThrowingPattern.asMatchPredicate();
    }

    public Predicate<String> multiDiceThrowing1() {
        return multiDiceThrowingPattern1.asMatchPredicate();
    }

    public Predicate<String> multiDiceThrowing2() {
        return multiDiceThrowingPattern2.asMatchPredicate();
    }

    public Predicate<String> late() {
        return latePattern.asMatchPredicate();
    }

    public Predicate<String> manul() {
        return manulPattern.asMatchPredicate();
    }

    public Predicate<String> war() {
        return warPattern.asMatchPredicate();
    }

    public Predicate<String> coin() {
        return coinPattern.asMatchPredicate();
    }

    public Predicate<String> grace() {
        return gracePattern.asMatchPredicate();
    }

    public Predicate<String> gastonFirst() {
        return gastonFirstPattern.asMatchPredicate();
    }

    public Predicate<String> gastonSecond() {
        return gastonSecondPattern.asMatchPredicate();
    }

    public Predicate<String> commandThrow() {
        return commandThrowPattern.asMatchPredicate();
    }

    public Predicate<String> commandFlip() {
        return commandFlipPattern.asMatchPredicate();
    }

    public Predicate<String> commandRofl() {
        return commandRoflPattern.asMatchPredicate();
    }

    public Predicate<String> commandLate() {
        return commandLatePattern.asMatchPredicate();
    }

    @SuppressWarnings("MagicConstant")
    private Pattern regex(String regex, int... flags) {
        OptionalInt f = Arrays.stream(flags).reduce((f1, f2) -> f1 | f2).stream().findAny();
        if (f.isEmpty()) {
            return regex(regex);
        }
        return Pattern.compile("^" + regex + "$", f.getAsInt());
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

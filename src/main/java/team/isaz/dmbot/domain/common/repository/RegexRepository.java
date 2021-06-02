package team.isaz.dmbot.domain.common.repository;

import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Getter
@Repository
public class RegexRepository {
    private final Pattern diceThrowingPattern = Pattern
            .compile("[0-9]*d(2|C|c|3|F|4|6|8|10|12|20|D|d|100)[0-9^(+\\-)]*");

    public Predicate<String> diceThrowing() {
        return diceThrowingPattern.asMatchPredicate();
    }
}

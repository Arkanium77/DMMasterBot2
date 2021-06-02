package team.isaz.dmbot.domain.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class DataUtils {
    public static Optional<Integer> fetchInt(String s) {
        if (s != null) {
            try {
                return Optional.of(Integer.parseInt(s));
            } catch (NumberFormatException ignored) {
            }
        }
        return Optional.empty();
    }
}

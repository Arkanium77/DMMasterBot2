package team.isaz.dmbot.domain.common.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Slf4j
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

    public static String getName(User user) {
        if (user == null) {
            log.error("Caught null user");
            return "Unknown";
        }
        String name = user.getUserName();
        if (name == null || name.isBlank()) {
            name = getNullableString(user.getFirstName()) + " " + getNullableString(user.getLastName());
            name = name.strip();
        }
        return name;
    }

    public static String getNullableString(String string) {
        if (string == null) {
            return "";
        }
        return string;
    }
}

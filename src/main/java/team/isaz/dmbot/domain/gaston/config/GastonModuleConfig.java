package team.isaz.dmbot.domain.gaston.config;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GastonModuleConfig {
    private final List<String> baseFirst = Arrays.asList(
            """
                    Кто храбрей, чем Гастон?
                    Кто умней, чем Гастон?
                    Очень мощную шею имеет Гастон""",
            """
                    Это он наш Гастон
                    Остроумный Гастон
                    Нет на свете красивей людей, чем Гастон""",
            """
                    Всех сильнее Гастон
                    Всех умнее Гастон
                    И никто не укусит вас, так как Гастон""",
            """
                    Самый крепкий Гастон
                    Самый мягкий Гастон
                    И в плевках у нас тоже Гастон чемпион""",
            """
                    Самый славный Гастон
                    Самый главный Гастон
                    И никто так не носит костюм как Гастон"""
    );

    private final List<String> baseSecond = Arrays.asList(
            "Ах, что за парень Гастон!",
            "О, в десятку, Гастон!",
            "Вот он какой наш Гастон!"
    );

    public List<String> getFirst() {
        return new ArrayList<>(baseFirst);
    }

    public List<String> getSecond() {
        return new ArrayList<>(baseSecond);
    }

}

package team.isaz.dmbot.domain.late.config;

import org.springframework.context.annotation.Configuration;
import team.isaz.dmbot.domain.common.model.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class LateModuleConfig {
    List<String> base = Arrays.asList(
            "Будет через",
            "Опоздает на",
            "Задержится на",
            "Будет очень скоро! Всего через",
            "Уже на подходе. Осталось всего",
            "Мчится на всех парах! Будет через",
            "Грядёт! Грядёт! Всего-то надо подождать"
    );
    List<Pair<String, Integer>> time = Arrays.asList(
            Pair.of("мин", 60),
            Pair.of("час", 3600),
            Pair.of("ден", 86400),
            Pair.of("недел", 604800)
    );


    public List<String> get() {
        return new ArrayList<>(base);
    }

}

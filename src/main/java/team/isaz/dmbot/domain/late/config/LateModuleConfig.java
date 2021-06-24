package team.isaz.dmbot.domain.late.config;

import org.springframework.context.annotation.Configuration;

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

    public List<String> get() {
        return new ArrayList<>(base);
    }

}

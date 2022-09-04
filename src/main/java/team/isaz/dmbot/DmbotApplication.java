package team.isaz.dmbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import team.isaz.dmbot.configuration.StaticBeans;

@Slf4j
@SpringBootApplication
public class DmbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmbotApplication.class, args);
        log.info("This is {} bot.",
                StaticBeans.applicationContext().getBean(TelegramBot.class).getClass().getSimpleName());
    }

}

package team.isaz.dmbot.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StaticBeans {
    private static ApplicationContext applicationContext;

    public StaticBeans(ApplicationContext applicationContext) {
        StaticBeans.applicationContext = applicationContext;
    }

    public static ApplicationContext applicationContext() {
        return applicationContext;
    }
}

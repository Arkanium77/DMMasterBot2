package team.isaz.dmbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.SplittableRandom;

@Configuration
public class BeanConfig {

    @Bean
    public SplittableRandom random() {
        return new SplittableRandom();
    }
}

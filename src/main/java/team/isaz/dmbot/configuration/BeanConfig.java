package team.isaz.dmbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.isaz.dmbot.domain.common.repository.StringBasedRepository;
import team.isaz.dmbot.domain.late.config.LateModuleConfig;
import team.isaz.dmbot.domain.rofl.config.RoflModuleConfig;

import java.util.SplittableRandom;

@Configuration
public class BeanConfig {

    @Bean
    public SplittableRandom random() {
        return new SplittableRandom();
    }

    @Bean
    public StringBasedRepository roflBase(RoflModuleConfig base) {
        return new StringBasedRepository(base.get(), random());
    }

    @Bean
    public StringBasedRepository lateBase(LateModuleConfig base) {
        return new StringBasedRepository(base.get(), random());
    }
}

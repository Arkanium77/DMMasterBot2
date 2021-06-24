package team.isaz.dmbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.isaz.dmbot.domain.common.model.Random;
import team.isaz.dmbot.domain.common.repository.StringBasedRepository;
import team.isaz.dmbot.domain.grace.config.GraceModuleConfig;
import team.isaz.dmbot.domain.late.config.LateModuleConfig;
import team.isaz.dmbot.domain.rofl.config.RoflModuleConfig;

@Configuration
public class BeanConfig {

    @Bean
    public StringBasedRepository roflBase(RoflModuleConfig base, Random random) {
        return new StringBasedRepository(base.get(), random, 0);
    }

    @Bean
    public StringBasedRepository lateBase(LateModuleConfig base, Random random) {
        return new StringBasedRepository(base.get(), random, 0);
    }

    @Bean
    public StringBasedRepository graceBase(GraceModuleConfig base, Random random) {
        return new StringBasedRepository(base.get(), random, 2);
    }
}

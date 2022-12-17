package team.isaz.dmbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.isaz.dmbot.domain.common.model.Random;
import team.isaz.dmbot.domain.common.repository.StringBasedRepository;
import team.isaz.dmbot.domain.excuse.config.ExcuseModuleConfig;
import team.isaz.dmbot.domain.gaston.config.GastonModuleConfig;
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

    @Bean
    public StringBasedRepository gastonFirstBase(GastonModuleConfig base, Random random) {
        return new StringBasedRepository(base.getFirst(), random, 2);
    }

    @Bean
    public StringBasedRepository gastonSecondBase(GastonModuleConfig base, Random random) {
        return new StringBasedRepository(base.getSecond(), random, 2);
    }

    @Bean
    public StringBasedRepository excuseStartBase(ExcuseModuleConfig base, Random random) {
        return new StringBasedRepository(base.getStart(), random, 0);
    }

    @Bean
    public StringBasedRepository excuseReasonBase(ExcuseModuleConfig base, Random random) {
        return new StringBasedRepository(base.getReason(), random, 0);
    }

    @Bean
    public StringBasedRepository excuseAdditionBase(ExcuseModuleConfig base, Random random) {
        return new StringBasedRepository(base.getAddition(), random, 0);
    }

    @Bean
    public StringBasedRepository excuseEndBase(ExcuseModuleConfig base, Random random) {
        return new StringBasedRepository(base.getEnd(), random, 0);
    }
}

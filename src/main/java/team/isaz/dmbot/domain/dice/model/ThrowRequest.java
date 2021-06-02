package team.isaz.dmbot.domain.dice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.isaz.dmbot.domain.common.utils.DataUtils;

import java.util.function.Supplier;

@Getter
@AllArgsConstructor
public class ThrowRequest {
    private final int count;
    private final DiceTypeEnum type;
    private final Supplier<Integer> diceRoller;
    private final int modifier;

    public static ThrowRequestBuilder builder() {
        return new ThrowRequestBuilder();
    }

    @NoArgsConstructor
    public static class ThrowRequestBuilder {
        private int count;
        private DiceTypeEnum type;
        private Supplier<Integer> diceRoller;
        private int modifier;

        public ThrowRequestBuilder count(int count) {
            this.count = count;
            return this;
        }

        public ThrowRequestBuilder count(String count) {
            DataUtils.fetchInt(count)
                    .ifPresent(i -> this.count = i);
            return this;
        }

        public ThrowRequestBuilder type(DiceTypeEnum type) {
            this.type = type;
            return this;
        }

        public ThrowRequestBuilder dice(Supplier<Integer> dice) {
            this.diceRoller = dice;
            return this;
        }

        public ThrowRequestBuilder modifier(int modifier) {
            this.modifier = modifier;
            return this;
        }

        public ThrowRequestBuilder modifier(String modifier) {
            DataUtils.fetchInt(modifier)
                    .ifPresent(i -> this.modifier = i);
            return this;
        }

        public ThrowRequest build() {
            return new ThrowRequest(count, type, diceRoller, modifier);
        }
    }
}

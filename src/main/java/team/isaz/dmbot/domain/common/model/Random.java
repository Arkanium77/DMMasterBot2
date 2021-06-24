package team.isaz.dmbot.domain.common.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.SplittableRandom;

@Component
@RequiredArgsConstructor
public class Random {
    private final SplittableRandom random = new SplittableRandom();

    public int nextInt(int bound) {
        return nextInt(0, bound);
    }

    public int nextInt(int origin, int bound) {
        return random.nextInt(origin, bound);
    }

    public boolean chanceOf(int i) {
        if (i < 1) {
            return false;
        }
        int c = random.nextInt(i);
        return c == 0;
    }
}

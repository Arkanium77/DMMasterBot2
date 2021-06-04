package team.isaz.dmbot.domain.rofl.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import team.isaz.dmbot.domain.rofl.config.RoflBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class RoflRepository {
    private final List<String> roflBase = RoflBase.get();
    private final SplittableRandom random;
    private final Map<String, List<String>> currentRofl = new ConcurrentHashMap<>();

    public String findRofl(String id) {
        if (currentRofl.get(id) == null) {
            register(id);
        }
        if (currentRofl.get(id).isEmpty()) {
            refill(id);
        }
        List<String> rofls = currentRofl.get(id);
        int nextRoflIndex = random.nextInt(rofls.size());
        String nextRofl = rofls.get(nextRoflIndex);
        rofls.remove(nextRoflIndex);
        return nextRofl;
    }

    private void register(String id) {
        currentRofl.put(id, new ArrayList<>(roflBase));
    }

    private void refill(String id) {
        currentRofl.get(id).addAll(roflBase);
    }

}

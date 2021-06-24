package team.isaz.dmbot.domain.common.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class StringBasedRepository {
    private final List<String> base;
    private final SplittableRandom random;
    private final Map<String, List<String>> chats = new ConcurrentHashMap<>();

    public String find(String id) {
        if (chats.get(id) == null) {
            register(id);
        }
        if (chats.get(id).isEmpty()) {
            refill(id);
        }
        List<String> currentChat = chats.get(id);
        int nextRoflIndex = random.nextInt(currentChat.size());
        String nextRofl = currentChat.get(nextRoflIndex);
        currentChat.remove(nextRoflIndex);
        return nextRofl;
    }

    private void register(String id) {
        chats.put(id, new ArrayList<>(base));
    }

    private void refill(String id) {
        chats.get(id).addAll(base);
    }

}

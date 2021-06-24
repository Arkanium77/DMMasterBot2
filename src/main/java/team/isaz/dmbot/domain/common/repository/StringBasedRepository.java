package team.isaz.dmbot.domain.common.repository;

import lombok.RequiredArgsConstructor;
import team.isaz.dmbot.domain.common.model.Random;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class StringBasedRepository {
    private final List<String> base;
    private final Random random;
    private final long cooldownHours;
    private final Map<String, List<String>> chats = new ConcurrentHashMap<>();
    private final Map<String, OffsetDateTime> chatsCooldown = new ConcurrentHashMap<>();

    public Optional<String> find(String id) {
        if (chats.get(id) == null) {
            register(id);
        }

        if (cooldownHours > 0 && chatsCooldown.get(id) != null
                && chatsCooldown.get(id).isAfter(OffsetDateTime.now())) {
            return Optional.empty();
        }

        if (chats.get(id).isEmpty()) {
            refill(id);
        }
        List<String> currentChat = chats.get(id);
        int nextRoflIndex = random.nextInt(currentChat.size());
        String nextRofl = currentChat.get(nextRoflIndex);
        currentChat.remove(nextRoflIndex);
        if (cooldownHours > 0) {
            chatsCooldown.put(id, OffsetDateTime.now().plusHours(cooldownHours));
        }
        return Optional.of(nextRofl);
    }

    private void register(String id) {
        chats.put(id, new ArrayList<>(base));
    }

    private void refill(String id) {
        chats.get(id).addAll(base);
    }

}

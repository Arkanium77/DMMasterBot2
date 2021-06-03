package team.isaz.dmbot.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import team.isaz.dmbot.configuration.BotConfig;
import team.isaz.dmbot.domain.common.exception.NoNeedResponseException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DMBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final CommandSwitch switchService;

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && !update.getMessage().isReply()) {
            try {
                List<PartialBotApiMethod<Message>> response = switchService.fetchResponse(update.getMessage());
                response.forEach(this::executeForTrueType);
            } catch (NoNeedResponseException ignored) {
            }
        }

    }

    private void executeForTrueType(PartialBotApiMethod<Message> response) {
        try {
            if (response instanceof SendMessage) {
                var t = (SendMessage) response;
                execute(t);
                log.debug("Sent message \"{}\"", t.toString());
            } else if (response instanceof SendSticker) {
                var t = (SendSticker) response;
                execute(t);
                log.debug("Sent sticker \"{}\"", t.toString());
            } else if (response instanceof SendPhoto) {
                var t = (SendPhoto) response;
                execute(t);
                log.debug("Sent photo \"{}\" to {}", t.toString(), t.getChatId());
            } else {
                log.error("Trying to send message of unknown type ({})", response.getClass().getSimpleName());
            }
        } catch (TelegramApiException e) {
            log.error("Failed to send message due to error: {}", e.getMessage(), e);
        } catch (NoNeedResponseException ignored) {
        }
    }
}

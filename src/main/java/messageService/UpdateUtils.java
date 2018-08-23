package messageService;

import org.telegram.telegrambots.api.objects.Update;

public class UpdateUtils {
    public static String getUserId (Update update) {
        return Integer.toString(update.getMessage().getFrom().getId());
    }

    public static String getReplyToUserId (Update update) {
        return Integer.toString(update.getMessage().getReplyToMessage().getFrom().getId());
    }
}

package messageService;

import dbService.DBException;
import dbService.DBService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Service
public class VictorinaService {
    private DBService dbService = new DBService();
    private Map<String, Victorina> victorinas = new HashMap<>();

    public String startVictorina(String userId) {
        if (hasActiveVictrina(userId)) {
            return "Уже есть активная викторина";
        } else {
            victorinas.put(userId, new Victorina());
            return "Ок";
        }
    }

    public String finishVictorina(String userId) {
        if (!victorinas.containsKey(userId) || (victorinas.get(userId).getState() != 0)) {
            return "Нет активных викторин";
        } else {
            victorinas.get(userId).setState(1);
            return printVictorina(victorinas.get(userId));
        }
    }

    public String countVictorina(String userId) {
        if (!victorinas.containsKey(userId) || (victorinas.get(userId).getState() != 0)) {
            return "Нет активных викторин";
        } else {
            return printVictorina(victorinas.get(userId));
        }
    }

    public boolean hasActiveVictrina(String userId) {
        return victorinas.containsKey(userId) && (victorinas.get(userId).getState() == 0);
    }

    public boolean hasActiveVictrina(Integer userId) {
        String stUserId = Integer.toString(userId);
        return victorinas.containsKey(stUserId) && (victorinas.get(stUserId).getState() == 0);
    }

    public boolean executeUpdate(Update update) {

        if (update.hasMessage()
                && update.getMessage().hasText()
                && update.getMessage().isReply()
                && hasActiveVictrina(update.getMessage().getFrom().getId())) {
            if ("+1".equals(update.getMessage().getText())) {
                victorinas.get(UpdateUtils.getUserId(update))
                        .increaseCount(UpdateUtils.getReplyToUserId(update));
                return true;
            }
            if ("-1".equals(update.getMessage().getText())) {
                victorinas.get(UpdateUtils.getUserId(update))
                        .decreaseCount(UpdateUtils.getReplyToUserId(update));
                return true;
            }
            return false;
        }
        return false;
    }

    public String printVictorina(Victorina victorina) {
        Map<String, Integer> count = victorina.getCount();
        String result = "";
        for (Map.Entry<String, Integer> entry : count.entrySet()) {
            try {
                result = result + dbService.getUserName(entry.getKey()) + ": " + entry.getValue() + "\n";
            } catch (DBException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

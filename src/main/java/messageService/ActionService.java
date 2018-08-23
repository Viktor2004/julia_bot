package messageService;

import com.github.kevinsawicki.http.HttpRequest;
import networkService.NetworkUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class ActionService {
    @Autowired
    private VictorinaService victorinaService;

    private static final Logger log = Logger.getLogger(ActionService.class);
    final static private String ANEKDOT = "anekdot";
    final static private String START_VICTORINA = "victorina.start";
    final static private String FINISH_VICTORINA = "victorina.finish";
    final static private String COUNT_VICTORINA = "victorina.count";
    final static private String CONFUSE = "Не понимаю...";

    public String performAction(String action, String userId) {
        if (action.equals(ANEKDOT)) {
            return getJoke();
        } else if (action.equals(START_VICTORINA)) {
            return victorinaService.startVictorina(userId);
        } else if (action.equals(FINISH_VICTORINA)) {
            return victorinaService.finishVictorina(userId);
        }else if (action.equals(COUNT_VICTORINA)) {
            return victorinaService.countVictorina(userId);
        }
        log.error("Неизвестный тип действия");
        return CONFUSE;
    }

    private static String getJoke() {
        try {
            URL adress = new URL("http://umorili.herokuapp.com/api/random?num=1");
            NetworkUtils.getInstance().removeSocksProxy();
            String response = HttpRequest.get(adress).body();
            NetworkUtils.getInstance().setSocksProxy();

            response = response.substring(1, response.length() - 1);
            JSONObject jsonObj = new JSONObject(response);
            String clearResponse = jsonObj.getString("elementPureHtml");
            clearResponse = Jsoup.parse(clearResponse).text();
            return clearResponse;
        } catch (Exception ex) {
            log.error(ex);
            return CONFUSE;
        }
    }
}

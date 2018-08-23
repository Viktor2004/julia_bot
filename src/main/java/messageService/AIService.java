package messageService;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import core.ConfigHelper;
import networkService.NetworkUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIService {
    private static final Logger log = Logger.getLogger(AIService.class);
    @Autowired
    private ActionService actionService;

    public String sendToAi(String text, String userId) {
        AIConfiguration config = new AIConfiguration(ConfigHelper.getInstance().getAIToken());
        AIDataService service = new AIDataService(config, null);
        AIRequest request = new AIRequest();
        request.setQuery(text);
        AIResponse response = null;
        try {
            NetworkUtils.getInstance().removeSocksProxy();
            response = service.request(request);
            NetworkUtils.getInstance().setSocksProxy();
        } catch (AIServiceException e) {
            log.error(e);
        }
        if (response != null && !response.getResult().getAction().equals("input.unknown")) {
            return actionService.performAction(response.getResult().getAction(), userId);
        }
        return response != null ? response.getResult().getFulfillment().getSpeech() : null;
    }
}

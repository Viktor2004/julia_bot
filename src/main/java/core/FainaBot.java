package core;

import dbService.DBException;
import dbService.DBService;
import messageService.AIService;
import messageService.Agro;
import messageService.Answer;
import messageService.VictorinaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.ChatMember;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FainaBot extends TelegramLongPollingBot {

    @Autowired
    private AIService aiService;
    @Autowired
    private VictorinaService victorinaService;
    private static final Logger log = Logger.getLogger(FainaBot.class);
    private Long adminChat = ConfigHelper.getInstance().getAdminChatId();
    private final static Integer OLEG_ID = ConfigHelper.getInstance().getOlegId();
    private final static Integer ADMIN_ID = ConfigHelper.getInstance().getAdminId();
    private String botUsername = ConfigHelper.getInstance().getBotName();
    private String botToken = ConfigHelper.getInstance().getBotToken();
    private Agro agro = new Agro();
    private Set<String> badWords = ConfigHelper.getInstance().getBadWords();
    private DBService dbService = new DBService();

    public FainaBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    public FainaBot() {
        super(ApiContext.getInstance(DefaultBotOptions.class));
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(this).isRunning();
            log.info("Julia started");
        } catch (TelegramApiRequestException e) {
            log.error(e);
        }
    }

    public void onUpdateReceived(Update update) {
        log.info(update);
        // We check if the update has a message and the message has text
        alivePing(update);
        try {
            executeCommand(update);
        } catch (DBException e) {
            log.error(e);
        }

        //добавляем сообщение в базу
        if (update.hasMessage() && update.getMessage().hasText() && !update.getMessage().isCommand()
                && update.getMessage().getChat().getId().equals(adminChat)
                ) {
            try {
                dbService.addMessage(update.getMessage(), hasCurseWords(update.getMessage().getText()));
            } catch (DBException e) {
                log.error(e);
            }
        }

//Тролим Олега
        if (update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getFrom().getId().equals(OLEG_ID)) {
            // Set variables
            if (hasCurseWords(update.getMessage().getText())) {
                sendAnswer(update, agro.agro());
            }
        }

        //Отправка в AI
        if (update.hasMessage() && update.getMessage().hasText()
                && isForJulia(update.getMessage().getText())) {
            String question = update.getMessage().getText().replace("@Julia4O_bot", "");
            sendAnswer(update, aiService.sendToAi(question, update.getMessage().getFrom().getId().toString()));
        }
        //работа с викторинами
        if (victorinaService.executeUpdate(update)) {
            sendAnswer(update, "Ок");
        }
    }

    private boolean isForJulia(String message) {
        return
                message.startsWith("@Julia4O_bot")
                        || message.startsWith(" @Julia4O_bot")
                        || message.startsWith("Юля,")
                ;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    private boolean hasCurseWords(String request) {
        String[] requestWords = request.split("[,;:.\"«»!?\\s]+");
        for (String word : requestWords) {
            if (badWords.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void alivePing(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()
                && update.getMessage().getFrom().getId().equals(ADMIN_ID)
                && update.getMessage().getChat().isUserChat()) {
            if (hasCurseWords(update.getMessage().getText())) {
                sendAnswer(update, "Ругань");
            } else {
                sendAnswer(update, "I'm allive");
            }

        }

    }

    private void executeCommand(Update update) throws DBException {

//        if (!isInAdminChat(update.getMessage().getFrom().getId())) {
//            sendAnswer(update, "Извините, но я вас не знаю.");
//            return;
//        }

        if (update.hasMessage() && update.getMessage().isCommand()) {

            if (update.getMessage().getText().contains("/mystat")) {
                sendAnswer(update,
                        "Ваш счет : " +
                                dbService.countUserMessages(update.getMessage().getFrom().getId().toString()));
                return;
            }

            if (update.getMessage().getText().contains("/chatstat")) {
                List<Answer> answer = new ArrayList<>();
                List<String> users = dbService.getUserIds();
                for (String user : users) {
                    String userName = dbService.getUserName(user);
                    Integer messagesCount = dbService.countUserMessages(user);
                    answer.add(new Answer(userName, messagesCount));
                }
                List<String> result = answer.stream()
                        .sorted((s1, s2) -> s2.getMessageNumber() - s1.getMessageNumber())
                        .map(s -> s.getUserName() + ": " + s.getMessageNumber())
                        .collect(Collectors.toList());
                sendAnswer(update,
                        result.stream().reduce((s1, s2) -> s1 + "\r\n" + s2).get());
                return;
            }

            if (update.getMessage().getText().contains("/cursechatstat")) {
                List<Answer> answer = new ArrayList<>();
                List<String> users = dbService.getUserIds();
                for (String user : users) {
                    Integer messagesCount = dbService.countCurseUserMessages(user);
                    String userName = dbService.getUserName(user);
                    answer.add(new Answer(userName, messagesCount));
                }
                List<String> result = answer.stream()
                        .sorted((s1, s2) -> s2.getMessageNumber() - s1.getMessageNumber())
                        .map(s -> s.getUserName() + ": " + s.getMessageNumber())
                        .collect(Collectors.toList());
                sendAnswer(update,
                        result.stream().reduce((s1, s2) -> s1 + "\r\n" + s2).get());
            }
        }

    }

    private boolean isInAdminChat(Integer userId) {
        GetChatMember isChatMember = new GetChatMember();
        isChatMember.setChatId(adminChat);
        isChatMember.setUserId(userId);
        ChatMember chatMember;
        try {
            chatMember = getChatMember(isChatMember);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return false;
        }
        if (chatMember.getUser() != null
                && (chatMember.getStatus().equals("member")
                || chatMember.getStatus().equals("creator")
                || chatMember.getStatus().equals("administrator"))

                ) {
            return true;
        } else {
            return false;
        }
    }

    private void sendAnswer(Update update, String answer) {
        log.info("Julia says : " + answer);
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(update.getMessage().getChatId())
                .setText(answer);
        try {
            sendMessage(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            log.error(e);
        }
    }
}



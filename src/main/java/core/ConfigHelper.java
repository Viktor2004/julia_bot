package core;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

public class ConfigHelper {
    private String proxyHost;
    private String proxyPort;
    private String proxyUserName;
    private String proxyPassword;
    private String botName;
    private String botToken;
    private Long adminChatId;
    private Integer adminId;
    private Integer olegId;
    private String aiToken;
    private Set<String> badWords = new HashSet<>();

    private static ConfigHelper ourInstance = new ConfigHelper();

    public static ConfigHelper getInstance() {
        return ourInstance;
    }

    private ConfigHelper() {
    }

    public void readConfig() {
        Properties configFile;
        {
            configFile = new java.util.Properties();
            try {
                configFile.load(this.getClass().getClassLoader().
                        getResourceAsStream("config.cfg"));
            } catch (Exception eta) {
                eta.printStackTrace();
            }
        }
        this.proxyHost = configFile.getProperty("proxyHost");
        this.proxyPort = configFile.getProperty("proxyPort");
        this.proxyUserName = configFile.getProperty("proxyUserName");
        this.proxyPassword = configFile.getProperty("proxyPassword");
        this.botName = configFile.getProperty("botName");
        this.botToken = configFile.getProperty("botToken");
        this.adminChatId = Long.parseLong(configFile.getProperty("adminChatId"));
        this.adminId = Integer.parseInt(configFile.getProperty("adminId"));
        this.olegId = Integer.parseInt(configFile.getProperty("olegId"));
        this.aiToken = configFile.getProperty("aiToken");
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream vocabStream = classLoader.getResourceAsStream("badwords");
        try (Scanner scanner = new Scanner(vocabStream, "UTF-8")) {

            while (scanner.hasNextLine()) {
                badWords.add(scanner.nextLine());
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("some output");
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getBotName() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public Long getAdminChatId() {
        return adminChatId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public Integer getOlegId() {
        return olegId;
    }

    public String getAIToken() {
        return aiToken;
    }

    public Set<String> getBadWords() {
        return badWords;
    }
}

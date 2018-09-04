package networkService;

import core.ConfigHelper;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class NetworkUtils {
    private static NetworkUtils ourInstance = new NetworkUtils();

    public static NetworkUtils getInstance() {
        return ourInstance;
    }

    private NetworkUtils() {
    }

    ConfigHelper configHelper = ConfigHelper.getInstance();
    private String PROXY_HOST = configHelper.getProxyHost()/* proxy host */;
    private String PROXY_PORT = configHelper.getProxyPort() /* proxy port */;
    private String PROXY_USER = configHelper.getProxyUserName()/* proxy user */;
    private String PROXY_PASSWORD = configHelper.getProxyPassword() /* proxy password */;

    public void setSocksProxy() {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(PROXY_USER, PROXY_PASSWORD.toCharArray());
            }
        });
        System.setProperty("socksProxyHost", PROXY_HOST);
        System.setProperty("socksProxyPort", PROXY_PORT);

    }

    public void removeSocksProxy() {
        System.clearProperty("socksProxyHost");
        System.clearProperty("socksProxyPort");
    }
}

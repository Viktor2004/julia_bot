package core;

import networkService.NetworkUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.ApiContextInitializer;


@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("messageService")
@ComponentScan("core")
public class Executor {
    public static void main(String[] args) {
        ConfigHelper.getInstance().readConfig();
        ApiContextInitializer.init();
        NetworkUtils.getInstance().setSocksProxy();
        ApplicationContext applicationContext =

                SpringApplication.run(Executor.class, args);

        for (String name : applicationContext.getBeanDefinitionNames()) {

            System.out.println(name);

        }
    }
}

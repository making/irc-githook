package githook;

import lombok.extern.slf4j.Slf4j;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class AppConfig {
    @Autowired
    IrcBotProperties ircBotProperties;

    @Bean
    ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    PircBotX pircBotX() throws Exception {
        log.info("Using {}", ircBotProperties);
        final CountDownLatch latch = new CountDownLatch(1);
        org.pircbotx.Configuration configuration = new org.pircbotx.Configuration.Builder()
                .setName(ircBotProperties.getName())
                .setLogin(ircBotProperties.getLogin())
                .setAutoNickChange(ircBotProperties.isAutoNickChange())
                .setCapEnabled(ircBotProperties.isCapEnabled())
                .setServerHostname(ircBotProperties.getServerHostname())
                .setServerPort(ircBotProperties.getServerPort())
                .addAutoJoinChannel(ircBotProperties.getJoinChannel())
                .setEncoding(ircBotProperties.getEncoding())
                .setServerPassword(ircBotProperties.getServerPassword())
                .addListener(new ListenerAdapter() {
                    @Override
                    public void onJoin(JoinEvent event) throws Exception {
                        latch.countDown();
                    }
                })
                .buildConfiguration();
        final PircBotX bot = new PircBotX(configuration);
        executorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    bot.startBot();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        latch.await();
        return bot;
    }
}

package githook;

import org.pircbotx.PircBotX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotService {

    @Autowired
    IrcBotProperties ircBotProperties;

    @Autowired
    PircBotX botX;

    public void sendMessage(String message) {
        botX.sendIRC().message(ircBotProperties.getJoinChannel(), message);
    }
}

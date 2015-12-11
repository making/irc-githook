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

    public void synchronizedSend(MessageSend messageSend) {
        synchronized (botX) {
            messageSend.process(new MessageSender());
        }
    }

    public class MessageSender {
        public void sendMessage(String message) {
            botX.sendIRC().message(ircBotProperties.getJoinChannel(), message);
        }
    }

    public interface MessageSend {
        void process(MessageSender sender);
    }
}

package githook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "irc.bot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IrcBotProperties {
    private String name = "PircBotX";
    private String login = "LQ";
    private boolean autoNickChange = true;
    private boolean capEnabled = true;
    private String serverHostname = "10.68.254.64";
    private int serverPort = 6667;
    private String joinChannel = "#nextgen";
}

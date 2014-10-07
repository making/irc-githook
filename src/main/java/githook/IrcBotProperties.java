package githook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import java.nio.charset.Charset;

@Component
@ConfigurationProperties(prefix = "irc.bot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IrcBotProperties {
    @Size(min = 1, max = 9)
    private String name = "GitHook";
    @NotEmpty
    private String login = "GH";
    private boolean autoNickChange = true;
    private boolean capEnabled = true;
    @NotEmpty
    private String serverHostname = "localhost";
    private int serverPort = 6667;
    @NotEmpty
    private String joinChannel = "#githook";
    private Charset encoding = Charset.forName("ISO-2022-JP");
}

package githook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import am.ik.gh.webhooks.EnableGitHubWebhooks;

@RestController
@SpringBootApplication
@EnableGitHubWebhooks
public class App {
    @Autowired
    BotService botService;

    @RequestMapping("/test")
    String foo(@RequestParam("msg") final String message) {
        botService.synchronizedSend(new BotService.MessageSend() {
            @Override
            public void process(BotService.MessageSender sender) {
                sender.sendMessage(message);
            }
        });
        return "OK";
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

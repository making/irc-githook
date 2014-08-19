package githook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pircbotx.PircBotX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@EnableAutoConfiguration
@ComponentScan
public class App {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IrcBotProperties ircBotProperties;
    @Autowired
    PircBotX botX;

    @RequestMapping("/test")
    String foo(@RequestParam("msg") String message) {
        botX.sendIRC().message(ircBotProperties.getJoinChannel(), message);
        return "OK";
    }

    @RequestMapping("/webHook")
    String webHook(WebHookRequest webHookRequest) throws IOException {
        Payload payload = objectMapper.readValue(webHookRequest.getPayload(), Payload.class);
        // build message
        botX.sendIRC().message(ircBotProperties.getJoinChannel(), "pusher:" + payload.getPusher().getName());
        botX.sendIRC().message(ircBotProperties.getJoinChannel(), "repo:" + payload.getRepository().getUrl());
        for (Commit commit : payload.getCommits()) {
            botX.sendIRC().message(ircBotProperties.getJoinChannel(), "\tcommiter:" + commit.getAuthor().getName() +
                    "\ttimestamp:" + commit.getTimestamp() +
                    "\tmessage:" + commit.getMessage() +
                    "\turl:" + commit.getUrl());
        }
        return "OK";
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebHookRequest {
        private String payload;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private User pusher;
        private String ref;
        private List<Commit> commits;
        private Repository repository;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String name;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Commit {
        private String id;
        private String message;
        private String timestamp;
        private String url;
        private List<String> added;
        private List<String> removed;
        private List<String> modified;
        private User author;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Repository {
        private String name;
        private String url;
        private String description;
        private int watchers;
        private int forks;
        private boolean private_;
        private User owner;

        public boolean isPrivate() {
            return this.private_;
        }

        public void setPrivate(boolean private_) {
            this.private_ = private_;
        }
    }
}
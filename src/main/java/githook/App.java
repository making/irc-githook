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
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @RequestMapping({"/webhook", "/webHook"})
    String webHook(WebHookRequest webHookRequest) throws IOException {
        Payload payload = objectMapper.readValue(webHookRequest.getPayload(), Payload.class);
        // build message
        String ref = payload.getRef();
        String branch = (ref != null) ? ref.replace("refs/heads/", "") : payload.getRepository().getDefault_branch();
        String url = payload.getRepository().getHtml_url();
        if (!Objects.equals(branch, payload.getRepository().getDefault_branch())) {
            url = url + "/tree/" + branch;
        }
        botX.sendIRC().message(ircBotProperties.getJoinChannel(), "user:" + payload.getPusher().getLogin());
        botX.sendIRC().message(ircBotProperties.getJoinChannel(), "url:" + url);
        botX.sendIRC().message(ircBotProperties.getJoinChannel(), "ref:" + ref);

        for (Commit commit : payload.getCommits()) {
            botX.sendIRC().message(ircBotProperties.getJoinChannel(), "\tauthor:" + commit.getAuthor().getName() +
                    "\ttimestamp:" + commit.getTimestamp() +
                    "\turl:" + commit.getHtml_url() +
                    "\tmessage:" + commit.getMessage());
        }
        return "OK";
    }


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
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
        private SiteUser pusher;
        private String ref;
        private List<Commit> commits;
        private Repository repository;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GitUser {
        private String name;
        private String email;
        private Date date;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SiteUser {
        private String login;
        private String email;
        private String type;
        private boolean site_admin;
        private Date created_at;
        private String url;
        private String html_url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Commit {
        private String id;
        private String message;
        private String timestamp;
        private List<String> added;
        private List<String> removed;
        private List<String> modified;
        private GitUser author;
        private GitUser committer;
        private String url;
        private String html_url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Repository {
        private String name;
        private String full_name;
        private String description;
        private int watchers;
        private int forks;
        private boolean private_;
        private String default_branch;
        private SiteUser owner;
        private int forks_count;
        private int watchers_count;
        private String url;
        private String http_url;
        private String clone_url;
        private String html_url;

        public boolean isPrivate() {
            return this.private_;
        }

        public void setPrivate(boolean private_) {
            this.private_ = private_;
        }
    }
}

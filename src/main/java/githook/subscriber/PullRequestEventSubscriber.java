package githook.subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import am.ik.gh.webhooks.pullrequest.PullRequestEventPayload;
import githook.BotService;

@Component
public class PullRequestEventSubscriber {
    @Autowired
    BotService botService;

    @EventListener
    public void subscribe(PullRequestEventPayload payload) {
        botService.sendMessage("==== "
                + StringUtils.capitalize(payload.getAction())
                + " PullRequest ====");
        botService.sendMessage("repository: "
                + payload.getRepository().getFullName());
        botService.sendMessage("title: " + payload.getPullRequest().getTitle());
        botService.sendMessage("owner:"
                + payload.getPullRequest().getUser().getLogin());
        botService.sendMessage("url:" + payload.getPullRequest().getHtmlUrl());
    }
}

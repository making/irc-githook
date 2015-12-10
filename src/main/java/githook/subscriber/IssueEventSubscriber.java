package githook.subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import am.ik.gh.webhooks.issues.IssuesEventPayload;
import githook.BotService;

@Component
public class IssueEventSubscriber {
    @Autowired
    BotService botService;

    @EventListener
    public void subscribe(IssuesEventPayload payload) {
        botService.sendMessage("==== "
                + StringUtils.capitalize(payload.getAction()) + " Issue ====");
        botService.sendMessage("repository: "
                + payload.getRepository().getFullName());
        botService.sendMessage("title: " + payload.getIssue().getTitle());
        botService.sendMessage("owner:"
                + payload.getIssue().getUser().getLogin());
        botService.sendMessage("url:" + payload.getIssue().getHtmlUrl());
    }
}

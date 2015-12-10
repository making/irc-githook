package githook.subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import am.ik.gh.webhooks.issuecomment.IssueCommentEventPayload;
import githook.BotService;

@Component
public class IssueCommentEventSubscriber {
    @Autowired
    BotService botService;

    @EventListener
    public void subscribe(IssueCommentEventPayload payload) {
        botService.sendMessage("==== "
                + StringUtils.capitalize(payload.getAction())
                + " Issue Comment ====");
        botService.sendMessage("user:"
                + payload.getComment().getUser().getLogin());
        botService.sendMessage("repository: "
                + payload.getRepository().getFullName());
        botService.sendMessage("title: " + payload.getIssue().getTitle());
        botService.sendMessage("url:" + payload.getComment().getHtmlUrl());
    }
}

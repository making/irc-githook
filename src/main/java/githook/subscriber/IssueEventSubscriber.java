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
    public void subscribe(final IssuesEventPayload payload) {
        botService.synchronizedSend(new BotService.MessageSend() {
            @Override
            public void process(BotService.MessageSender sender) {
                sender.sendMessage("==== "
                        + StringUtils.capitalize(payload.getAction())
                        + " Issue ====");
                sender.sendMessage("repository: "
                        + payload.getRepository().getFullName());
                sender.sendMessage("title: " + payload.getIssue().getTitle());
                sender.sendMessage("owner:"
                        + payload.getIssue().getUser().getLogin());
                sender.sendMessage("url:" + payload.getIssue().getHtmlUrl());
            }
        });
    }
}

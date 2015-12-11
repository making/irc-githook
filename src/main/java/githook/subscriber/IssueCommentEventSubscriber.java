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
    public void subscribe(final IssueCommentEventPayload payload) {
        botService.synchronizedSend(new BotService.MessageSend() {
            @Override
            public void process(BotService.MessageSender sender) {
                sender.sendMessage("==== "
                        + StringUtils.capitalize(payload.getAction())
                        + " Issue Comment ====");
                sender.sendMessage("repository: "
                        + payload.getRepository().getFullName());
                sender.sendMessage("title: " + payload.getIssue().getTitle());
                sender.sendMessage("owner:"
                        + payload.getIssue().getUser().getLogin());
                sender.sendMessage("commented by:"
                        + payload.getComment().getUser().getLogin());
                sender.sendMessage("url:" + payload.getComment().getHtmlUrl());
                if ("created".equals(payload.getAction())) {
                    sender.sendMessage("comment:"
                            + payload.getComment().getBody());
                }
            }
        });

    }
}

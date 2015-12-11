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
    public void subscribe(final PullRequestEventPayload payload) {
        botService.synchronizedSend(new BotService.MessageSend() {
            @Override
            public void process(BotService.MessageSender sender) {
                sender.sendMessage("==== "
                        + StringUtils.capitalize(payload.getAction())
                        + " PullRequest ====");
                sender.sendMessage("repository: "
                        + payload.getRepository().getFullName());
                sender.sendMessage("title: "
                        + payload.getPullRequest().getTitle());
                sender.sendMessage("owner:"
                        + payload.getPullRequest().getUser().getLogin());
                sender.sendMessage("url:"
                        + payload.getPullRequest().getHtmlUrl());
            }
        });

    }
}

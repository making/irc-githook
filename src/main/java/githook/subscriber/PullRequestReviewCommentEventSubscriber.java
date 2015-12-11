package githook.subscriber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import am.ik.gh.webhooks.pullrequestreviewcomment.PullRequestReviewCommentEventPayload;
import githook.BotService;

@Component
public class PullRequestReviewCommentEventSubscriber {
    @Autowired
    BotService botService;

    @EventListener
    public void subscribe(final PullRequestReviewCommentEventPayload payload) {
        botService.synchronizedSend(new BotService.MessageSend() {
            @Override
            public void process(BotService.MessageSender sender) {
                sender.sendMessage("==== "
                        + StringUtils.capitalize(payload.getAction())
                        + " Review Comment ====");
                sender.sendMessage("repository: "
                        + payload.getRepository().getFullName());
                sender.sendMessage("title: "
                        + payload.getPullRequest().getTitle());
                sender.sendMessage("owner:"
                        + payload.getPullRequest().getUser().getLogin());
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

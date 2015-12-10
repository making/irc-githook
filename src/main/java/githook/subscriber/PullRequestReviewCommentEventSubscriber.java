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
    public void subscribe(PullRequestReviewCommentEventPayload payload) {
        botService.sendMessage("==== "
                + StringUtils.capitalize(payload.getAction())
                + " Review Comment ====");
        botService.sendMessage("user:"
                + payload.getComment().getUser().getLogin());
        botService.sendMessage("repository: "
                + payload.getRepository().getFullName());
        botService.sendMessage("title: " + payload.getPullRequest().getTitle());
        botService.sendMessage("url:" + payload.getComment().getHtmlUrl());
    }
}
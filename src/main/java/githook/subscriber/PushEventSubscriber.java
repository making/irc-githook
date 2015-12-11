package githook.subscriber;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import am.ik.gh.webhooks.push.PushEventPayload;
import githook.BotService;

@Component
public class PushEventSubscriber {
    @Autowired
    BotService botService;

    @EventListener
    public void subscribe(final PushEventPayload payload) {
        botService.synchronizedSend(new BotService.MessageSend() {
            @Override
            public void process(BotService.MessageSender sender) {
                sender.sendMessage("==== Push ====");
                String ref = payload.getRef();
                String branch = (ref != null) ? ref.replace("refs/heads/", "")
                        : payload.getRepository().getDefaultBranch();
                String url = payload.getRepository().getHtmlUrl();
                if (!Objects.equals(branch, payload.getRepository()
                        .getDefaultBranch())) {
                    url = url + "/tree/" + branch;
                }
                sender.sendMessage("user:" + payload.getPusher().getEmail());
                sender.sendMessage("url:" + url);
                sender.sendMessage("ref:" + ref);

                for (PushEventPayload.Commit commit : payload.getCommits()) {
                    sender.sendMessage("\tauthor:"
                            + commit.getAuthor().getName() + "\ttimestamp:"
                            + commit.getTimestamp() + "\turl:"
                            + commit.getUrl() + "\tmessage:"
                            + commit.getMessage());
                }
            }
        });
    }
}

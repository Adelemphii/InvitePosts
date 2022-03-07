package me.adelemphii.inviteposts.commands;

import me.adelemphii.inviteposts.objects.Groups;
import me.adelemphii.inviteposts.utility.Utility;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.WebhookMessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PostCommand implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        if(event.getMessageAuthor().isBotUser()) return;
        if(event.getMessageAuthor().isWebhook()) return;
        Server server;
        if(event.getServer().isEmpty()) return;
        server = event.getServer().get();

        List<String> args = Arrays.asList(event.getMessage().getContent().split(" "));
        if(args.isEmpty()) return;

        Message message = event.getMessage();

        System.out.println(event.getMessage());

        if(!args.get(0).startsWith("!")) {
            return;
        }

        if(args.get(0).equalsIgnoreCase("!post")) {
            if(args.size() < 4) {
                message.reply("Invalid command usage! ``!post <url> <channel> <sendInvites>``");
                return;
            }
            try {
                if(server.getTextChannelById(args.get(2)).isEmpty()) {
                    message.reply("Invalid command usage! ``!post <url> <channel> <sendInvites>`` (Make sure the channel is in this server!)");
                    return;
                }
                TextChannel channel = server.getTextChannelById(args.get(2)).get();

                boolean sendInvites = args.get(3).equalsIgnoreCase("yes") || args.get(3).equalsIgnoreCase("true");

                if(!runPost(args.get(1).replace("<", "").replace(">", ""), message, channel, sendInvites)) {
                    message.reply(":( Something went wrong...");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                message.reply(":( Something went wrong...");
            }
        }
    }

    private boolean runPost(String url, Message message, TextChannel channel, boolean sendInvites) throws ExecutionException, InterruptedException {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            message.reply("That is not a valid URL!");
            return true;
        }

        List<Object> objects = Utility.scrapeInformation(url);
        if(objects == null) {
            return false;
        }
        EmbedBuilder builder = (EmbedBuilder) objects.get(0);
        if(builder == null) {
            return false;
        }
        Message sent = channel.sendMessage(builder).get();

        message.reply("Sent! " + sent.getLink());
        if(sendInvites) {
            @SuppressWarnings("unchecked")
            ArrayList<Groups> groups = (ArrayList<Groups>) objects.get(1);
            runInvite(message, builder, groups);
        }

        return true;
    }

    private void runInvite(Message message, EmbedBuilder builder, List<Groups> invitations) {
        for(Groups group : invitations) {
            WebhookMessageBuilder webhookBuilder = new WebhookMessageBuilder();
            webhookBuilder.addEmbed(builder);
            webhookBuilder.send(message.getApi(), group.getWebhook());
        }
        message.reply("Sent invites to: " + invitations);
    }
}

package me.adelemphii.inviteposts.instances;

import me.adelemphii.inviteposts.Core;
import me.adelemphii.inviteposts.commands.PostCommand;
import me.adelemphii.inviteposts.utility.Configuration;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordBot {

    private Configuration config = Core.config;

    private ScheduledExecutorService statusRunnable;

    // Discord API
    private final DiscordApi api;

    public DiscordBot() {
        api = new DiscordApiBuilder()
                .setToken(config.getDiscordBotToken())
                .login().join();

        registerListeners(api);
        startActivityUpdateTask();
    }

    public void start() {
        System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
    }

    public void stop() {
        api.disconnect();
        statusRunnable.shutdown();
    }

    private void registerListeners(DiscordApi api) {
        api.addListener(new PostCommand());
    }

    public DiscordApi getApi() {
        return api;
    }

    private void startActivityUpdateTask() {
        Runnable statusRunnable = () -> {
            List<String> channels = List.of(
                    "Ur mom lol",
                    "Ayoooooooo",
                    "LORD OF THE CRAFT",
                    "UR SO POGG!"
            );

            double randomIndex = Math.floor(Math.random() * (channels.size() - 1) + 1);

            api.updateActivity(ActivityType.WATCHING, channels.get((int) randomIndex));

        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(statusRunnable, 0, 10, TimeUnit.SECONDS);

        this.statusRunnable = executor;
    }

}


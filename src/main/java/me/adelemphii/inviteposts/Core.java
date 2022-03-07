package me.adelemphii.inviteposts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.adelemphii.inviteposts.instances.DiscordBot;
import me.adelemphii.inviteposts.utility.Configuration;

import java.io.InputStream;

public class Core {

    public static Configuration config;
    public static DiscordBot discordBot;

    public static String url = "https://www.lordofthecraft.net/forums/topic/210537-hi-feel-free-to-ignore-this/";

    public static void main(String[] args) {
        loadConfiguration();
        discordBot = new DiscordBot();
        discordBot.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            discordBot.stop();
            System.out.println();

            System.out.println("Shutting down...");
        }, "Shutdown-thread"));
    }

    private static void loadConfiguration() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream is = loader.getResourceAsStream("config.yml");

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            config = mapper.readValue(is, Configuration.class);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Unable to load Configuration ... Exiting.");
            System.exit(1);
        }
    }
}

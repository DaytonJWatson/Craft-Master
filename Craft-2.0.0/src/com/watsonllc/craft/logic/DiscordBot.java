package com.watsonllc.craft.logic;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Config;

/**
 * DiscordBot class facilitates communication between the Minecraft server and a Discord bot.
 * It listens to player events (join, quit, death, chat) and sends corresponding messages to Discord channels.
 */
@SuppressWarnings("deprecation")
public class DiscordBot {

    // Toggle for outputting message send logs to the server console.
    private static final boolean OUTPUT = false;

    /**
     * Runs the provided task asynchronously using Bukkit's scheduler.
     * 
     * @param task The task to be executed asynchronously.
     */
    private static void runAsync(Runnable task) {
        new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        }.runTaskAsynchronously(Main.instance);
    }

    /**
     * Sends a death message to Discord when a player dies.
     * 
     * @param event The PlayerDeathEvent containing information about the player's death.
     */
    public static void deathMessage(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            runAsync(() -> DiscordBot.sendMessage("[" + Utils.currentDate() + "] " + event.getDeathMessage(), "chat"));
        }
    }

    /**
     * Sends a quit message to Discord when a player leaves the server.
     * 
     * @param event The PlayerQuitEvent triggered when a player disconnects.
     */
    public static void quitMessage(PlayerQuitEvent event) {
        runAsync(() -> DiscordBot.sendMessage("[" + Utils.currentDate() + "] " + event.getPlayer().getName() + " left the server", "chat"));
    }

    /**
     * Sends a join message to Discord when a player joins the server.
     * Handles first-time joins differently by sending a welcome message.
     * 
     * @param event The PlayerJoinEvent triggered when a player connects.
     */
    public static void joinMessage(PlayerJoinEvent event) {
        runAsync(() -> {
            if (!event.getPlayer().hasPlayedBefore()) {
                sendMessage(event.getPlayer().getName() + " has joined for the first time!", "chat");
            }
            DiscordBot.sendMessage("[" + Utils.currentDate() + "] " + event.getPlayer().getName() + " joined the server", "chat");
        });
    }

    /**
     * Sends a chat message to Discord when a player sends a message in-game.
     * 
     * @param event The AsyncPlayerChatEvent triggered during chat.
     */
    public static void chatMessage(AsyncPlayerChatEvent event) {
        runAsync(() -> DiscordBot.sendMessage("[" + Utils.currentDate() + "] " + event.getPlayer().getName() + " >> " + event.getMessage(), "chat"));
    }

    /**
     * Sends a message to a specific Discord channel by making an HTTP POST request.
     * 
     * @param message The message to send.
     * @param channel The target channel (e.g., "chat", "griefReport", "playerReport").
     */
    public static void sendMessage(String message, String channel) {
        // Retrieve the Discord server address and relevant channel ID from the configuration.
        String serverAddress = Config.getString("discordBot.serverAddress");
        String channelID = switch (channel) {
            case "chat" -> Config.getString("discordBot.chatChannel");
            case "griefReport" -> Config.getString("discordBot.reportGriefChannel");
            case "playerReport" -> Config.getString("discordBot.reportPlayerChannel");
            default -> null;
        };

        // Log an error if the channel ID is not configured.
        if (channelID == null) {
            Main.instance.getLogger().severe("Channel ID for channel: " + channel + " is null");
            return;
        }

        try {
            // Establish a connection to the Discord server.
            URL url = new URL(serverAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain");

            // Format the message to include the channel ID.
            String combinedMessage = channelID + ":" + message;
            if (OUTPUT) Main.instance.getLogger().warning("Sending message: " + combinedMessage);

            // Write the message to the output stream.
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = combinedMessage.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);

                // Log the response code from the Discord server.
                int responseCode = connection.getResponseCode();
                if (OUTPUT) Main.instance.getLogger().warning("Response Code: " + responseCode);
            }

        } catch (Exception e) {
            // Log errors if the message fails to send.
            Main.instance.getLogger().severe("Failed to send message to channel " + channelID + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package com.watsonllc.craft.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Config;

/**
 * Announcer handles automated server announcements at fixed intervals.
 * Messages are randomly selected and broadcast to all players.
 */
public class Announcer {
    
    private static final List<String> BASE_MESSAGES = List.of(
        "Thank you for playing on &6Craft&f!",
        "Have a problem with griefing or a player? Use &c/report",
        "Server restarts happen every 6 hours",
        "Don't forget to vote for the server!",
        "Need help? Join the Discord using /discord",
        "AusTaxOffice is the wurst",
        "Bored? Try out &6/hardmode &ffor a challenge!",
        "Too hard? Try out &6/peaceful &fto make things easier!",
        "Want to build at spawn? Talk to a moderator about becoming trusted!",
        "Make sure to claim your territory! Use &b/claim howto &fto get started"
    );

    private static final Random RANDOM = new Random();

    /**
     * Starts the broadcast task that announces messages to the server every 30 minutes.
     * The first message is broadcast immediately when the task starts.
     */
    public static void startBroadcastTask() {
        broadcastRandomMessage();
        new BukkitRunnable() {
            @Override
            public void run() {
                broadcastRandomMessage();
            }
        }.runTaskTimer(Main.instance, 0, 36000); // 36000 ticks = 30 minutes
    }

    /**
     * Broadcasts a random message to the server.
     * Dynamic messages involving server uptime and player count are generated on the fly.
     */
    private static void broadcastRandomMessage() {
        List<String> dynamicMessages = generateDynamicMessages();
        List<String> allMessages = new ArrayList<>(BASE_MESSAGES);
        allMessages.addAll(dynamicMessages);
        
        String message = allMessages.get(RANDOM.nextInt(allMessages.size()));
        Bukkit.broadcastMessage(Utils.color("&fAnnouncement &7>> &f" + message));
    }


    /**
     * Generates messages that rely on dynamic server data, such as uptime and player counts.
     * 
     * @return List of dynamically generated messages.
     */
    private static List<String> generateDynamicMessages() {
        return List.of(
            "There have been " + Config.getInt("unique") + " unique players!",
            "This world has been alive for " + Config.getUptime("days") + " days " + 
                Config.getUptime("hours") + " hours " + Config.getUptime("minutes") + " minutes and " +
                Config.getUptime("seconds") + " seconds!"
        );
    }
} 

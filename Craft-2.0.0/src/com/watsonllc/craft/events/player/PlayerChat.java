package com.watsonllc.craft.events.player;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;
import com.watsonllc.craft.groups.GroupManager;
import com.watsonllc.craft.groups.PlayerManager;
import com.watsonllc.craft.logic.DiscordBot;

public class PlayerChat implements Listener {

    private final HashMap<UUID, Long> lastMessageTime = new HashMap<>();
    private static final long MESSAGE_COOLDOWN = 2000; // Cooldown time in milliseconds
    
    private static final String spamMessage = Utils.color("&cYou are sending messages too quickly");

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        handlePlayerChat(event);
    }

    /**
     * Handles the chat event by processing player messages, applying cooldowns,
     * and formatting the chat. Also checks for muted players.
     * 
     * @param event The AsyncPlayerChatEvent to process.
     */
    private void handlePlayerChat(AsyncPlayerChatEvent event) {
        if (event == null || event.getPlayer() == null) {
            return;
        }

        Player player = event.getPlayer();

        if (PlayerData.isMuted(player.getUniqueId().toString())) {
            event.setCancelled(true);
            player.sendMessage(Utils.color("&cYou are muted! Reason: " + PlayerData.getMuteReason(player.getUniqueId().toString())));
            Bukkit.broadcast(Utils.color("&c" + player.getName() + " tried to talk but they're muted! Reason: " + PlayerData.getMuteReason(player.getUniqueId().toString())), "craft.mute.notify");
            return;
        }

        if (event.getMessage().equalsIgnoreCase("f")) {
            Bukkit.broadcastMessage(Utils.color("&c" + player.getName() + " pays their respects"));
            DiscordBot.chatMessage(event);
            event.setCancelled(true);
            return;
        }

        formatChat(event, player);
        antiSpam(event, player);
    }

    /**
     * Formats the chat message by applying player prefixes and modifying the chat layout.
     * 
     * @param event The chat event to format.
     * @param player The player sending the message.
     */
    private void formatChat(AsyncPlayerChatEvent event, Player player) {
        String format = Utils.color("%prefix% %player% &7>> &f%message%");
        GroupManager group = new GroupManager(new PlayerManager(player).getGroup());
        format = format.replace("%player%", Utils.color(player.getDisplayName()));
        format = format.replace("%message%", event.getMessage());
        format = format.replace("%prefix%", group.getPrefix());
        event.setFormat(format);
    }

    /**
     * Prevents players from spamming messages by enforcing a cooldown period.
     * Cancels the event if the cooldown has not elapsed since the last message.
     * 
     * @param event The chat event to check.
     * @param player The player attempting to send a message.
     */
    private void antiSpam(AsyncPlayerChatEvent event, Player player) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (lastMessageTime.containsKey(playerUUID)) {
            long lastTime = lastMessageTime.get(playerUUID);
            if ((currentTime - lastTime) < MESSAGE_COOLDOWN) {
                event.setCancelled(true);
                player.sendMessage(spamMessage);
                lastMessageTime.replace(playerUUID, currentTime);
                return;
            }
        }

        DiscordBot.chatMessage(event);
        lastMessageTime.put(playerUUID, currentTime);
    }
}
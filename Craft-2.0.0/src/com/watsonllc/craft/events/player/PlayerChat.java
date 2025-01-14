package com.watsonllc.craft.events.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;
import com.watsonllc.craft.groups.GroupManager;
import com.watsonllc.craft.groups.PlayerManager;
import com.watsonllc.craft.logic.DiscordBot;
import com.watsonllc.craft.logic.JackMode;

public class PlayerChat implements Listener {

	private final Map<UUID, List<UUID>> welcomedPlayer = new HashMap<>();
	private final HashMap<UUID, Long> lastMessageTime = new HashMap<>();
	private static final long MESSAGE_COOLDOWN = 2000;

	private static final String spamMessage = Utils.color("&cYou are sending messages too quickly");

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		handlePlayerChat(event);
	}

	/**
	 * Handles the chat event by processing player messages, applying cooldowns, and
	 * formatting the chat. Also checks for muted players.
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
			player.sendMessage(Utils
					.color("&cYou are muted! Reason: " + PlayerData.getMuteReason(player.getUniqueId().toString())));
			Bukkit.broadcast(Utils.color("&c" + player.getName() + " tried to talk but they're muted! Reason: "
					+ PlayerData.getMuteReason(player.getUniqueId().toString())), "craft.mute.notify");
			return;
		}

		if (event.getMessage().equalsIgnoreCase("f")) {
			Bukkit.broadcastMessage(Utils.color("&c" + player.getName() + " pays their respects"));
			DiscordBot.chatMessage(event);
			event.setCancelled(true);
			return;
		}
		
		JackMode.handleChat(event);

		formatChat(event, player);
		rewardWelcome(event);
		antiSpam(event, player);
	}

	private void rewardWelcome(AsyncPlayerChatEvent event) {
	    Player sender = event.getPlayer();
	    String message = event.getMessage().toLowerCase();

	    if (PlayerJoin.recentJoins.isEmpty()) return;

	    if (!message.contains("welcome")) return;

	    for (UUID playerId : PlayerJoin.recentJoins.keySet()) {
	        long joinTime = PlayerJoin.recentJoins.get(playerId);

	        // Check if the join time is within 10 seconds
	        if (!(System.currentTimeMillis() - joinTime <= 10000)) continue;

	        Player joinedPlayer = Bukkit.getPlayer(playerId);

	        if (joinedPlayer == null) continue;

	        // Check if the sender has already welcomed this player
	        List<UUID> alreadyWelcomed = welcomedPlayer.getOrDefault(playerId, new ArrayList<>());
	        if (alreadyWelcomed.contains(sender.getUniqueId())) {
	            sender.sendMessage(ChatColor.RED + "You've already welcomed " + joinedPlayer.getName() + "!");
	            return;
	        }

	        // Reward the sender
	        sender.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
	        sender.sendMessage(ChatColor.GREEN + "Thank you for welcoming " + joinedPlayer.getName() + "! Here's a diamond.");

	        // Mark the sender as having welcomed this player
	        alreadyWelcomed.add(sender.getUniqueId());
	        welcomedPlayer.put(playerId, alreadyWelcomed);
	        return;
	    }
	}

	/**
	 * Formats the chat message by applying player prefixes and modifying the chat
	 * layout.
	 * 
	 * @param event  The chat event to format.
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
	 * @param event  The chat event to check.
	 * @param player The player attempting to send a message.
	 */
	private void antiSpam(AsyncPlayerChatEvent event, Player player) {
		UUID playerUUID = player.getUniqueId();
		long currentTime = System.currentTimeMillis();

		if(player.hasPermission("craft.antispam.bypass"))
			return;
		
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
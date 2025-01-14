package com.watsonllc.craft.logic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.watsonllc.craft.Utils;

public class JackMode implements Listener {
	public static void handleJoin(PlayerJoinEvent event) {
		if (event.getPlayer() == null)
			return;

		if (event.getPlayer() != Bukkit.getPlayer("AusTaxOffice"))
			return;

		event.setJoinMessage(Utils.color(
				"&7AusTaxOffice &ajoined &7the game in Jack Mode! This gamemode is specially designed to support AusTaxOffice with his disabilities so he can enjoy Minecraft with us"));
		event.getPlayer().teleport(Spawn.location());
	}

	public static void handleChat(AsyncPlayerChatEvent event) {
		if (event.getPlayer() == null)
			return;

		if (event.getPlayer() != Bukkit.getPlayer("AusTaxOffice"))
			return;

		event.setMessage("im a fucking idiot, incapable of basic comprehension");
		event.getPlayer().sendMessage(Utils.color(
				"&8[&aJack&7Mode&8] &cUh oh! Its known you tend to make up lies quite often, so we helped speak the truth for you!"));
	}

	@EventHandler
	public static void handleMovement(PlayerMoveEvent event) {
		if (event.getPlayer() == null)
			return;

		if (!event.getPlayer().getName().equals("AusTaxOffice"))
			return;

		Location spawnLocation = Spawn.location();
		Location playerLocation = event.getPlayer().getLocation();

		if (playerLocation.getBlockX() != spawnLocation.getBlockX()
				|| playerLocation.getBlockY() != spawnLocation.getBlockY()
				|| playerLocation.getBlockZ() != spawnLocation.getBlockZ()) {

			event.getPlayer().teleport(Spawn.location());
			event.setCancelled(true);
			event.getPlayer().sendMessage(Utils.color(
					"&8[&aJack&7Mode&8] &cUh oh! Walking too far might be dangerous, stay on this block for safety!"));
		}
	}

	@EventHandler
	public static void handleDropItem(PlayerDropItemEvent event) {
		if (event.getPlayer() == null)
			return;

		if (event.getPlayer() != Bukkit.getPlayer("AusTaxOffice"))
			return;

		event.setCancelled(true);
		event.getPlayer().sendMessage(Utils.color(
				"&8[&aJack&7Mode&8] &cUh oh! you dropped your item because of your child like hands, dont worry I picked it up for you!"));
	}

	@EventHandler
	public static void handleInventory(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (player == null)
				return;

			if (player != Bukkit.getPlayer("AusTaxOffice"))
				return;

			event.setCancelled(true);
			player.closeInventory();
			player.sendMessage(Utils.color(
					"&8[&aJack&7Mode&8] &cUh oh! That feature of this game might be a little advanced, why dont you try to learn how to walk first!"));
		}
	}

	@EventHandler
	public static void handleInteract(PlayerInteractEvent event) {
		if (event.getPlayer() == null)
			return;

		if (event.getPlayer() != Bukkit.getPlayer("AusTaxOffice"))
			return;

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK
				&& event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_AIR) {
			return;
		}

		event.setCancelled(true);
	}

}

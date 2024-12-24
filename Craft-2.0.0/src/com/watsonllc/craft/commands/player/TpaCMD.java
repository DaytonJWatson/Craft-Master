package com.watsonllc.craft.commands.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

public class TpaCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.tpa")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}

		if (args.length != 1) {
			player.sendMessage(Utils.color("&7/&ctpa &7<&cplayer&7>"));
			return false;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null || !target.isOnline()) {
			player.sendMessage(Utils.color("&c" + args[0] + " not found"));
			return false;
		}

		if (target == player) {
			player.sendMessage(Utils.color("&cYou cant request to teleport to yourself"));
			return false;
		}
		
		if(Main.teleportRequests.containsValue(player.getUniqueId())) {
			player.sendMessage(Utils.color("&cYou already have an active teleport request!"));
			return false;
		}

		Main.teleportRequests.put(target.getUniqueId(), player.getUniqueId());
		target.sendMessage(
				Utils.color("&6" + player.getName() + " has requested to teleport to you, type /tpaccept to accept"));
		player.sendMessage(Utils.color("&6Teleport request sent to " + target.getName()));

		new BukkitRunnable() {
			@Override
			public void run() {
				if (Main.teleportRequests.containsKey(target.getUniqueId()) && Main.teleportRequests.get(target.getUniqueId()).equals(player.getUniqueId())) {
					Main.teleportRequests.remove(target.getUniqueId());
					player.sendMessage(Utils.color("&cYour teleport request to " + target.getName() + " has expired"));
					target.sendMessage(Utils.color("&cTeleport request from " + player.getName() + " has expired"));
				} else
					return;
			}
		}.runTaskLater(Main.instance, 20 * 15); // 20 ticks = 1 second

		return false;
	}

	public boolean onTpacceptCommand(CommandSender sender) {
		if (!(sender instanceof Player))
			return false;

		Player target = (Player) sender;
		UUID requesterUUID = Main.teleportRequests.remove(target.getUniqueId());
		
		if(!target.hasPermission("craft.tpaccept")) {
			target.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}

		if (requesterUUID == null) {
			target.sendMessage(Utils.color("&cNo pending teleport requests"));
			return false;
		}

		Player requester = Bukkit.getPlayer(requesterUUID);
		if (requester == null || !requester.isOnline()) {
			target.sendMessage(Utils.color("&cThe requesting player is no longer online"));
			return false;
		}

		requester.teleport(target.getLocation());
		target.sendMessage(Utils.color("&aTeleport request accepted"));
		requester.sendMessage(Utils.color("&aTeleporting to " + target.getName()));

		return false;
	}

	public boolean onTpdenyCommand(CommandSender sender) {
		if (!(sender instanceof Player))
			return false;

		Player target = (Player) sender;
		UUID requesterUUID = Main.teleportRequests.remove(target.getUniqueId());
		
		if(!target.hasPermission("craft.tpdeny")) {
			target.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}

		if (requesterUUID == null) {
			target.sendMessage(Utils.color("&cNo pending teleport requests"));
			return false;
		}

		Player requester = Bukkit.getPlayer(requesterUUID);
		if (requester == null || !requester.isOnline()) {
			target.sendMessage(Utils.color("&cThe requesting player is no longer online"));
			return false;
		}

		target.sendMessage(Utils.color("&cTeleport request denied"));
		requester.sendMessage(Utils.color("&cYour teleport request to " + target.getName() + " was denied"));

		return false;
	}
}
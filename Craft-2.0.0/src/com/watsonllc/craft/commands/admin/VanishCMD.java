package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class VanishCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.vanish")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}

		// vanish
		if (args.length == 0) {
			if (player.isInvisible()) {
				player.setInvisible(false);
				player.setCanPickupItems(true);
				player.setCollidable(true);
				player.sendMessage(Utils.color("&cVanish disabled"));
			} else if (!player.isInvisible()) {
				player.setInvisible(true);
				player.setCanPickupItems(false);
				player.setCollidable(false);
				player.sendMessage(Utils.color("&aVanish enabled"));
			}
			return false;
		}

		if (!player.hasPermission("craft.vanish.others")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}

		// vanish <player>
		if (args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);

			if (target == null) {
				player.sendMessage(Utils.color("&c" + args[0] + " could not be found"));
				return false;
			}

			if (target.isInvisible()) {
				target.setInvisible(false);
				target.setCanPickupItems(true);
				target.setCollidable(true);
				player.sendMessage(Utils.color("&cVanish disabled for " + target.getName()));
				target.sendMessage(Utils.color("&cVanish disabled"));
			} else if (!target.isInvisible()) {
				target.setInvisible(true);
				target.setCanPickupItems(false);
				target.setCollidable(false);
				player.sendMessage(Utils.color("&aVanish enabled for " + target.getName()));
				target.sendMessage(Utils.color("&aVanish enabled"));
			}
			return false;
		}

		player.sendMessage(Utils.color("&7/&cvanish &7<&cplayer&7>"));
		return false;
	}

}

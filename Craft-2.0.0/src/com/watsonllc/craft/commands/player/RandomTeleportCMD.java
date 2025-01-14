package com.watsonllc.craft.commands.player;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.logic.RTP;

public class RandomTeleportCMD implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.rtp")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}

		if (!RTP.canTeleport(player)) {
		    long cooldownTimeLeft = RTP.getCooldownTime(player);
		    long seconds = cooldownTimeLeft / 1000;
		    long minutes = seconds / 60;
		    seconds %= 60;

		    player.sendMessage(Utils.color("&cYou can't teleport right now. Please wait &6" +
		            (minutes > 0 ? minutes + " minute" + (minutes > 1 ? "s" : "") + " and " : "") +
		            seconds + " second" + (seconds != 1 ? "s" : "")));
		    return false;
		}
		
		// rtp <distance>
		if (args.length == 1 && player.hasPermission("craft.rtp.distance")) {
			if (!isInt(args[0]) || Integer.parseInt(args[0]) <= 225 || Integer.parseInt(args[0]) > 50000) {
				player.sendMessage(Utils.color("&cInvalid distance [Max: 50,000 | Min: 225 | Default: 1,000]"));
				return false;
			}
			if (RTP.teleport(player, Integer.parseInt(args[0]), 225)) {
				return true;
			}
			return false;
		}

		if (args.length == 0) {
			if (RTP.teleport(player, 1000, 225)) {
				return true;
			} else
				return false;
		}

		player.sendMessage(Utils.color("&7/&crtp &7<&cmax-distance&7>"));
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return Collections.emptyList();
	}

	private boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
package com.watsonllc.craft.commands.player;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.logic.DiscordBot;

public class ReportCMD implements CommandExecutor, TabCompleter {

	private List<String> priorityLevels = Arrays.asList("low", "medium", "high");
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;
		String name = player.getName();
		
		if(!player.hasPermission("craft.report")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		// report grief priorityLevel
		// report <player> <reason>
		if (args.length <= 1) {
			player.sendMessage(Utils.color("&7/&creport grief &7[&cpriority&7] - &fReports your current location"));
			player.sendMessage(Utils.color("&7/&creport player &7<&cplayer&7> &7[&creason&7] - &fReports the player for review"));
			return false;
		}
		
		// report player <reason>
		if(args.length <= 2 && args[0].equalsIgnoreCase("player")) {
			player.sendMessage(Utils.color("&7/&creport grief &7[&cpriority&7] - &fReports your current location"));
			player.sendMessage(Utils.color("&7/&creport player &7<&cplayer&7> &7[&creason&7] - &fReports the player for review"));
            return false;
		}
		
		// report player <player> <reason>
		if(args.length > 2 && args[0].equalsIgnoreCase("player")) {
			StringBuilder reason = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				reason.append(args[i]).append(" ");
            }
			
            // Trim the trailing space and convert to string
            String result = reason.toString().trim();
            
            DiscordBot.sendMessage(name + " has reported " + args[1], "playerReport");
            DiscordBot.sendMessage("REASON:" + result, "playerReport");
            player.sendMessage(ChatColor.RED + "Thank you for reporting a suspicious player, a moderator will be online soon to assist");
            return false;
		}

		// report grief level
		if (args.length == 2 && args[0].equalsIgnoreCase("grief")) {
			Location loc = player.getLocation();
			String world = loc.getWorld().getName();
			int x = loc.getBlockX();
			int z = loc.getBlockZ();
			DiscordBot.sendMessage(name + " has reported a griefed location at: [" + world + "] " + x + ", " + z, "griefReport");
			DiscordBot.sendMessage("PRIORITY LEVEL: " + args[1], "griefReport");
			player.sendMessage(ChatColor.RED + "Thank you for reporting a griefed location, a moderator will be online soon to assist");
			return false;
		}
		
		player.sendMessage(ChatColor.RED + "Report failed to send, please try again");

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
		//List<String> completions = new ArrayList<>();
		if(args.length == 1) {
			List<String> subCommands = Arrays.asList("grief", "player");
			return subCommands;
		}
		
		if (args.length == 2 && args[0].equalsIgnoreCase("grief")) {
			return priorityLevels;
		}
		
		if(args.length == 3 && args[0].equalsIgnoreCase("player")) {
			return Arrays.asList("reason");
		}

		return null;
	}
}
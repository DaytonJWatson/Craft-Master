package com.watsonllc.craft.commands.admin;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;
import com.watsonllc.craft.groups.PlayerManager;

public class GroupsCMD implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.groups")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		//group
		if(args.length == 0) {
			player.sendMessage(Utils.color("&6You are in group: &7"+ PlayerData.getGroup(player)));
			return false;
		}
		
		//group set <player> <group>
		if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
			Player target = Bukkit.getPlayer(args[1]);
			PlayerManager group = new PlayerManager(target);
			
			if(target == null) {
				player.sendMessage(Utils.color("&c" + args[1] + " could not be found"));
				return false;
			}
			
			group.setGroup(args[2].toLowerCase());
			player.sendMessage(Utils.color("&a"+ target.getName() + " group updated to: &7"+ args[2]));
			target.sendMessage(Utils.color("&aYour group was updated to: &7"+ args[2]));
			return false;
		}
		
		player.sendMessage(Utils.color("&7/&cgroup set &7<&cplayer&7>"));
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args) {
		if(args.length == 1) {
			List<String> subCommands = Arrays.asList("set");
			return subCommands;
		}
		
		if(args.length == 3 && args[0].equalsIgnoreCase("set")) {
			List<String> subCommands = Arrays.asList("default", "founder", "trusted", "moderator", "administrator", "owner");
			return subCommands;
		}
		return null;
	}
}
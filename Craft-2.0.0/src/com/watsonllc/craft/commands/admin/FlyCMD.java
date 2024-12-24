package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class FlyCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.fly")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length == 0) {
			if(player.getAllowFlight() == true) {
				player.setAllowFlight(false);
				player.sendMessage(Utils.color("&6You are no longer able to fly"));
			} else {
				player.setAllowFlight(true);
				player.sendMessage(Utils.color("&6You are now able to fly"));
			}
		}
		
		if(args.length == 1) {
			if(!player.hasPermission("craft.fly.others")) {
				player.sendMessage(Utils.color("&cYou dont have permission to do that"));
				return false;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage(Utils.color("&c"+args[0]+" could not be found"));
				return false;
			}
			
			if(target.getAllowFlight() == true) {
				target.setAllowFlight(false);
				target.sendMessage(Utils.color("&6You are no longer able to fly"));
				player.sendMessage(Utils.color("&6"+ target.getName() +" can no longer fly"));
			} else {
				target.setAllowFlight(true);
				target.sendMessage(Utils.color("&6You are now able to fly"));
				player.sendMessage(Utils.color("&6"+ target.getName() +" is now able to fly"));
			}
		}
		return false;
	}
}
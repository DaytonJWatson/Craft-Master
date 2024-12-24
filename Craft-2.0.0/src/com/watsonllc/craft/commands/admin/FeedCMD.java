package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class FeedCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.feed")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length == 0) {
			player.setFoodLevel(20);
			player.setSaturation(20);
			player.sendMessage(Utils.color("&6You have been fed"));
			return false;
		}
		
		if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null || !target.isOnline()) {
				player.sendMessage(Utils.color("&c"+ args[0] +" could not be found"));
				return false;
			}
			
			target.setFoodLevel(20);
			target.setSaturation(20);
			target.sendMessage(Utils.color("&6You have been fed"));
			player.sendMessage(Utils.color("&6You have fed "+ target.getName()));
		}
		return false;
	}
}

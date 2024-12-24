package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class EnderChestCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.enderchest")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length == 0) {
			player.sendMessage(Utils.color("&6Opening your ender chest"));
			player.openInventory(player.getEnderChest());
			return false;
		}
		
		if(!player.hasPermission("craft.enderchest.others")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage(Utils.color("&c"+ args[0] +" could not be found"));
				return false;
			}
			
			player.openInventory(target.getEnderChest());
			player.sendMessage(Utils.color("&6Opening " + target.getName() + "'s ender chest"));
		}
		return false;
	}

}

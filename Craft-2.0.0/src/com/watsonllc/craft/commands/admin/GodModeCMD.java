package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class GodModeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.god")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length == 0) {
			if(player.isInvulnerable()) {
				player.setInvulnerable(false);
				player.sendMessage(Utils.color("&6You are no longer invulnerable"));
				return false;
			} else {
				player.setInvulnerable(true);
				player.sendMessage(Utils.color("&6You are now invulnerable"));
				return false;
			}
		}
		
		if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null || !target.isOnline()) {
				player.sendMessage(Utils.color("&c"+ args[0] +" could not be found"));
				return false;
			}
			
			if(target.isInvulnerable()) {
				target.setInvulnerable(false);
				target.sendMessage(Utils.color("&6You are no longer invulnerable"));
				player.sendMessage(Utils.color("&6" + target.getName() +" is no longer invulnerable"));
				return false;
			} else {
				target.setInvulnerable(true);
				target.sendMessage(Utils.color("&6You are now invulnerable"));
				player.sendMessage(Utils.color("&6" + target.getName() +" is now invulnerable"));
				return false;
			}
		}
		
		return false;
	}

}

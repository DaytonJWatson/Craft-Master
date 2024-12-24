package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class InvSeeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.invsee")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length != 1) {
			player.sendMessage(Utils.color("&7/&cinvsee &7<&cplayer&7>"));
			return false;
		}
		
		//invsee <player>
		Player target = Bukkit.getPlayer(args[0]);
		
		if(target == null) {
			player.sendMessage(Utils.color("&c"+ args[0] +" cant be found"));
			return false;
		}
		
		player.openInventory(target.getInventory());
		return false;
	}

}

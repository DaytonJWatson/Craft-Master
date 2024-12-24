package com.watsonllc.craft.commands.player;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.logic.Spawn;

public class SpawnCMD implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		//
		// PLAYER COMMAND
		//

		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.spawn")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length == 0) {
			if(Spawn.isNull()) {
				player.sendMessage(Utils.color("&cSpawn location has not been set yet"));
				return false;
			}
			
			player.sendMessage(Utils.color("&6Taking you to spawn"));
			player.teleport(Spawn.location());
		}
		
		//
		// ADMIN COMMAND
		//
		
		if(args.length == 1) {
			if(!sender.hasPermission("craft.spawn.others")) {
				sender.sendMessage(Utils.color("&cYou dont have permission to do that"));
				return false;
			}
			
			Player target = Bukkit.getPlayer(args[0]);
			
			if (target == null) {
				String targetNULL = Utils.color("&c%target% could not be found");
				targetNULL = targetNULL.replace("%target%", args[0]);
				player.sendMessage(Utils.color("&c%target% could not be found"));
				return false;
			}
			
			if(Spawn.isNull()) {
				player.sendMessage(Utils.color("&cSpawn location has not been set yet"));
				return false;
			}
			
			String teleportOther = Utils.color("&6Teleporting %target% to spawn");
			teleportOther = teleportOther.replace("%target%", target.getName());
			player.sendMessage(teleportOther);
			target.sendMessage(Utils.color("&6Taking you to spawn"));
			target.teleport(Spawn.location());
		}
			
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return Collections.emptyList();
	}
}

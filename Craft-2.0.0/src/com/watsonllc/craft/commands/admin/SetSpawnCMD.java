package com.watsonllc.craft.commands.admin;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.logic.Spawn;

public class SetSpawnCMD implements CommandExecutor, TabCompleter {
	private final String setSpawn = Utils.color("&aSet new spawn location");
	private final String overwritingSpawn = Utils.color("&cOverwriting spawn location");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		Location playerLoc = player.getLocation();
		
		if(!player.hasPermission("craft.setspawn")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(!Spawn.isNull())
			player.sendMessage(overwritingSpawn);
		
		Spawn.set(playerLoc);
		player.sendMessage(setSpawn);
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return Collections.emptyList();
	}
}
package com.watsonllc.craft.commands.player;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class BedCMD implements CommandExecutor, TabCompleter {

	private final String message = Utils.color("&6Taking you to your bed");
	private final String bedNull = Utils.color("&cYour bed is missing or obstructed");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.bed")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		Location bedLoc = player.getRespawnLocation();
		
		if(bedLoc == null) {
			player.sendMessage(bedNull);
			return false;
		}
		
		player.teleport(bedLoc);
		player.sendMessage(message);
		
		return false;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
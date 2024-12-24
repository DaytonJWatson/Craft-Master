package com.watsonllc.craft.commands.player;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class RulesCMD implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.rules")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		player.sendMessage(Utils.color("&8================ &7Rules &8================"));
		player.sendMessage(Utils.color("&7- &fDo not annoy other players"));
		player.sendMessage(Utils.color("&7- &fDo not spam chat"));
		player.sendMessage(Utils.color("&7- &fNo griefing or stealing from others"));
		player.sendMessage(Utils.color("&7- &fNo using clients that give you an advantage"));
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}
}
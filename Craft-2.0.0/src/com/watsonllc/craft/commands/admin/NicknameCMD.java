package com.watsonllc.craft.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;

public class NicknameCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.nickname")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length != 1) {
			player.sendMessage(Utils.color("&8/&cnickname &8<&cnickname&8>"));
			return false;
		}
		
		player.setDisplayName(Utils.color(args[0]));
		player.setPlayerListName(Utils.color(args[0]));
		PlayerData.updateNickname(player, args[0]);
		player.sendMessage(Utils.color("&6Your nickname has been updated to &7"+ args[0]));
		return false;
	}

}
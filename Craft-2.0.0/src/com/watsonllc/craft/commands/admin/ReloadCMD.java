package com.watsonllc.craft.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Config;
import com.watsonllc.craft.config.Groups;
import com.watsonllc.craft.config.Homes;
import com.watsonllc.craft.config.PlayerData;

public class ReloadCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player)) {
			Config.reload();
			Main.instance.getLogger().warning("Reloading 'config.yml'");
			Groups.reload();
			Main.instance.getLogger().warning("Reloading 'groups.yml'");
			Homes.reload();
			Main.instance.getLogger().warning("Reloading 'homes.yml'");
			PlayerData.reload();
			Main.instance.getLogger().warning("Reloading 'playerData.yml'");
			return false;
		}
		
		Player player = (Player) sender;
		
		if(!player.hasPermission("craft.reload")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		Config.reload();
		Main.instance.getLogger().warning("Reloading 'config.yml'");
		Groups.reload();
		Main.instance.getLogger().warning("Reloading 'groups.yml'");
		Homes.reload();
		Main.instance.getLogger().warning("Reloading 'homes.yml'");
		PlayerData.reload();
		Main.instance.getLogger().warning("Reloading 'playerData.yml'");
		
		player.sendMessage(Utils.color("&6Successfully reloaded all configuration files"));
		
		return false;
	}

	
	
}

package com.watsonllc.craft.commands.player;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.gui.GUIManager;
import com.watsonllc.craft.gui.guis.AdaptiveSettingsGUI;

public class SettingsCMD implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;

		if(!player.hasPermission("craft.settings")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		GUIManager.openGUI(player, new AdaptiveSettingsGUI(player));
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return Collections.emptyList();
	}
}
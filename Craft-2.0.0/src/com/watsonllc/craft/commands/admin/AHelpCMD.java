package com.watsonllc.craft.commands.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

public class AHelpCMD implements CommandExecutor, TabCompleter {
	private Player player;
	private static int totalPages = 2;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (!(sender instanceof Player))
			return false;

		this.player = (Player) sender;
		
		if (!player.hasPermission("craft.ahelp")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length == 0) {
			sendPage(1);
			return false;
		} 
		
		if(args.length == 1) {
			if(isInt(args[0])) {
				sendPage(Integer.parseInt(args[0]));
				return false;
			}
		}
		
		if(args.length >= 2) {
			sendPage(1);
		}
		return false;
	}

	// 10 commands per page
	private void sendPage(int page) {
		player.sendMessage(Utils.color("&8================ &7Help Page &8[&6"+ page +"&8/&6"+ totalPages +"&8] ================"));
		
		if(page == 1) {
			sendCmdHelp("ban", " &8[&7player&8] <&7reason&8>", "craft.ban");
			sendCmdHelp("craftingtable", "", "craft.craftingtable");
			sendCmdHelp("craftreload", "", "craft.reload");
			sendCmdHelp("enderchest", " &8<&7player&8>", "craft.enderchest");
			sendCmdHelp("feed", " &8<&7player&8>", "craft.feed");
			sendCmdHelp("fly", " &8<&7player&8>", "craft.fly");
			sendCmdHelp("god", " &8<&7player&8>", "craft.god");
			sendCmdHelp("groups", " &8[&7set&8] &8<&7player&8>", "craft.groups");
			sendCmdHelp("heal", " &8<&7player&8>", "craft.heal");
			sendCmdHelp("invsee", " &8<&7player&8>", "craft.invsee");
			sendCmdHelp("kick", " &8[&7player&8] <&7reason&8>", "craft.kick");
			return;
		}

		if(page >= totalPages) {
			sendCmdHelp("mute", " &8[&7player&8] <&7reason&8>", "craft.mute");
			sendCmdHelp("setspawn", "", "craft.setspawn");
			sendCmdHelp("spectate", "", "craft.spectate");
			sendCmdHelp("unban", "", "craft.unban");
			sendCmdHelp("unmute", " &8[&7player&8]", "craft.unmute");
			sendCmdHelp("warn", " &8[&7player&8] <&7reason&8>", "craft.warn");
			sendCmdHelp("vanish", " &8<&7player&8>", "craft.vanish");
			return;
		}
	}

	private void sendCmdHelp(String command, String syntax, String permission) {
		if (player.hasPermission(permission))
			player.sendMessage(Utils.color("&8/&7" + command + Utils.color(syntax) + " &8- &f" + Main.instance.getCommand(command).getDescription()));
	}

	@SuppressWarnings("unused")
	private void sendCmdHelp(String command, String syntax, String permission, String desc) {
		if (player.hasPermission(permission))
			player.sendMessage(Utils.color("&8/&7"+ command + syntax +" &8- &f" + desc));
	}
	
	private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> pages = new ArrayList<>();
		for(int i=1; i <= totalPages; i++) {
			pages.add(String.valueOf(i));
		}
		return pages;
	}
}

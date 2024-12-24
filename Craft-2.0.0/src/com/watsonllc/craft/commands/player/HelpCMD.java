package com.watsonllc.craft.commands.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

public class HelpCMD implements CommandExecutor, TabCompleter {

	private Player player;
	private static int totalPages = 3;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (!(sender instanceof Player))
			return false;

		this.player = (Player) sender;
		
		if (!player.hasPermission("craft.help")) {
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
			sendCmdHelp("ahelp", " &8<&7page&8>", "craft.ahelp");
			sendCmdHelp("bank", " &8[&7account&8|&7balance&8|&7taxrate&8]", "econony.bank", "View the Global Economy and manage your account");
			sendCmdHelp("balance", "", "economy.balance", "View your account balance");
			sendCmdHelp("baltop", "", "economy.baltop", "View the economies highest balances");
			sendCmdHelp("bed", "", "craft.bed");
			sendCmdHelp("claim", " &8[&7create&8|&7remove&8|&7edit&8|&7addguest&8|&7removeguest&8]", "landclaims.create", "Manage your claimed land");
			sendCmdHelp("chestlock", " &8[&7add&8|&7remove&8|&7claim&8|&7destroy&8]", "chestlock.add", "Manage your claimed locks");
			sendCmdHelp("craftingtable", "", "craft.craftingtable");
			sendCmdHelp("delhome", "", "craft.delhome");
			sendCmdHelp("discord", "", "craft.discord");
			sendCmdHelp("heal", " &8<&7player&8>", "craft.heal");
			return;
		}

		if(page == 2) {
			sendCmdHelp("home", "", "craft.home");
			sendCmdHelp("leavearena", "", "mobarena.leave", "Leave the Mob Arena");
			sendCmdHelp("msg", " &8<&7player&8>", "craft.msg", "Private message a player");
			sendCmdHelp("nickname", " &8<&7nickname&8>", "craft.nickname");
			sendCmdHelp("pay", " &8<&7player&8> [&7amount&8]", "economy.pay", "Pay another player from your balance");
			sendCmdHelp("peaceful", "", "craft.peacefulmode");
			sendCmdHelp("pvp", "", "craft.pvptoggle");
			sendCmdHelp("report", " &8[&7player&8] <&7player&8> <&7reason&8>", "craft.report");
			sendCmdHelp("rtp", " &8<&7distance&8>", "craft.rtp");
			sendCmdHelp("rules", "", "craft.rules");
			return;
		}
		
		if(page >= totalPages) {
			sendCmdHelp("sethome", "", "craft.sethome");
			sendCmdHelp("spawn", "", "craft.spawn");
			sendCmdHelp("specs", "", "craft.specs");
			sendCmdHelp("suicide", "", "craft.suicide");
			sendCmdHelp("tpa", " &8<&7player&8>", "craft.tpa");
			sendCmdHelp("tpaccept", "", "craft.tpaccept");
			sendCmdHelp("tpdeny", "", "craft.tpdeny");
			return;
		}
	}

	private void sendCmdHelp(String command, String syntax, String permission) {
		if (player.hasPermission(permission))
			player.sendMessage(Utils.color("&8/&7" + command + Utils.color(syntax) + " &8- &f" + Main.instance.getCommand(command).getDescription()));
	}

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
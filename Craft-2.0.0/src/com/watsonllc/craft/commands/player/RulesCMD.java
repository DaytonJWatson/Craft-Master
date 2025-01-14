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
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;

		Player player = (Player) sender;

		if (!player.hasPermission("craft.rules")) {
			player.sendMessage(Utils.color("&cYou don't have permission to do that."));
			return false;
		}

		player.sendMessage(Utils.color("&8================ &7Server Rules &8================"));
		player.sendMessage(Utils.color("&71. &fRespect all players. Harassment, bullying, or hate speech will not be tolerated."));
		player.sendMessage(Utils.color("&72. &fNo griefing or stealing. Respect other players' builds and items."));
		player.sendMessage(Utils.color("&73. &fNo spamming or excessive use of caps in chat."));
		player.sendMessage(Utils.color("&74. &fNo advertising or sharing links without staff permission."));
		player.sendMessage(Utils.color("&75. &fUse only allowed modifications. Hacks, cheats, or unfair clients are prohibited."));
		player.sendMessage(Utils.color("&76. &fKeep chat and gameplay appropriate for all ages."));
		player.sendMessage(Utils.color("&77. &fAvoid exploiting bugs or glitches. Report them to staff immediately."));
		player.sendMessage(Utils.color("&78. &fAFK farms and lag-inducing builds are not allowed."));
		player.sendMessage(Utils.color("&79. &fNo PvP without mutual consent. Respect other players' wishes."));
		player.sendMessage(Utils.color("&710. &fFollow staff instructions. Disrespecting staff will result in penalties."));
		player.sendMessage(Utils.color("&8========================================"));

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return Collections.emptyList();
	}
}

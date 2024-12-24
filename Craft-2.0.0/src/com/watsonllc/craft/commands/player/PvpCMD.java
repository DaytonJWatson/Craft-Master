package com.watsonllc.craft.commands.player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.logic.PVP;

public class PvpCMD implements CommandExecutor, TabCompleter {

	private Player player;
	private UUID uuid;

	private final String enabledPvp = Utils.color("&cYou can now be attacked by other players");
	private final String disabledPvp = Utils.color("&aYou can no longer be attacked by other players");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (!(sender instanceof Player))
			return false;

		this.player = (Player) sender;
		this.uuid = player.getUniqueId();
		
		if(!player.hasPermission("craft.pvptoggle")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if (PVP.canPvp(uuid)) {
			PVP.disablePvp(uuid);
			player.sendMessage(disabledPvp);
		} else {
			PVP.enablePvp(uuid);
			player.sendMessage(enabledPvp);
		}

		return false;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
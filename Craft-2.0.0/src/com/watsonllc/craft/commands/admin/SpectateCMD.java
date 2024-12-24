package com.watsonllc.craft.commands.admin;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class SpectateCMD implements CommandExecutor {

	private HashMap<Player, GameMode> gamemodeStorage = new HashMap<>();
	private HashMap<Player, Location> locationStorage = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("&cThis command can only be used by players."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("craft.spectate")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that."));
            return true;
        }
        
        GameMode gamemode = player.getGameMode();
        Location playerLocation = player.getLocation();
        
        if(gamemode != GameMode.SPECTATOR) {
        	gamemodeStorage.put(player, gamemode);
        	locationStorage.put(player, playerLocation);
        	player.setGameMode(GameMode.SPECTATOR);
        	player.sendMessage(Utils.color("&6You are now spectating players"));
        } else {
        	player.setGameMode(gamemodeStorage.get(player));
        	player.teleport(locationStorage.get(player));
        	gamemodeStorage.remove(player);
        	locationStorage.remove(player);
        	player.sendMessage(Utils.color("&6You are no longer spectating players"));
        }
		return false;
	}
}
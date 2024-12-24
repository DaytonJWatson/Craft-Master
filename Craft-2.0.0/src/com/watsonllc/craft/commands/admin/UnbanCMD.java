package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;
import com.watsonllc.craft.logic.DiscordBot;

public class UnbanCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("&cThis command can only be used by players."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("craft.unban")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.color("&7Usage: /unban &7[&cplayer&7]"));
            return true;
        }

        String targetName = args[0];
        String uuid = getPlayerUUID(targetName);

        if (uuid == null) {
            player.sendMessage(Utils.color("&cPlayer not found."));
            return true;
        }

        if (!PlayerData.isBanned(uuid)) {
            player.sendMessage(Utils.color("&c'"+targetName+"' is not banned."));
            return true;
        }

        String path = "playerData." + uuid + ".banned";
        PlayerData.set(path, null);

        Bukkit.broadcastMessage(Utils.color("&a" + targetName + " was unbanned by " + player.getName()));
        
        DiscordBot.sendMessage(targetName + " was unbanned by " + player.getName(), "chat");

        return true;
    }

    private String getPlayerUUID(String playerName) {
        for (String key : PlayerData.playerData.getConfigurationSection("playerData").getKeys(false)) {
            String path = "playerData." + key + ".username";
            if (PlayerData.getString(path).equalsIgnoreCase(playerName)) {
                return key;
            }
        }
        return null;
    }
}

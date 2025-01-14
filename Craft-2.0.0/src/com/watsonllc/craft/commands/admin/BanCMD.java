package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Config;
import com.watsonllc.craft.config.PlayerData;
import com.watsonllc.craft.logic.DiscordBot;

public class BanCMD implements CommandExecutor {

	private final String discordInvite = Config.getString("discordInvite");
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("&cThis command can only be used by players."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("craft.ban")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.color("&7Usage: /ban &7[&cplayer&7] <&creason&7>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Utils.color("&cPlayer not found."));
            return true;
        }

        if (target.hasPermission("craft.ban.exempt")) {
            player.sendMessage(Utils.color("&cYou cannot ban this player."));
            return true;
        }

        String reason = "Banned by an administrator";
        if (args.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            reason = sb.toString().trim();
        }

        // Add ban to player data
        String uuid = target.getUniqueId().toString();
        String path = "playerData." + uuid + ".banned";
        PlayerData.set(path, true);
        PlayerData.set(path + ".reason", reason);
        PlayerData.set(path + ".bannedBy", player.getName());

        // Kick the player
        target.kickPlayer(Utils.color("&cYou have been banned from the server. \n &7Reason: &c" + reason + ". \n &7You can appeal your ban on Discord: &f" + discordInvite + "\n \n &6Thank you for playing Craft!"));
        Bukkit.broadcastMessage(Utils.color("&c" + target.getName() + " was banned by " + player.getName() + (reason.isEmpty() ? "" : " for: " + reason)));
        
        DiscordBot.sendMessage(target.getName() + " was banned by " + player.getName() + (reason.isEmpty() ? "" : " for: " + reason), "chat");

        return true;
    }
}

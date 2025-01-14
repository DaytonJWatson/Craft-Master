package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;

public class KickCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("&cThis command can only be used by players."));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("craft.kick")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that."));
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage(Utils.color("&7Usage: /kick &7[&cplayer&7] <&creason&7>"));
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        
        if (target == null) {
            player.sendMessage(Utils.color("&cPlayer not found."));
            return true;
        }
        
        if (target.hasPermission("craft.kick.exempt")) {
            player.sendMessage(Utils.color("&cYou cannot kick this player."));
            return true;
        }
        
        String reason = "Kicked by an administrator.";
        if (args.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            reason = sb.toString().trim();
        }
        
        target.kickPlayer(Utils.color("&cYou have been kicked from the server. \n &7Reason: &c"+ reason));
        Bukkit.broadcastMessage(Utils.color("&c" + target.getName() + " was kicked by " + player.getName() + (reason.isEmpty() ? "" : " for: " + reason)));
        
        return true;
    }
}

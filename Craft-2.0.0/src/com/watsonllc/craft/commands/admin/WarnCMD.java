package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;

import java.util.List;

public class WarnCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("&cThis command can only be used by players."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("craft.warn")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that."));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.color("&7Usage: /warn &7[&cplayer&7] <&cwarning&7>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Utils.color("&cPlayer not found."));
            return true;
        }

        if (args.length == 1) {
            // View warnings
            List<String> warnings = PlayerData.getWarnings(target.getUniqueId().toString());
            if (warnings.isEmpty()) {
                player.sendMessage(Utils.color("&a" + target.getName() + " has no warnings."));
            } else {
                player.sendMessage(Utils.color("&a" + target.getName() + " has the following warnings:"));
                for (String warning : warnings) {
                    player.sendMessage(Utils.color("&7- &c" + warning));
                }
            }
            return true;
        }

        // Add warning
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String warning = sb.toString().trim();

        PlayerData.addWarning(target.getUniqueId().toString(), warning);
        player.sendMessage(Utils.color("&a" + target.getName() + " has been warned for: " + warning));
        target.sendMessage(Utils.color("&cYou have been warned for: " + warning));

        return true;
    }
}

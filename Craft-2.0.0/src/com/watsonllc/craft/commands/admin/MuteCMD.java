package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;

public class MuteCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("&cThis command can only be used by players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("craft.mute")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.color("&7Usage: /mute &7[&cplayer&7] <&creason&7>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Utils.color("&cPlayer not found"));
            return true;
        }

        if (args.length == 1) {
            // View mute status
            boolean isMuted = PlayerData.isMuted(target.getUniqueId().toString());
            String muteReason = PlayerData.getMuteReason(target.getUniqueId().toString());

            if (isMuted) {
                player.sendMessage(Utils.color("&a" + target.getName() + " is currently muted for: &7" + muteReason));
            } else {
                player.sendMessage(Utils.color("&a" + target.getName() + " is not muted, to mute them use &8/&7mute "+ target.getName() + " &8<&7reason&8>"));
            }
            return true;
        }

        // Mute player
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString().trim();

        PlayerData.mutePlayer(target.getUniqueId().toString(), reason);
        player.sendMessage(Utils.color("&a" + target.getName() + " has been muted for: &7" + reason));
        target.sendMessage(Utils.color("&cYou have been muted for: " + reason));

        return true;
    }
}
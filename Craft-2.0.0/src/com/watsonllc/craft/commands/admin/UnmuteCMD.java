package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;

public class UnmuteCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.color("&cThis command can only be used by players"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("craft.unmute")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.color("&7Usage: /unmute &7[&cplayer&7]"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Utils.color("&cPlayer not found."));
            return true;
        }

        if (!PlayerData.isMuted(target.getUniqueId().toString())) {
            player.sendMessage(Utils.color("&a" + target.getName() + " is not muted"));
            return true;
        }

        PlayerData.unmutePlayer(target.getUniqueId().toString());
        player.sendMessage(Utils.color("&a" + target.getName() + " has been unmuted"));
        target.sendMessage(Utils.color("&aYou have been unmuted"));

        return true;
    }
}

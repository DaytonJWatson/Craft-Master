package com.watsonllc.craft.commands.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Homes;

public class HomeCMD implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("craft.home")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that"));
            return false;
        }
        
        if (args.length == 0) {
            player.sendMessage(Utils.color("&cUsage: /home <home_name>"));
            return false;
        }
        
        String homeName = args[0];
        if (Homes.hasHome(player, homeName)) {
            player.teleport(Homes.getHome(player, homeName));
            player.sendMessage(Utils.color("&6Taking you to home '" + homeName + "'"));
        } else {
            player.sendMessage(Utils.color("&cYou don't have a home named '" + homeName + "'"));
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1 && sender instanceof Player) {
            Player player = (Player) sender;
            completions.addAll(Homes.getHomeList(player));
        }
        return completions;
    }
}
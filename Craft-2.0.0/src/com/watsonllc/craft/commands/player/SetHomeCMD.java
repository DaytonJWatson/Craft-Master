package com.watsonllc.craft.commands.player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Homes;

import java.util.ArrayList;
import java.util.List;

public class SetHomeCMD implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("craft.sethome")) {
            player.sendMessage(Utils.color("&cYou don't have permission to do that"));
            return false;
        }
        
        if (args.length != 1) {
            player.sendMessage(Utils.color("&cUsage: /sethome <home_name>"));
            return false;
        }
        
        String homeName = args[0];
        if (Homes.setHome(player, homeName)) {
            player.sendMessage(Utils.color("&aYou set your home location '" + homeName + "'"));
        } else {
            player.sendMessage(Utils.color("&cYou have reached the maximum number of homes allowed"));
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        return new ArrayList<>();
    }
}

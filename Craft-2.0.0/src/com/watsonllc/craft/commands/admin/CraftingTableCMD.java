package com.watsonllc.craft.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.watsonllc.craft.Utils;

public class CraftingTableCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		Inventory craftingInventory = Bukkit.createInventory(player, InventoryType.WORKBENCH);
		
		if(!player.hasPermission("craft.craftingtable")) {
			player.sendMessage(Utils.color("&cYou dont have permission to do that"));
			return false;
		}
		
		if(args.length == 0) {
			player.sendMessage(Utils.color("&6Opening a crafting table"));
			player.openInventory(craftingInventory);
			return false;
		}
		return false;
	}

}

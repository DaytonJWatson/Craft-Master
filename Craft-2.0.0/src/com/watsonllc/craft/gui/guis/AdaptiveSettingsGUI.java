package com.watsonllc.craft.gui.guis;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;
import com.watsonllc.craft.gui.GUICreator;

public class AdaptiveSettingsGUI extends GUICreator {

	public AdaptiveSettingsGUI(Player player) {
		super(player, "Adaptive Settings", 36);
	}

	@Override
	public void initializeItems() {
	    boolean nametagsEnabled = PlayerData.isNameTagsEnabled(player);
	    String statusColor = nametagsEnabled ? "&a" : "&c";  // Set color based on initial status
	    
	    inventory.setItem(10, createItem(Material.NAME_TAG, "&r&6Name Tags", 1, 
	        List.of("&7Toggle entity name tags", "&8Enabled: " + statusColor + nametagsEnabled)));

	    inventory.setItem(35, createItem(Material.BARRIER, "&cClose", 1, List.of()));
	}

	@Override
	public void handleClick(InventoryClickEvent event) {
	    event.setCancelled(true); // Prevent item movement
	    ItemStack clickedItem = event.getCurrentItem();
	    if (clickedItem == null)
	        return;

	    switch (clickedItem.getType()) {
	        case NAME_TAG:
	            boolean nametagsEnabled = PlayerData.isNameTagsEnabled(player);
	            
	            // Toggle the setting
	            PlayerData.setNameTagsEnabled(player, !nametagsEnabled);
	            
	            // Determine the new color based on the updated setting
	            String statusColor = PlayerData.isNameTagsEnabled(player) ? "&a" : "&c";
	            
	            // Update the item in the GUI
	            ItemStack updatedItem = createItem(Material.NAME_TAG, "&r&6Name Tags", 1, 
	                List.of("&7Toggle entity name tags", "&8Enabled: " + statusColor + PlayerData.isNameTagsEnabled(player)));
	            
	            inventory.setItem(10, updatedItem);  // Refresh the item at slot 10
	            player.updateInventory();  // Ensure the player sees the update immediately
	            break;	            
	        case BARRIER:
	            player.closeInventory();
	            break;
	            
	        default:
	            break;
	    }
	}

	private ItemStack createItem(Material material, String name, int amount, List<String> lore) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.color(name));
		meta.setLore(Utils.color(lore));
		item.setItemMeta(meta);
		return item;
	}
}
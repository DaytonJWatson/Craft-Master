package com.watsonllc.craft.customitems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BloodMoonMace {

	// Life drain attribute that increases during blood moon
	public static ItemStack item() {
		ItemStack item = new ItemStack(Material.MACE, 1);
		ItemMeta meta = item.getItemMeta();

		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.RESET + "Forged under a crimson eclipse,");
		lore.add(ChatColor.RESET + "this mace thirsts for the life essence");
		lore.add(ChatColor.RESET + "of those who stand against its wielder.");

		meta.setItemName(ChatColor.DARK_RED + "Blood Moon Mace");
		meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
		meta.setEnchantmentGlintOverride(true);
		meta.setLore(lore);

		item.setItemMeta(meta);

		return item;
	}

	public static void attributes(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity target = event.getEntity();

		// Check if the damager is a player
		if (!(damager instanceof Player))
			return;

		Player player = (Player) damager;
		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		
		ItemMeta meta = itemInHand.getItemMeta();
		if (meta != null && meta.getItemName().equals(item().getItemMeta().getItemName())) {
		    // Life drain logic
		    double originalDamage = event.getDamage();
		    double lifeDrain = 0.5;
		    double newHealth = Math.min(player.getHealth() + lifeDrain, player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
		    player.setHealth(newHealth);

		    if (!target.hasMetadata("BloodMoonMob"))
		        return;

		    // Increased damage
		    event.setDamage(originalDamage * 2.0); // double the damage
		}

	}
}

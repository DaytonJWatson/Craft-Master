package com.watsonllc.craft;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Utility class providing helper methods for common server tasks, such as item creation,
 * color formatting, and temporary player invulnerability.
 */
public class Utils {

	/**
	 * Translates alternate color codes to ChatColor values.
	 *
	 * @param s The string with color codes (e.g., '&6Hello').
	 * @return The formatted string with color codes applied, or an empty string if null.
	 */
	public static String color(String s) {
	    if (s == null || s.isEmpty()) {
	        return "";  // Prevent null from causing exceptions
	    }
	    return ChatColor.translateAlternateColorCodes('&', s);
	}

    /**
     * Creates an ItemStack with the specified material, amount, and display name.
     *
     * @param material The material of the item.
     * @param amount   The quantity of the item.
     * @param name     The display name of the item.
     * @return A customized ItemStack.
     */
    public static ItemStack item(Material material, int amount, String name) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color(name));
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Creates an ItemStack with specified material, amount, display name, and lore.
     *
     * @param material The material of the item.
     * @param amount   The quantity of the item.
     * @param name     The display name of the item.
     * @param lore     The lore text to attach to the item.
     * @return A customized ItemStack with lore.
     */
    public static ItemStack item(Material material, int amount, String name, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(color(name));
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    /**
     * Retrieves the current date and time formatted as MM/dd/yyyy hh:mm a.
     *
     * @return The formatted date and time string.
     */
    public static String currentDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        return now.format(formatter);
    }

    /**
     * Grants temporary invulnerability to a player for 3 seconds.
     *
     * @param player The player to make invulnerable.
     */
    public static void tempInvulnerable(Player player) {
        player.setInvulnerable(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setInvulnerable(false);
            }
        }.runTaskLater(Main.instance, 60); // 60 ticks = 3 seconds
    }
} 

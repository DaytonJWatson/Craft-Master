package com.watsonllc.craft.logic;

import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

/**
 * Handles random teleportation (RTP) of players to safe locations within a specified radius.
 * Implements cooldowns to prevent frequent teleports.
 */
public class RTP {

    private static final long COOLDOWN_TIME = 2 * 60 * 1000; // 2 minutes in milliseconds
    private static final Random random = new Random();

    /**
     * Teleports a player to a random safe location within the specified range.
     *
     * @param player The player to teleport.
     * @param max    The maximum distance from spawn to teleport.
     * @param min    The minimum distance from spawn to teleport.
     * @return true if teleportation was successful, false otherwise.
     */
    public static boolean teleport(Player player, int max, int min) {
        player.sendMessage(Utils.color("&6Looking for a safe location..."));
        long currentTime = System.currentTimeMillis();
        Location randomLocation = getRandomSafeLocation(Main.world.getSpawnLocation(), max, min);
        
        if(Spawn.location() != null) {
        	randomLocation = getRandomSafeLocation(Spawn.location(), max, min);
        }

        if (randomLocation != null) {
            player.teleport(randomLocation.add(0, 1, 0));
            player.sendMessage(Utils.color("&6You have been teleported to a random location"));
            Main.lastTeleportTime.put(player.getUniqueId(), currentTime);
            return true;
        } else {
            player.sendMessage(Utils.color("&cFailed to find a safe location to teleport, please try again"));
            return false;
        }
    }

    /**
     * Checks if the player can teleport based on cooldown.
     *
     * @param player The player to check.
     * @return true if the cooldown has expired or the player has not teleported recently.
     */
    public static boolean canTeleport(Player player) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        return !Main.lastTeleportTime.containsKey(playerUUID) ||
               (currentTime - Main.lastTeleportTime.get(playerUUID) >= COOLDOWN_TIME);
    }
    
    /**
     * Checks how much cooldown time is left for the player.
     *
     * @param player The player to check.
     * @return Remaining cooldown time in milliseconds, or 0 if the player can teleport.
     */
    public static long getCooldownTime(Player player) {
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        if (Main.lastTeleportTime.containsKey(playerUUID)) {
            long lastTeleport = Main.lastTeleportTime.get(playerUUID);
            long timePassed = currentTime - lastTeleport;
            if (timePassed < COOLDOWN_TIME) {
                return COOLDOWN_TIME - timePassed;
            }
        }
        return 0;
    }

    /**
     * Attempts to find a random safe location within a specified radius from origin.
     *
     * @param origin The origin location (usually spawn).
     * @param max    The maximum teleport distance.
     * @param min    The minimum teleport distance.
     * @return A safe location or null if no location is found after multiple attempts.
     */
    private static Location getRandomSafeLocation(Location origin, int max, int min) {
        for (int i = 0; i < 10; i++) { // Try 10 times to find a safe location
            double distance = min + random.nextDouble() * (max - min);
            double angle = random.nextDouble() * 2 * Math.PI;

            int x = origin.getBlockX() + (int) (distance * Math.cos(angle));
            int z = origin.getBlockZ() + (int) (distance * Math.sin(angle));
            int y = origin.getWorld().getHighestBlockYAt(x, z);

            Location potentialLocation = new Location(origin.getWorld(), x, y, z);
            Material material = potentialLocation.getBlock().getType();

            if (isSafe(material)) {
                return potentialLocation;
            }
        }
        return null;
    }

    /**
     * Checks if the material at the location is safe for teleportation.
     *
     * @param material The block material at the teleport destination.
     * @return true if the material is solid and not hazardous.
     */
    private static boolean isSafe(Material material) {
        return material != Material.LAVA && material != Material.WATER && material.isSolid();
    }
}

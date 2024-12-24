package com.watsonllc.craft.logic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Config;

/**
 * Manages server spawn points, including setting, removing, and teleporting players to spawn.
 */
public class Spawn {

    /**
     * Sets the spawn location and saves it to the configuration.
     *
     * @param location The new spawn location.
     */
    public static void set(Location location) {
        Config.set("spawn.world", location.getWorld().getName());
        Config.set("spawn.x", location.getX());
        Config.set("spawn.y", location.getY());
        Config.set("spawn.z", location.getZ());
        Config.set("spawn.yaw", location.getYaw());
        Config.set("spawn.pitch", location.getPitch());
        Config.save();
    }

    /**
     * Removes the spawn location from the configuration.
     */
    public static void remove() {
        String[] keys = {"world", "x", "y", "z", "yaw", "pitch"};
        for (String key : keys) {
            Config.set("spawn." + key, null);
        }
        Config.save();
    }

    /**
     * Retrieves the current spawn location from the configuration.
     *
     * @return The spawn location, or null if the world is not found.
     */
    public static Location location() {
        World world = Bukkit.getWorld(Config.getString("spawn.world"));
        if (world == null) return null;
        return new Location(
            world,
            Config.getDouble("spawn.x"),
            Config.getDouble("spawn.y"),
            Config.getDouble("spawn.z"),
            (float) Config.getDouble("spawn.yaw"),
            (float) Config.getDouble("spawn.pitch")
        );
    }

    /**
     * Checks if the spawn location is set and valid.
     *
     * @return true if any spawn configuration is missing, false otherwise.
     */
    public static boolean isNull() {
        String[] keys = {"world", "x", "y", "z", "yaw", "pitch"};
        for (String key : keys) {
            if (Config.get("spawn." + key) == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Teleports a player to the spawn location when they take void damage.
     *
     * @param event The EntityDamageEvent triggered by void damage.
     */
    public static void teleportFromVoid(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && event.getCause() == EntityDamageEvent.DamageCause.VOID && !Spawn.isNull()) {
            event.setCancelled(true);
            Utils.tempInvulnerable(player);
            player.teleport(Spawn.location());
            player.sendMessage("Whoosh!");
        }
    }
}

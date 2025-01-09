package com.watsonllc.craft.logic;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.config.PlayerData;

public class NameTags {

    public static void initialize() {
        // Register a repeating task to check visibility every 5 ticks (0.25 seconds)
        new BukkitRunnable() {
            @Override
            public void run() {
                // Iterate over all online players
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Schedule a synchronous task to ensure safety
                    Bukkit.getScheduler().runTask(Main.instance, () -> checkEntityVisibility(player));
                }
            }
        }.runTaskTimer(Main.instance, 0L, 5L);
    }

    public static void checkEntityVisibility(Player player) {
        // Check if nametags are enabled for this player
        boolean nametagsEnabled = PlayerData.isNameTagsEnabled(player);

        List<LivingEntity> nearbyLivingEntities = player.getNearbyEntities(50, 50, 50).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .collect(Collectors.toList());

        for (LivingEntity livingEntity : nearbyLivingEntities) {
            if (!nametagsEnabled) {
                // Hide nametag if disabled
                livingEntity.setCustomNameVisible(false);
            } else {
                // Check visibility if nametags are enabled
                boolean isVisible = player.hasLineOfSight(livingEntity) && isDirectlyVisible(player, livingEntity);
                livingEntity.setCustomNameVisible(isVisible);
            }
        }
    }

    public static boolean isDirectlyVisible(Player player, LivingEntity entity) {
        // Get the locations of the player and the entity
        Location playerLocation = player.getEyeLocation();
        Location entityLocation = entity.getLocation().add(0, entity.getHeight() / 2.0, 0); // target the middle of the entity

        // Ray trace along the direction to see if there is a direct line of sight
        return playerLocation.getWorld().rayTraceBlocks(playerLocation, entityLocation.toVector().subtract(playerLocation.toVector()), playerLocation.distance(entityLocation)) == null;
    }
}
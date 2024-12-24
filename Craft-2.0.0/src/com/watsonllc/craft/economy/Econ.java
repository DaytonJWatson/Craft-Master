package com.watsonllc.craft.economy;

import org.bukkit.entity.EntityType;

/**
 * Handles the economy system for mob rewards.
 * Provides monetary rewards based on the type of mob killed.
 */
public class Econ {    
    /**
     * Returns the reward amount for killing a specific type of mob.
     *
     * @param mobType The type of mob killed.
     * @return The monetary reward for the specified mob type.
     */
    public static double mobReward(EntityType mobType) {
        switch (mobType) {
            case BLAZE -> { return 4; }
            case CREEPER -> { return 4; }
            case ENDERMAN -> { return 5; }
            case ENDERMITE -> { return 1; }
            case ELDER_GUARDIAN -> { return 8; }
            case EVOKER -> { return 5; }
            case GHAST -> { return 6; }
            case GUARDIAN -> { return 3; }
            case PHANTOM -> { return 2; }
            case PIGLIN -> { return 3; }
            case PIGLIN_BRUTE -> { return 4; }
            case PILLAGER -> { return 2; }
            case VINDICATOR -> { return 4; }
            case RAVAGER -> { return 8; }
            case ZOMBIE -> { return 2; }
            case ZOMBIE_VILLAGER -> { return 3; }
            case DROWNED -> { return 2; }
            case HUSK -> { return 2; }
            case SKELETON -> { return 2; }
            case STRAY -> { return 2; }
            case SHULKER -> { return 2; }
            case SILVERFISH -> { return 0.5; }
            case SPIDER -> { return 2; }
            case CAVE_SPIDER -> { return 2; }
            case MAGMA_CUBE -> { return 0.5; }
            case SLIME -> { return 0.5; }
            case WITHER_SKELETON -> { return 6; }
            case WITHER -> { return 100; }
            case ENDER_DRAGON -> { return 250; }
            case BAT -> { return 0.25; }
            case WITCH -> { return 3; }
            default -> { return 0; }
        }
    }
}

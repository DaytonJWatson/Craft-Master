package com.watsonllc.craft.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;

/**
 * AdaptiveDifficulty dynamically adjusts the health and damage of mobs based on
 * nearby player difficulty levels. Difficulty scales with player experience, kills, deaths,
 * and playtime, creating a progressive challenge as players engage more with the game.
 */
public class AdaptiveDifficulty {
    private final static int maxDifficulty = 1000;
    private final static Random random = new Random();
    private static final Map<Player, PlayerStats> playerStats = new HashMap<>();

    /**
     * Starts a repeating task that adjusts player difficulty daily at a specific in-game time.
     */
    public static void startDifficultyTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = Main.world.getTime();
                if (currentTime == 13000) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        adjustPlayerDifficulty(player);
                    }
                }
            }
        }.runTaskTimer(Main.instance, 0, 1);
    }

    /**
     * Adjusts player difficulty based on experience, kills, deaths, blocks broken, and playtime.
     * @param player The player whose difficulty will be recalculated.
     */
    public static void adjustPlayerDifficulty(Player player) {
        String path = "playerData." + player.getUniqueId().toString() + ".difficulty";
        int storedDifficulty = (PlayerData.get(path) != null) ? (int) PlayerData.get(path) : 1;

        PlayerStats stats = playerStats.computeIfAbsent(player, p -> new PlayerStats());
        stats.difficultyLevel = storedDifficulty;

        // Calculate experience factor:
        // +1 if the player's total experience is greater than 1000.
        // An additional +1 if recent XP gain is greater than 100.
        int experienceFactor = player.getTotalExperience() > 1000 ? 1 : 0;
        if (stats.recentXpGain > 35) experienceFactor++;

        // Calculate kill factor:
        // 1 point for every 10 mob kills, capped at 100 points.
        int killFactor = Math.min(stats.mobKills / 10, 100);

        // Calculate death factor:
        // -1 point for each death, capped at a maximum penalty of -10.
        int deathFactor = -Math.min(stats.deaths, 10);

        // Calculate playtime factor:
        // 1 point for every hour of playtime (72,000 ticks = 1 hour), capped at 10 points.
        int playtimeFactor = (int) (player.getStatistic(org.bukkit.Statistic.PLAY_ONE_MINUTE) / 72000);
        playtimeFactor = Math.min(playtimeFactor, 10);

        // Calculate blocks broken factor:
        // 1 point for every 100 blocks broken, capped at 100 points.
        int blocksBrokenFactor = Math.min(stats.blocksBroken / 100, 100);

        // Calculate total difficulty adjustment:
        // Sum of all factors: experience, kills, playtime, deaths, and blocks broken.
        int difficultyAdjustment = experienceFactor + killFactor + playtimeFactor + deathFactor + blocksBrokenFactor;

        // Update the player's difficulty level:
        // Ensure it's at least 1 and does not exceed the maxDifficulty value.
        stats.difficultyLevel = Math.max(1, Math.min(stats.difficultyLevel + difficultyAdjustment, maxDifficulty));

        // Save the updated difficulty level to the data source.
        PlayerData.set(path, stats.difficultyLevel);
        player.setPlayerListName(Utils.color("&7Lvl "+PlayerData.getDifficulty(player)+ "&r "+ player.getName()));

        // Notify the player of the updated difficulty and factors.
        player.sendMessage("§8[§6AdaptiveDifficulty§8] §aWorld difficulties have updated!");
        player.sendMessage("§e - XP: " + player.getTotalExperience() + " (+" + stats.recentXpGain + ") | Factor: " + experienceFactor);
        player.sendMessage("§e - Mob Kills: " + stats.mobKills + " | Factor: " + killFactor);
        player.sendMessage("§e - Deaths: " + stats.deaths + " | Factor: " + deathFactor);
        player.sendMessage("§e - Playtime: "+ player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000 + " | Factor: " + playtimeFactor);
        player.sendMessage("§e - Excavation: "+ stats.blocksBroken + " | Factor: " + blocksBrokenFactor);
        player.sendMessage("§a - New Difficulty Level: " + stats.difficultyLevel);

        // Reset temporary stats such as recent XP gain to avoid double-counting.
        stats.resetTemporaryStats();
    }

    /**
     * Scales mob health and damage upon spawning, based on nearby players' difficulty levels.
     * @param event Creature spawn event triggering the scaling adjustment.
     */
    @EventHandler
    public static void onMobSpawn(CreatureSpawnEvent event) {
        if (!Utils.HOSTILE_MOBS.contains(event.getEntityType())) return;

        LivingEntity mob = event.getEntity();
        int maxNearbyDifficulty = getMaxNearbyPlayerDifficulty(mob.getLocation(), 100);
        double typeModifier = getMobTypeModifier(mob.getType());
        double randomFactor = 0.8 + (random.nextDouble() * 0.4);

        double scaleFactor = 1 + ((maxNearbyDifficulty / 1000.0) * 3.0);
        double finalHealth = Math.round(Math.min(1000, mob.getAttribute(Attribute.MAX_HEALTH).getBaseValue() * typeModifier * scaleFactor * randomFactor));
        double finalDamage = Math.round(Math.min(50, mob.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue() * typeModifier * scaleFactor));

        double originalHealth = mob.getAttribute(Attribute.MAX_HEALTH).getBaseValue();//debug info
        mob.getAttribute(Attribute.MAX_HEALTH).setBaseValue(finalHealth);
        mob.setHealth(finalHealth);
        double originalDamage = mob.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue();//debug info
        mob.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(finalDamage);
        
        mob.setCustomName(mob.getType().name() + " [Lvl " + maxNearbyDifficulty + "]");
        mob.setCustomNameVisible(true);
        
        //debug info
        Bukkit.broadcastMessage("§6New Mob Spawn: " + event.getEntity().getCustomName());
        Bukkit.broadcastMessage(" §a- Original Damage: "+ originalDamage);
        Bukkit.broadcastMessage(" §c- Leveled Damage: "+ finalDamage);
        Bukkit.broadcastMessage(" §a- Original Health: "+ originalHealth);
        Bukkit.broadcastMessage(" §c- Leveled Health: "+ finalHealth);
    }

    /**
     * Calculates the highest nearby player difficulty within a radius.
     * @param location The location of the mob being spawned.
     * @param radius The search radius around the mob.
     * @return The highest player difficulty level within the radius.
     */
    private static int getMaxNearbyPlayerDifficulty(Location location, double radius) {
        int maxDifficulty = 1;
        double min = 0.9;
        double max = 1.125;
        double randomValue = min + (Math.random() * (max - min));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(location.getWorld()) && player.getLocation().distance(location) <= radius) {
                PlayerStats stats = playerStats.get(player);
                int playerDifficulty = (stats != null) ? stats.difficultyLevel : 1;
                maxDifficulty = (int) Math.round(Math.max(maxDifficulty, playerDifficulty) * randomValue);
            }
        }
        return maxDifficulty;
    }

    /**
     * Returns a type-based difficulty modifier for mob scaling.
     * @param type The type of the entity being spawned.
     * @return The scaling multiplier for the entity type.
     */
    private static double getMobTypeModifier(EntityType type) {
        return switch (type) {
            case ENDERMAN, WITHER_SKELETON -> 1.8;
            case ZOMBIE, SKELETON -> 1.2;
            case SPIDER, CREEPER -> 1.5;
            case WITCH -> 2.0;
            default -> 1.0;
        };
    }

    /**
     * Tracks player statistics to calculate difficulty scaling.
     */
    private static class PlayerStats {
        int difficultyLevel = 0;
        int mobKills = 0;
        int recentXpGain = 0;
        int deaths = 0;
        int blocksBroken = 0;

        /**
         * Resets temporary player statistics after each difficulty adjustment.
         */
        void resetTemporaryStats() {
            mobKills = 0;
            recentXpGain = 0;
            deaths = 0;
            blocksBroken = 0;
        }
    }

	@EventHandler
	public static void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String path = "playerData." + player.getUniqueId().toString() + ".difficulty";
		int storedDifficulty = (PlayerData.get(path) != null) ? (int) PlayerData.get(path) : 1;

		PlayerStats stats = playerStats.computeIfAbsent(player, p -> new PlayerStats());
		stats.difficultyLevel = storedDifficulty;

		adjustPlayerDifficulty(player);
	}

	@EventHandler
	public static void onExperienceGain(PlayerExpChangeEvent event) {
		Player player = event.getPlayer();
		PlayerStats stats = playerStats.computeIfAbsent(player, p -> new PlayerStats());
		stats.recentXpGain += event.getAmount();
	}

	@EventHandler
	public static void onPlayerKill(EntityDeathEvent event) {
		Entity entity = event.getEntity();

		if (entity instanceof Monster && event.getEntity().getKiller() instanceof Player player) {
			PlayerStats stats = playerStats.computeIfAbsent(player, p -> new PlayerStats());
			stats.mobKills++;
		}
	}

	@EventHandler
	public static void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerStats stats = playerStats.computeIfAbsent(player, p -> new PlayerStats());
		stats.deaths++;
	}
	
    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerStats stats = playerStats.computeIfAbsent(player, p -> new PlayerStats());
        stats.blocksBroken++;
    }
}

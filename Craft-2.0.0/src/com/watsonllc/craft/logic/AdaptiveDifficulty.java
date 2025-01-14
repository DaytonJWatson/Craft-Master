package com.watsonllc.craft.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.PlayerData;

/**
 * AdaptiveDifficulty dynamically adjusts the health and damage of mobs based on
 * nearby player difficulty levels. Difficulty scales with player experience,
 * kills, deaths, and playtime, creating a progressive challenge as players
 * engage more with the game.
 */
public class AdaptiveDifficulty {
	private final static int maxDifficulty = 1000;
	private final static Random random = new Random();
	private static final Map<Player, PlayerStats> playerStats = new HashMap<>();

	/**
	 * Starts a repeating task that adjusts player difficulty daily at a specific
	 * in-game time.
	 */
	public static void startDifficultyTimer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				long currentTime = Main.world.getTime();
				if (currentTime == 22300) {
					for (Player player : Bukkit.getOnlinePlayers()) {
						adjustPlayerDifficulty(player);
					}
				}
			}
		}.runTaskTimer(Main.instance, 0, 1);
	}

	/**
	 * Adjusts player difficulty based on experience, kills, deaths, blocks broken,
	 * and playtime.
	 * 
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
		if (stats.recentXpGain > 35)
			experienceFactor++;

		// Calculate kill factor:
		// 1 point for every 10 mob kills, capped at 100 points.
		int killFactor = Math.min(stats.mobKills / 10, 100);

		// Calculate death factor:
		// -1 point for each death, capped at a maximum penalty of -10.
		int deathFactor = -Math.min(stats.deaths, 10);

		// Calculate playtime factor:
		// 1 point for every hour of playtime (72,000 ticks = 1 hour), capped at 10
		// points.
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
		player.setPlayerListName(Utils.color("&7Lvl " + PlayerData.getDifficulty(player) + "&r " + player.getName()));

		// Notify the player of the updated difficulty and factors.
		player.sendMessage("§8[§6AdaptiveDifficulty§8] §aWorld difficulties have updated!");
		player.sendMessage("§e - XP: " + player.getTotalExperience() + " (+" + stats.recentXpGain + ") | Factor: "
				+ experienceFactor);
		player.sendMessage("§e - Mob Kills: " + stats.mobKills + " | Factor: " + killFactor);
		player.sendMessage("§e - Deaths: " + stats.deaths + " | Factor: " + deathFactor);
		player.sendMessage("§e - Playtime: " + player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 72000 + " | Factor: "
				+ playtimeFactor);
		player.sendMessage("§e - Excavation: " + stats.blocksBroken + " | Factor: " + blocksBrokenFactor);
		player.sendMessage("§a - New Difficulty Level: " + stats.difficultyLevel);

		// Reset temporary stats such as recent XP gain to avoid double-counting.
		stats.resetTemporaryStats();
	}

	/**
	 * Scales mob health and damage upon spawning, based on nearby players'
	 * difficulty levels.
	 * 
	 * @param event Creature spawn event triggering the scaling adjustment.
	 */
	@EventHandler
	public static void onMobSpawn(CreatureSpawnEvent event) {
		if (!Utils.HOSTILE_MOBS.contains(event.getEntityType()))
			return;

		LivingEntity mob = event.getEntity();
		int maxNearbyDifficulty = getMaxNearbyPlayerDifficulty(mob.getLocation(), 100);
		double typeModifier = getMobTypeModifier(mob.getType());
		double randomFactor = 0.8 + (random.nextDouble() * 0.4);

		double scaleFactor = 1 + ((maxNearbyDifficulty / 1000.0) * 3.0);
		double finalHealth = Math.round(Math.min(1000,
				mob.getAttribute(Attribute.MAX_HEALTH).getBaseValue() * typeModifier * scaleFactor * randomFactor));
		double finalDamage = Math.round(
				Math.min(50, mob.getAttribute(Attribute.ATTACK_DAMAGE).getBaseValue() * typeModifier * scaleFactor));

		mob.getAttribute(Attribute.MAX_HEALTH).setBaseValue(finalHealth);
		mob.setHealth(finalHealth);
		mob.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(finalDamage);

		mob.setCustomName(mob.getType().name() + " [Lvl " + maxNearbyDifficulty + "]");
		mob.setCustomNameVisible(true);

		// Generate loot for the mob to wear
		List<ItemStack> loot = generateLootForLevel(maxNearbyDifficulty);

		// Equip the mob with loot (armor and weapon)
		equipMobWithLoot(mob, loot);
	}

	/**
	 * Calculates the highest nearby player difficulty within a radius.
	 * 
	 * @param location The location of the mob being spawned.
	 * @param radius   The search radius around the mob.
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
	 * 
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

	private static List<ItemStack> generateLootForLevel(int level) {
	    List<ItemStack> loot = new ArrayList<>();
	    Random random = new Random();

	    // Define loot tiers
	    Material[] lowTierTools = { Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_HOE };
	    Material[] lowTierArmor = { Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS };

	    Material[] midTierTools = { Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_HOE };
	    Material[] midTierArmor = { Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS };

	    Material[] highTierTools = { Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_HOE };
	    Material[] highTierArmor = { Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS };

	    Material[] legendaryTierTools = { Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE };
	    Material[] legendaryTierArmor = { Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS };

	    Material[] ultimateTierTools = { Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE };
	    Material[] ultimateTierArmor = { Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS };

	    // Determine loot generation probability
	    if (level < 600) {
	        double lootChance = level / 600.0; // Probability scales from 0 at level 0 to 1 at level 600
	        if (random.nextDouble() > lootChance) {
	            return loot; // No loot for this mob
	        }
	    }

	    // Determine loot tier based on level
	    Material[] tools;
	    Material[] armor;

	    if (level < 20) {
	        return loot; // No loot for mobs below level 20
	    } else if (level < 300) {
	        tools = mixTiers(lowTierTools, midTierTools, 0.9, random);
	        armor = mixTiers(lowTierArmor, midTierArmor, 0.9, random);
	    } else if (level < 500) {
	        tools = mixTiers(midTierTools, highTierTools, 0.8, random);
	        armor = mixTiers(midTierArmor, highTierArmor, 0.8, random);
	    } else if (level < 750) {
	        tools = mixTiers(highTierTools, legendaryTierTools, 0.7, random);
	        armor = mixTiers(highTierArmor, legendaryTierArmor, 0.7, random);
	    } else {
	        tools = mixTiers(legendaryTierTools, ultimateTierTools, 0.6, random);
	        armor = mixTiers(legendaryTierArmor, ultimateTierArmor, 0.6, random);
	    }

	    // Very rare chance for out-of-tier loot
	    if (random.nextDouble() < 0.005) { // 0.5% chance
	        tools = ultimateTierTools;
	        armor = ultimateTierArmor;
	    } else if (random.nextDouble() < 0.01) { // 1% chance
	        tools = lowTierTools;
	        armor = lowTierArmor;
	    }

	    // Generate loot items
	    int itemCount = Math.min(5, level / 200 + 1); // Max 5 items
	    for (int i = 0; i < itemCount; i++) {
	        loot.add(random.nextBoolean() 
	            ? new ItemStack(tools[random.nextInt(tools.length)]) 
	            : new ItemStack(armor[random.nextInt(armor.length)]));
	    }

	    // Add enchantments based on level
	    for (ItemStack item : loot) {
	        addEnchantments(item, level);
	    }

	    return loot;
	}

	/**
	 * Mixes items from two tiers with a bias toward the first tier.
	 *
	 * @param tier1  The first tier of items.
	 * @param tier2  The second tier of items.
	 * @param chance The probability of selecting an item from the first tier.
	 * @param random The Random instance to use.
	 * @return A combined array of items with bias toward the first tier.
	 */
	private static Material[] mixTiers(Material[] tier1, Material[] tier2, double chance, Random random) {
		List<Material> mixed = new ArrayList<>();
		for (Material item : tier1) {
			if (random.nextDouble() <= chance) {
				mixed.add(item);
			}
		}
		for (Material item : tier2) {
			if (random.nextDouble() > chance) {
				mixed.add(item);
			}
		}
		return mixed.toArray(new Material[0]);
	}

	/**
	 * Equips a mob with loot items.
	 *
	 * @param mob  The mob to equip.
	 * @param loot The loot items to equip the mob with.
	 */
	private static void equipMobWithLoot(LivingEntity mob, List<ItemStack> loot) {
		for (ItemStack item : loot) {
			Material type = item.getType();

			// Equip armor
			if (type.name().contains("_HELMET")) {
				mob.getEquipment().setHelmet(item);
				mob.getEquipment().setHelmetDropChance(1.0f); // Ensure it always drops
			} else if (type.name().contains("_CHESTPLATE")) {
				mob.getEquipment().setChestplate(item);
				mob.getEquipment().setChestplateDropChance(1.0f);
			} else if (type.name().contains("_LEGGINGS")) {
				mob.getEquipment().setLeggings(item);
				mob.getEquipment().setLeggingsDropChance(1.0f);
			} else if (type.name().contains("_BOOTS")) {
				mob.getEquipment().setBoots(item);
				mob.getEquipment().setBootsDropChance(1.0f);
			}

			// Equip main hand items (tools and weapons)
			else if (type.name().contains("_SWORD") || type.name().contains("_AXE") || type.name().contains("_PICKAXE")
					|| type.name().contains("_SHOVEL") || type.name().contains("_HOE")) {
				mob.getEquipment().setItemInMainHand(item);
				mob.getEquipment().setItemInMainHandDropChance(1.0f); // Ensure it always drops
			}

			// Equip off-hand items (e.g., shields)
			else if (type == Material.SHIELD) {
				mob.getEquipment().setItemInOffHand(item);
				mob.getEquipment().setItemInOffHandDropChance(1.0f);
			}
		}
	}

	/**
	 * Adds applicable enchantments to an item based on the mob's level. Lower-level
	 * mobs receive fewer enchantments, while higher-level mobs receive more.
	 * 
	 * @param item  The item to enchant.
	 * @param level The mob's level.
	 */
	private static void addEnchantments(ItemStack item, int level) {
		ItemMeta meta = item.getItemMeta();
		if (meta == null)
			return;

		Random random = new Random();

		// Get applicable enchantments for the item
		Enchantment[] applicableEnchantments = getApplicableEnchantments(item);

		// Determine the number of enchantments based on mob level
		int minEnchantments = 1;
		int maxEnchantments = level < 100 ? 1 : (level < 500 ? 2 : 3);
		int enchantmentCount = minEnchantments
				+ (maxEnchantments > minEnchantments ? random.nextInt(maxEnchantments - minEnchantments + 1) : 0);

		for (int i = 0; i < enchantmentCount; i++) {
			if (applicableEnchantments.length == 0)
				break;

			// Select a random enchantment from applicable ones
			Enchantment enchantment = applicableEnchantments[random.nextInt(applicableEnchantments.length)];

			// Determine the max level for this enchantment
			int maxLevel = getMaxEnchantmentLevel(enchantment);

			// Calculate enchantment level, ensuring positive bounds
			int levelCap = Math.min(level / 200, maxLevel);
			int enchantmentLevel = levelCap > 0 ? random.nextInt(levelCap) + 1 : 1;

			// Apply the enchantment if not already applied
			if (!meta.hasEnchant(enchantment)) {
				meta.addEnchant(enchantment, enchantmentLevel, true);
			}
		}

		item.setItemMeta(meta);
	}

	/**
	 * Retrieves applicable enchantments for the given item.
	 * 
	 * @param item The item to check.
	 * @return An array of applicable enchantments.
	 */
	private static Enchantment[] getApplicableEnchantments(ItemStack item) {
		List<Enchantment> enchantments = new ArrayList<>();
		Material type = item.getType();

		// Tool Enchantments
		if (type.name().contains("_SWORD")) {
			enchantments.add(Enchantment.SHARPNESS);
			enchantments.add(Enchantment.FIRE_ASPECT);
			enchantments.add(Enchantment.LOOTING);
			enchantments.add(Enchantment.KNOCKBACK);
			enchantments.add(Enchantment.SWEEPING_EDGE);
			enchantments.add(Enchantment.UNBREAKING);
			enchantments.add(Enchantment.MENDING);
		} else if (type.name().contains("_AXE")) {
			enchantments.add(Enchantment.SHARPNESS);
			enchantments.add(Enchantment.EFFICIENCY);
			enchantments.add(Enchantment.FORTUNE);
			enchantments.add(Enchantment.UNBREAKING);
			enchantments.add(Enchantment.MENDING);
			enchantments.add(Enchantment.SILK_TOUCH);
		} else if (type.name().contains("_PICKAXE") || type.name().contains("_SHOVEL")
				|| type.name().contains("_HOE")) {
			enchantments.add(Enchantment.EFFICIENCY);
			enchantments.add(Enchantment.FORTUNE);
			enchantments.add(Enchantment.UNBREAKING);
			enchantments.add(Enchantment.MENDING);
			enchantments.add(Enchantment.SILK_TOUCH);
		}
		// Armor Enchantments
		else if (type.name().contains("_HELMET") || type.name().contains("_CHESTPLATE")
				|| type.name().contains("_LEGGINGS") || type.name().contains("_BOOTS")) {
			enchantments.add(Enchantment.PROTECTION);
			enchantments.add(Enchantment.FIRE_PROTECTION);
			enchantments.add(Enchantment.PROJECTILE_PROTECTION);
			enchantments.add(Enchantment.BLAST_PROTECTION);
			enchantments.add(Enchantment.THORNS);
			enchantments.add(Enchantment.UNBREAKING);
			enchantments.add(Enchantment.MENDING);

			if (type.name().contains("_HELMET")) {
				enchantments.add(Enchantment.RESPIRATION);
				enchantments.add(Enchantment.AQUA_AFFINITY);
			} else if (type.name().contains("_BOOTS")) {
				enchantments.add(Enchantment.DEPTH_STRIDER);
				enchantments.add(Enchantment.FROST_WALKER);
				enchantments.add(Enchantment.FEATHER_FALLING);
				enchantments.add(Enchantment.SOUL_SPEED);
			}
		}
		// Bow Enchantments
		else if (type == Material.BOW) {
			enchantments.add(Enchantment.PUNCH);
			enchantments.add(Enchantment.FLAME);
			enchantments.add(Enchantment.INFINITY);
			enchantments.add(Enchantment.POWER);
			enchantments.add(Enchantment.UNBREAKING);
			enchantments.add(Enchantment.MENDING);
		}
		// Crossbow Enchantments
		else if (type == Material.CROSSBOW) {
			enchantments.add(Enchantment.PIERCING);
			enchantments.add(Enchantment.QUICK_CHARGE);
			enchantments.add(Enchantment.MULTISHOT);
			enchantments.add(Enchantment.UNBREAKING);
			enchantments.add(Enchantment.MENDING);
		}
		// Trident Enchantments
		else if (type == Material.TRIDENT) {
			enchantments.add(Enchantment.IMPALING);
			enchantments.add(Enchantment.LOYALTY);
			enchantments.add(Enchantment.RIPTIDE);
			enchantments.add(Enchantment.CHANNELING);
			enchantments.add(Enchantment.UNBREAKING);
			enchantments.add(Enchantment.MENDING);
		}
		// Shield Enchantments (Only Unbreaking and Mending are applicable)
		else if (type == Material.SHIELD) {
			enchantments.add(Enchantment.UNBREAKING);
			enchantments.add(Enchantment.MENDING);
		}

		return enchantments.toArray(new Enchantment[0]);
	}

	/**
	 * Retrieves the maximum level for a given enchantment.
	 * 
	 * @param enchantment The enchantment to check.
	 * @return The maximum level for this enchantment.
	 */
	private static int getMaxEnchantmentLevel(Enchantment enchantment) {
		if (enchantment.equals(Enchantment.MENDING))
			return 1;
		if (enchantment.equals(Enchantment.SILK_TOUCH))
			return 1;
		if (enchantment.equals(Enchantment.FORTUNE) || enchantment.equals(Enchantment.LOOTING)
				|| enchantment.equals(Enchantment.LUCK_OF_THE_SEA))
			return 3;
		if (enchantment.equals(Enchantment.DEPTH_STRIDER) || enchantment.equals(Enchantment.FROST_WALKER)
				|| enchantment.equals(Enchantment.CHANNELING) || enchantment.equals(Enchantment.RIPTIDE))
			return 3;
		if (enchantment.equals(Enchantment.PROTECTION) || enchantment.equals(Enchantment.BLAST_PROTECTION)
				|| enchantment.equals(Enchantment.PROJECTILE_PROTECTION)
				|| enchantment.equals(Enchantment.FEATHER_FALLING) || enchantment.equals(Enchantment.SHARPNESS)
				|| enchantment.equals(Enchantment.EFFICIENCY) || enchantment.equals(Enchantment.UNBREAKING)
				|| enchantment.equals(Enchantment.POWER))
			return 5;
		if (enchantment.equals(Enchantment.FORTUNE) || enchantment.equals(Enchantment.THORNS)
				|| enchantment.equals(Enchantment.IMPALING) || enchantment.equals(Enchantment.QUICK_CHARGE))
			return 3;
		if (enchantment.equals(Enchantment.KNOCKBACK) || enchantment.equals(Enchantment.SWEEPING_EDGE)
				|| enchantment.equals(Enchantment.PIERCING) || enchantment.equals(Enchantment.MULTISHOT))
			return 2;
		if (enchantment.equals(Enchantment.LOYALTY) || enchantment.equals(Enchantment.PUNCH))
			return 3;

		return 1; // Default for unknown or custom enchantments
	}

	@EventHandler
	public static void onPlayerJoin(PlayerJoinEvent event) {
	    Player player = event.getPlayer();
	    String path = "playerData." + player.getUniqueId().toString() + ".difficulty";
	    
	    int storedDifficulty = (PlayerData.get(path) != null) ? (int) PlayerData.get(path) : 1;

	    PlayerStats stats = playerStats.computeIfAbsent(player, p -> new PlayerStats());
	    stats.difficultyLevel = storedDifficulty;

	    player.setPlayerListName(Utils.color("&7Lvl " + storedDifficulty + "&r " + player.getName()));

	    player.sendMessage("§8[§6AdaptiveDifficulty§8] §aYour current difficulty level is §e" + storedDifficulty);
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

		// Ensure the entity is a valid hostile mob
		if (!Utils.HOSTILE_MOBS.contains(entity.getType()))
			return;

		// Ensure the killer is a player
		if (!(event.getEntity().getKiller() instanceof Player player))
			return;

		// Track player stats for kills
		PlayerStats stats = playerStats.computeIfAbsent(player, p -> new PlayerStats());
		stats.mobKills++;
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

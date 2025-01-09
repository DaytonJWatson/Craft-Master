package com.watsonllc.craft.logic;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Config;
import com.watsonllc.craft.customitems.BloodMoonMace;

public class BloodMoon {

	public static boolean isActive;

	private static final int SUNDOWN = 13000;
	private static final int SUNRISE = 23000;

	private static final double BLOODMOON_CHANCE = Config.getDouble("bloodMoon.chance");

	public static void initialize() {
		eventCheck();
	}

	private static void eventCheck() {
		Main.debug("Starting Blood Moon event checker");
		Bukkit.getScheduler().runTaskTimer(Main.instance, new Runnable() {
			@Override
			public void run() {
				World world = Bukkit.getWorlds().get(0);
				long time = world.getTime();

				Random random = new Random();
				double chance = random.nextDouble();

				if (time == SUNDOWN) {
					if (chance <= BLOODMOON_CHANCE)
						activateBloodMoon();
				}

				if (time == SUNRISE) {
					if (isActive)
						deactivateBloodMoon();
				}
			}
		}, 1L, 1L);
	}

	private static void activateBloodMoon() {
		isActive = true;
		Main.debug("A Blood Moon event is starting");
		Bukkit.broadcastMessage(ChatColor.RED + "The Blood Moon has risen! Prepare yourself!");
	}

	private static void deactivateBloodMoon() {
		isActive = false;
		Main.debug("A Blood Moon event has finished");
		Bukkit.broadcastMessage(ChatColor.GREEN + "The Blood Moon has ended. You survived... for now.");
	}

	public static void increaseMobSpawnRate(CreatureSpawnEvent event) {
        if (!isActive) return;

        World world = event.getLocation().getWorld();
        if (world == null || !world.equals(Bukkit.getWorlds().get(0))) return;

        if (!Utils.HOSTILE_MOBS.contains(event.getEntityType())) {
            event.setCancelled(true);
            return;
        }

        // Increase spawn rate during blood moon
        Random random = new Random();
        if (random.nextDouble() < 0.5) {
            Entity extraMob = world.spawnEntity(event.getLocation(), event.getEntityType());

            // Only apply changes to the additional mob
            if (extraMob instanceof LivingEntity livingEntity) {
                livingEntity.setMetadata("BloodMoonMob", new FixedMetadataValue(Main.instance, true));
                livingEntity.setCustomName("§cBlood Moon Spawn");
                livingEntity.setCustomNameVisible(true);

                // Add extra health
                double extraHealth = 40.0;
                livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(extraHealth);
                livingEntity.setHealth(extraHealth);
            }
        }
    }
	
	public static void addMobDrops(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		
		if(!(event.getDamageSource().getCausingEntity() instanceof Player))
			return;

        // Check if the mob has the metadata or is marked as a Blood Moon mob
        if (entity.hasMetadata("BloodMoonMob")) {
            // Add custom drops
            Random random = new Random();
            if (random.nextDouble() < Config.getDouble("bloodMoon.maceChance")) {
                event.getDrops().add(BloodMoonMace.item());
            }
        }
	}
}
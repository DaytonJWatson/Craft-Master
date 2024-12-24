package com.watsonllc.craft.events.entity;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.watsonllc.craft.config.Config;

public class EntitySpawn implements Listener {
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
	    if (shouldCancelSpawn(event.getEntityType()))
	        event.setCancelled(true);
	}

	/**
	 * Determines if the spawn of a specific entity type should be cancelled.
	 * 
	 * <p>This method checks the entity type against the list of disabled mobs
	 * configured in the plugin's settings. If the entity type is found in the
	 * disabled mobs list, the spawn event should be cancelled.</p>
	 * 
	 * @param entityType The {@link EntityType} of the entity attempting to spawn.
	 * @return {@code true} if the entity spawn should be cancelled, {@code false} otherwise.
	 */
	private boolean shouldCancelSpawn(EntityType entityType) {
	    return Config.getDisabledMobs().contains(entityType);
	}
}
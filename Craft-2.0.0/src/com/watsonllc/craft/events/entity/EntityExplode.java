package com.watsonllc.craft.events.entity;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplode implements Listener {
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		handleExplosion(event);
	}

	/**
	 * Handles explosion events by preventing block destruction if the exploding
	 * entity is a Creeper or TNT.
	 * 
	 * @param event The EntityExplodeEvent to process.
	 */
	private void handleExplosion(EntityExplodeEvent event) {
		if (event.getEntity() instanceof Creeper || event.getEntity() instanceof TNTPrimed) {
			event.blockList().clear();
		}
	}
}

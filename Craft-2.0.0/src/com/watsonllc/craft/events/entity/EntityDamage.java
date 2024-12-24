package com.watsonllc.craft.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.watsonllc.craft.logic.Spawn;

public class EntityDamage implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Spawn.teleportFromVoid(event);
	}
}
package com.watsonllc.craft.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.watsonllc.craft.logic.AdaptiveDifficulty;
import com.watsonllc.craft.logic.BloodMoon;

public class EntityDeath implements Listener {

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		AdaptiveDifficulty.onPlayerKill(event);
		BloodMoon.addMobDrops(event);
	}	
}
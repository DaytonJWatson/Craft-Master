package com.watsonllc.craft.events.entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.watsonllc.craft.logic.AdaptiveDifficulty;
import com.watsonllc.craft.logic.BloodMoon;

public class CreatureSpawn implements Listener {

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		BloodMoon.increaseMobSpawnRate(event);
		AdaptiveDifficulty.onMobSpawn(event);
	}
}
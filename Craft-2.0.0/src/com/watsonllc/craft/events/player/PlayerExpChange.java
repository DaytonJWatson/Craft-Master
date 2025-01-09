package com.watsonllc.craft.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import com.watsonllc.craft.logic.AdaptiveDifficulty;

public class PlayerExpChange implements Listener {

	@EventHandler
	public void onExpChange(PlayerExpChangeEvent event) {
		AdaptiveDifficulty.onExperienceGain(event);
	}
}

package com.watsonllc.craft.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.logic.DiscordBot;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        handlePlayerDeath(event);
    }

    /**
     * Handles the player death event by sending a message to Discord and modifying the death message.
     * 
     * @param event The PlayerDeathEvent to process.
     */
    private void handlePlayerDeath(PlayerDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        DiscordBot.deathMessage(event);
        event.setDeathMessage(Utils.color("&c" + event.getDeathMessage()));
    }
}

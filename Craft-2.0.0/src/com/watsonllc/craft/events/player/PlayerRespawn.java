package com.watsonllc.craft.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.watsonllc.craft.logic.Spawn;

public class PlayerRespawn implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        handlePlayerRespawn(event);
    }

    /**
     * Handles the player respawn event by teleporting the player to the spawn location
     * if no custom respawn location is set.
     * 
     * @param event The PlayerRespawnEvent to process.
     */
    private void handlePlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        teleportToSpawn(player, event);
    }

    /**
     * Teleports the player to the spawn location if no specific respawn location is set.
     * 
     * @param player The player who respawned.
     * @param event The PlayerRespawnEvent to modify the respawn location.
     */
    private void teleportToSpawn(Player player, PlayerRespawnEvent event) {
        if (player.getRespawnLocation() != null) {
            return;
        }

        if (Spawn.isNull()) {
            return;
        }

        event.setRespawnLocation(Spawn.location());
    }
}
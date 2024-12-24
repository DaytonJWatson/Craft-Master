package com.watsonllc.craft.events.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import com.watsonllc.craft.logic.PVP;

/**
 * Listener that handles entity damage events where a player attacks another player.
 * PvP damage is allowed only if both players have PvP enabled.
 */
public class EntityDamageByEntity implements Listener {

    /**
     * Handles damage events between two players.
     * Cancels the event if PvP is not enabled for either the victim or attacker.
     *
     * @param event The EntityDamageByEntityEvent triggered when an entity damages another.
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Ensure both entities involved are players
        if (!(event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker)) {
            return;
        }

        // Cancel event if PvP is not allowed
        if (!PVP.canPvp(victim, attacker)) {
            event.setCancelled(true);
        }
    }
}

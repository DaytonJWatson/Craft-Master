package com.watsonllc.craft.events.entity;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.watsonllc.craft.customitems.BloodMoonMace;
import com.watsonllc.craft.logic.PVP;

/**
 * Listener that handles entity damage events where a player attacks another player.
 * PvP damage is allowed only if both players have PvP enabled.
 */
public class EntityDamageByEntity implements Listener {

    /**
     * Handles damage events between entities.
     * Cancels the event if PvP is not enabled for either the victim or the attacker.
     *
     * @param event The EntityDamageByEntityEvent triggered when an entity damages another.
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        BloodMoonMace.attributes(event);

        if (!(event.getEntity() instanceof Player victim)) {
            return;
        }

        if (event.getDamager() instanceof Player attacker) {
            if (!PVP.canPvp(victim, attacker)) {
                event.setCancelled(true);
            }
            return;
        }

        if (event.getDamager() instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player attacker) {
                if (!PVP.canPvp(victim, attacker)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
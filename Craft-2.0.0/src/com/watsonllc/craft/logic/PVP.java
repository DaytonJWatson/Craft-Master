package com.watsonllc.craft.logic;

import java.util.UUID;
import org.bukkit.entity.Player;
import com.watsonllc.craft.Main;

/**
 * Manages PvP (Player versus Player) interactions for players on the server.
 * This class handles enabling, disabling, and checking PvP status for players
 * based on their UUIDs.
 */
public class PVP {

    /**
     * Checks if two players can engage in PvP.
     * PvP is allowed only if both the victim and the attacker have PvP enabled.
     *
     * @param victim   The player being attacked.
     * @param attacker The player initiating the attack.
     * @return true if both players can PvP, false otherwise.
     */
    public static boolean canPvp(Player victim, Player attacker) {
        UUID victimUUID = victim.getUniqueId();
        UUID attackerUUID = attacker.getUniqueId();
        return Main.pvpMap.getOrDefault(victimUUID, false) && Main.pvpMap.getOrDefault(attackerUUID, false);
    }

    /**
     * Enables PvP for a specific player by UUID.
     *
     * @param uuid The UUID of the player.
     */
    public static void enablePvp(UUID uuid) {
        Main.pvpMap.put(uuid, true);
    }

    /**
     * Disables PvP for a specific player by UUID.
     *
     * @param uuid The UUID of the player.
     */
    public static void disablePvp(UUID uuid) {
        Main.pvpMap.put(uuid, false);
    }

    /**
     * Removes the PvP status of a player by UUID.
     * Logs the removal process to the server console.
     *
     * @param uuid The UUID of the player.
     */
    public static void removePvp(UUID uuid) {
        if (Main.pvpMap.remove(uuid) != null) {
            Main.instance.getLogger().info("Removed " + uuid + " from pvpMap");
        }
    }

    /**
     * Checks if a player can engage in PvP based on their UUID.
     *
     * @param uuid The UUID of the player.
     * @return true if PvP is enabled for the player, false otherwise.
     */
    public static boolean canPvp(UUID uuid) {
        return Main.pvpMap.getOrDefault(uuid, false);
    }
}

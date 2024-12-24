package com.watsonllc.craft.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.logic.DiscordBot;
import com.watsonllc.craft.logic.PVP;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        handlePlayerQuit(event);
    }

    /**
     * Handles the player quit event by sending a message to Discord, removing PVP status,
     * and resetting player invisibility and interaction states.
     * 
     * @param event The PlayerQuitEvent to process.
     */
    private void handlePlayerQuit(PlayerQuitEvent event) {
        if (event == null || event.getPlayer() == null) {
            return;
        }

        Player player = event.getPlayer();
        quit(player, event);
        DiscordBot.quitMessage(event);
    }

    /**
     * Handles the quit logic for the player, including setting quit messages,
     * updating PVP status, and resetting invisibility.
     * 
     * @param player The player who quit the server.
     * @param event The PlayerQuitEvent to modify.
     */
    private void quit(Player player, PlayerQuitEvent event) {
        String quit = Utils.color("&7%player% &cquit");
        quit = quit.replace("%player%", player.getDisplayName());
        PVP.removePvp(player.getUniqueId());
        event.setQuitMessage(quit);

        if (player.isInvisible()) {
            player.setInvisible(false);
            player.setCanPickupItems(true);
            player.setCollidable(true);
        }
    }
}

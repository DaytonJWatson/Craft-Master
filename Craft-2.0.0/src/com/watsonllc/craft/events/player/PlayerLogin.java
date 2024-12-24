package com.watsonllc.craft.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Config;
import com.watsonllc.craft.config.PlayerData;

public class PlayerLogin implements Listener {

    private final String discordInvite = Config.getString("discordInvite");

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        handlePlayerLogin(event);
    }

    /**
     * Handles the player login event by checking if the player is banned.
     * If banned, the player is disallowed from logging in with a message including the ban reason.
     * 
     * @param event The PlayerLoginEvent to process.
     */
    private void handlePlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        if (PlayerData.isBanned(player.getUniqueId().toString())) {
            String reason = PlayerData.getString("playerData." + player.getUniqueId().toString() + ".banned.reason");
            event.disallow(Result.KICK_BANNED, Utils.color("&cYou have been banned from this server. Reason: " + reason + ". You can appeal your ban on discord: " + discordInvite));
        }
    }
}
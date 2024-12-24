package com.watsonllc.craft.events.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.watsonllc.craft.config.Config;

public class ServerPing implements Listener {

    private final long days = Config.getUptime("days");
    private final long hours = Config.getUptime("hours");
    private final long minutes = Config.getUptime("minutes");
    private final long seconds = Config.getUptime("seconds");

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        handleServerPing(event);
    }

    /**
     * Handles the server list ping event by modifying the server MOTD to display uptime and other dynamic values.
     * 
     * @param event The ServerListPingEvent to process.
     */
    private void handleServerPing(ServerListPingEvent event) {
        String MOTD = event.getMotd();
        MOTD = MOTD.replace("%uptime%", Config.getUptime());
        MOTD = MOTD.replace("%days%", String.valueOf(days));
        MOTD = MOTD.replace("%hours%", String.valueOf(hours));
        MOTD = MOTD.replace("%minutes%", String.valueOf(minutes));
        MOTD = MOTD.replace("%seconds%", String.valueOf(seconds));
        MOTD = MOTD.replace("%unique%", String.valueOf(Config.getInt("unique")));
        event.setMotd(MOTD);
    }
}
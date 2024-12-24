package com.watsonllc.craft.events;

import org.bukkit.plugin.PluginManager;
import com.watsonllc.craft.Main;
import com.watsonllc.craft.events.entity.*;
import com.watsonllc.craft.events.player.*;
import com.watsonllc.craft.events.server.ServerPing;

/**
 * Handles the registration of all event listeners within the plugin.
 * This class ensures that all relevant event handlers are properly registered
 * when the plugin is initialized.
 */
public class Events {

    /**
     * Registers all event listeners with the server's plugin manager.
     * This method should be called during the plugin's initialization phase.
     */
    public static void setup() {
        PluginManager pm = Main.instance.getServer().getPluginManager();

        // Register entity-related event listeners
        pm.registerEvents(new EntityDamage(), Main.instance);
        pm.registerEvents(new EntityDamageByEntity(), Main.instance);
        pm.registerEvents(new EntityExplode(), Main.instance);
        pm.registerEvents(new EntitySpawn(), Main.instance);

        // Register player-related event listeners
        pm.registerEvents(new BlockBreak(), Main.instance);
        pm.registerEvents(new PlayerChat(), Main.instance);
        pm.registerEvents(new PlayerDeath(), Main.instance);
        pm.registerEvents(new PlayerJoin(), Main.instance);
        pm.registerEvents(new PlayerLogin(), Main.instance);
        pm.registerEvents(new PlayerQuit(), Main.instance);
        pm.registerEvents(new PlayerRespawn(), Main.instance);

        // Register server-related event listeners
        pm.registerEvents(new ServerPing(), Main.instance);
    }
}

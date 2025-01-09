package com.watsonllc.craft.events;

import org.bukkit.plugin.PluginManager;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.events.entity.CreatureSpawn;
import com.watsonllc.craft.events.entity.EntityDamage;
import com.watsonllc.craft.events.entity.EntityDamageByEntity;
import com.watsonllc.craft.events.entity.EntityDeath;
import com.watsonllc.craft.events.entity.EntityExplode;
import com.watsonllc.craft.events.entity.EntitySpawn;
import com.watsonllc.craft.events.player.BlockBreak;
import com.watsonllc.craft.events.player.InventoryClick;
import com.watsonllc.craft.events.player.InventoryClose;
import com.watsonllc.craft.events.player.PlayerChat;
import com.watsonllc.craft.events.player.PlayerDeath;
import com.watsonllc.craft.events.player.PlayerExpChange;
import com.watsonllc.craft.events.player.PlayerJoin;
import com.watsonllc.craft.events.player.PlayerLogin;
import com.watsonllc.craft.events.player.PlayerMove;
import com.watsonllc.craft.events.player.PlayerQuit;
import com.watsonllc.craft.events.player.PlayerRespawn;
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
        pm.registerEvents(new CreatureSpawn(), Main.instance);
        pm.registerEvents(new EntityDamage(), Main.instance);
        pm.registerEvents(new EntityDamageByEntity(), Main.instance);
        pm.registerEvents(new EntityDeath(), Main.instance);
        pm.registerEvents(new EntityExplode(), Main.instance);
        pm.registerEvents(new EntitySpawn(), Main.instance);

        // Register player-related event listeners
        pm.registerEvents(new BlockBreak(), Main.instance);
        pm.registerEvents(new InventoryClick(), Main.instance);
        pm.registerEvents(new InventoryClose(), Main.instance);
        pm.registerEvents(new PlayerChat(), Main.instance);
        pm.registerEvents(new PlayerDeath(), Main.instance);
        pm.registerEvents(new PlayerExpChange(), Main.instance);
        pm.registerEvents(new PlayerJoin(), Main.instance);
        pm.registerEvents(new PlayerLogin(), Main.instance);
        pm.registerEvents(new PlayerMove(), Main.instance);
        pm.registerEvents(new PlayerQuit(), Main.instance);
        pm.registerEvents(new PlayerRespawn(), Main.instance);

        // Register server-related event listeners
        pm.registerEvents(new ServerPing(), Main.instance);
    }
}

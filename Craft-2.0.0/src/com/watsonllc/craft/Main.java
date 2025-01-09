package com.watsonllc.craft;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.watsonllc.craft.commands.Commands;
import com.watsonllc.craft.config.Config;
import com.watsonllc.craft.events.Events;
import com.watsonllc.craft.logic.AdaptiveDifficulty;
import com.watsonllc.craft.logic.Announcer;
import com.watsonllc.craft.logic.BloodMoon;
import com.watsonllc.craft.logic.NameTags;
import com.watsonllc.craft.logic.RestartWarning;

/**
 * Main entry point for the Minecraft plugin.
 * This class initializes key components, event listeners, commands, and background tasks.
 */
public class Main extends JavaPlugin {

    /** Singleton instance of the plugin. */
    public static Main instance;

    /** Tracks player PvP status. */
    public static HashMap<UUID, Boolean> pvpMap;

    /** Tracks the last teleport time of players to manage cooldowns. */
    public static HashMap<UUID, Long> lastTeleportTime;

    /** Stores active teleport requests between players. */
    public static HashMap<UUID, UUID> teleportRequests;

    /** Name of the plugin. */
    public static String name;

    /** Version of the plugin. */
    public static String version;

    /** Reference to the main world. */
    public static World world;

    /** Reference to the Nether world. */
    public static World netherWorld;

    /**
     * Called when the plugin is enabled. Initializes necessary configurations,
     * commands, events, and scheduled tasks.
     */
    @Override
    public void onEnable() {
        instance = this;
        pvpMap = new HashMap<>();
        lastTeleportTime = new HashMap<>();
        teleportRequests = new HashMap<>();

        name = Main.instance.getDescription().getName();
        version = Main.instance.getDescription().getVersion();

        // Load the main world and nether world
        world = getServer().getWorlds().get(0);
        netherWorld = getServer().getWorld(world.getName()+ "_nether");

        // Initialize commands, configurations, and events
        Commands.setup();
        Config.setup();
        Events.setup();

        // Start background tasks for announcements and restart warnings
        Announcer.startBroadcastTask();
        RestartWarning.startRestartWarningTask();
        
        // Start BloodMoon
        if(Config.getBoolean("bloodMoon.enabled"))
        	BloodMoon.initialize();
        
        // Start the potentially super laggy no nametags
        if(Config.getBoolean("nameTags.enabled"))
        	NameTags.initialize();
    
        AdaptiveDifficulty.startDifficultyTimer();
    }
    
	public static void warning(String string) {
		instance.getLogger().warning(string);
	}
	
	public static void debug(String string) {
		instance.getLogger().info(string);
	}
}
package com.watsonllc.craft.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.EntityType;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

/**
 * The Config class handles the loading, saving, and management of configuration data
 * for the plugin. It provides utility methods for accessing configuration values,
 * managing disabled entities, and tracking uptime since the plugin was first installed.
 */
public class Config {
    // List of disabled mob types retrieved from the config file
    private static List<EntityType> disabledMobs;

    /**
     * Initializes and sets up the configuration by creating necessary defaults and
     * loading additional configuration sections such as groups, homes, and player data.
     */
    public static void setup() {
        create();
        Groups.create();
        Homes.create();
        PlayerData.create();

        loadDisabledMobs();
    }

    /**
     * Loads the list of disabled mob types from the configuration file.
     * Invalid entity types are logged and skipped.
     */
    private static void loadDisabledMobs() {
        disabledMobs = Main.instance.getConfig().getStringList("disabledMobs").stream()
            .map(Config::getEntityType)
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toList());
    }

    /**
     * Converts a string to an EntityType. Logs an error if the entity type is invalid.
     *
     * @param typeName The name of the entity type.
     * @return The corresponding EntityType, or null if invalid.
     */
    private static EntityType getEntityType(String typeName) {
        try {
            return EntityType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            Main.instance.getLogger().severe("Invalid EntityType in config: " + typeName);
            return null;
        }
    }

    /**
     * Retrieves the list of disabled mob types.
     *
     * @return A list of EntityTypes representing disabled mobs.
     */
    public static List<EntityType> getDisabledMobs() {
        return disabledMobs;
    }

    /**
     * Creates the default configuration if it does not already exist and initializes uptime.
     */
    private static void create() {
        Main.instance.getConfig().options().copyDefaults(true);
        Main.instance.saveDefaultConfig();

        uptime();
    }

    /**
     * Saves the current state of the configuration to the file.
     */
    public static void save() {
        Main.instance.saveConfig();
    }

    /**
     * Reloads the configuration from the file.
     */
    public static void reload() {
        Main.instance.reloadConfig();
    }

    /**
     * Sets a configuration value at the specified path.
     *
     * @param path The path in the configuration.
     * @param obj  The value to set.
     */
    public static void set(String path, Object obj) {
        Main.instance.getConfig().set(path, obj);
    }

    /**
     * Retrieves a configuration value from the specified path.
     *
     * @param path The path in the configuration.
     * @return The object at the specified path.
     */
    public static Object get(String path) {
        return Main.instance.getConfig().get(path);
    }

    /**
     * Retrieves a string value from the configuration, with color codes translated.
     *
     * @param path The path in the configuration.
     * @return A formatted string with colors applied.
     */
    public static String getString(String path) {
        return Utils.color(Main.instance.getConfig().getString(path));
    }
    
	public static List<String> getStringList(String path) {
		return Main.instance.getConfig().getStringList(path);
	}

    /**
     * Retrieves an integer value from the configuration.
     *
     * @param path The path in the configuration.
     * @return The integer value.
     */
    public static int getInt(String path) {
        return Main.instance.getConfig().getInt(path);
    }

    /**
     * Retrieves a double value from the configuration.
     *
     * @param path The path in the configuration.
     * @return The double value.
     */
    public static double getDouble(String path) {
        return Main.instance.getConfig().getDouble(path);
    }

    /**
     * Retrieves a boolean value from the configuration.
     *
     * @param path The path in the configuration.
     * @return The boolean value.
     */
    public static boolean getBoolean(String path) {
        return Main.instance.getConfig().getBoolean(path);
    }

    /**
     * Retrieves the uptime duration in a human-readable format.
     *
     * @return A string representing the duration since the first install.
     */
    public static String getUptime() {
        String installDate = Main.instance.getConfig().getString("uptime");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime installDateTime = LocalDateTime.parse(installDate, formatter);
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(installDateTime, now);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;

        return days + " days, " + hours + " hours, and " + minutes + " minutes";
    }

    /**
     * Retrieves uptime in the specified unit (days, hours, minutes, or seconds).
     *
     * @param value The time unit to retrieve.
     * @return The uptime in the specified unit.
     */
    public static long getUptime(String value) {
        String installDate = Main.instance.getConfig().getString("uptime");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime installDateTime = LocalDateTime.parse(installDate, formatter);
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(installDateTime, now);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        switch (value.toLowerCase()) {
            case "days": return days;
            case "hours": return hours;
            case "minutes": return minutes;
            case "seconds": return seconds;
            default: return 999;
        }
    }

    /**
     * Sets the initial installation date if not already set. Logs the install date.
     */
    private static void uptime() {
        if (!Main.instance.getConfig().contains("uptime")) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedNow = now.format(formatter);
            
            Main.instance.getConfig().set("uptime", formattedNow);
            save();
            
            Main.instance.getLogger().info("First install date set to: " + formattedNow);
        } else {
            Main.instance.getLogger().info("Successfully loaded install date");
        }
    }
}

package com.watsonllc.craft.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

/**
 * Manages group configurations stored in a YAML file.
 * This class provides methods to create, load, save, and manipulate group data.
 */
public class Groups {
    // File path to the groups.yml configuration file
    private static File groupsFile = new File(Main.instance.getDataFolder(), "groups.yml");
    // YamlConfiguration object to handle reading and writing of the YAML file
    private static YamlConfiguration groups = YamlConfiguration.loadConfiguration(groupsFile);

    /**
     * Creates the groups.yml file if it does not already exist.
     * If the file exists, the method exits early.
     */
    public static void create() {
        if (groupsFile.exists()) {
            return; // Exit if the file already exists
        }
        setDefaults(); // Set default group values if file is created
    }

    /**
     * Saves the current state of the groups configuration to the groups.yml file.
     */
    public static void save() {
        try {
            groups.save(groupsFile);
        } catch (IOException e) {
            e.printStackTrace(); // Log error if saving fails
        }
    }

    /**
     * Reloads the groups.yml file, refreshing the configuration in memory.
     */
    public static void reload() {
        try {
            groups.load(groupsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace(); // Log error if reloading fails
        }
    }

    /**
     * Sets default group configurations in the YAML file.
     * This is called when the file is first created.
     */
    private static void setDefaults() {
        setString("groups.default.prefix", "&7[&6Default&7]");
        setString("groups.founder.prefix", "&7[&6Founder&7]");
        setString("groups.moderator.prefix", "&7[&cModerator&7]");
        setString("groups.administrator.prefix", "&7[&cAdministrator&7]");
        save(); // Save defaults immediately after setting them
    }

    /**
     * Sets a configuration value at the specified path.
     * @param path The path in the YAML configuration.
     * @param obj  The object to set at the specified path.
     */
    public static void set(String path, Object obj) {
        groups.set(path, obj);
    }

    /**
     * Sets a string value at the specified path.
     * @param path   The path in the YAML configuration.
     * @param string The string to set at the specified path.
     */
    public static void setString(String path, String string) {
        groups.set(path, string);
    }

    /**
     * Sets a list of strings at the specified path.
     * @param path        The path in the YAML configuration.
     * @param stringList  The list of strings to set.
     */
    public static void setStringList(String path, List<String> stringList) {
        groups.set(path, stringList);
    }

    /**
     * Retrieves an object from the configuration at the specified path.
     * @param path The path to retrieve the object from.
     * @return The object at the specified path, or null if not found.
     */
    public static Object get(String path) {
        return groups.get(path);
    }

    /**
     * Retrieves a colored string from the configuration at the specified path.
     * Uses the Utils class to apply color codes.
     * @param path The path to retrieve the string from.
     * @return The formatted string or null if not found.
     */
    public static String getString(String path) {
        return Utils.color(groups.getString(path));
    }

    /**
     * Retrieves a list of strings from the configuration at the specified path.
     * @param path The path to retrieve the list from.
     * @return A list of strings, or an empty list if the path does not exist.
     */
    public static List<String> getStringList(String path) {
        return groups.getStringList(path);
    }
}

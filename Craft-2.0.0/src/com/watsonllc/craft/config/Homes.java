package com.watsonllc.craft.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Main;

/**
 * Manages player home locations stored in a YAML file.
 * Provides methods to create, save, delete, and retrieve player-set homes.
 */
public class Homes {
    // File path to the homes.yml configuration file
    private static File homesFile = new File(Main.instance.getDataFolder(), "homes.yml");
    // YamlConfiguration object for handling the YAML file
    private static YamlConfiguration homes = YamlConfiguration.loadConfiguration(homesFile);
    // Maximum number of homes a player can set
    private static int maxHomes = 4;

    /**
     * Creates the homes.yml file if it does not exist.
     */
    public static void create() {
        if (homesFile.exists()) {
            return; // Exit if the file already exists
        }
        save(); // Save initial empty configuration
    }

    /**
     * Saves the current state of the homes configuration to the homes.yml file.
     */
    public static void save() {
        try {
            homes.save(homesFile);
        } catch (IOException e) {
            e.printStackTrace(); // Log error if saving fails
        }
    }

    /**
     * Reloads the homes.yml file, refreshing the configuration in memory.
     */
    public static void reload() {
        try {
            homes.load(homesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace(); // Log error if reloading fails
        }
    }

    /**
     * Sets a home location for a player with a specified name.
     * @param player   The player setting the home.
     * @param homeName The name of the home.
     * @return true if the home was set successfully, false if the player has reached the maximum home limit.
     */
    public static boolean setHome(Player player, String homeName) {
        String UUID = player.getUniqueId().toString();
        String PATH = "homes." + UUID + "." + homeName;

        if (getHomeCount(player) >= maxHomes) {
            return false; // Exceeds maximum number of homes
        }

        Location loc = player.getLocation();

        homes.set(PATH + ".world", loc.getWorld().getName());
        homes.set(PATH + ".x", loc.getX());
        homes.set(PATH + ".y", loc.getY());
        homes.set(PATH + ".z", loc.getZ());
        homes.set(PATH + ".yaw", loc.getYaw());
        homes.set(PATH + ".pitch", loc.getPitch());
        save();
        return true;
    }

    /**
     * Retrieves the location of a specified home for a player.
     * @param player   The player whose home is being retrieved.
     * @param homeName The name of the home.
     * @return The Location object of the home, or null if the home does not exist.
     */
    public static Location getHome(Player player, String homeName) {
        String UUID = player.getUniqueId().toString();
        String PATH = "homes." + UUID + "." + homeName;

        if (homes.get(PATH) == null) {
            return null;
        }

        World world = Bukkit.getWorld(homes.getString(PATH + ".world"));
        double x = homes.getDouble(PATH + ".x");
        double y = homes.getDouble(PATH + ".y");
        double z = homes.getDouble(PATH + ".z");
        float yaw = (float) homes.getDouble(PATH + ".yaw");
        float pitch = (float) homes.getDouble(PATH + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Deletes a specified home for a player.
     * @param player   The player whose home is being deleted.
     * @param homeName The name of the home.
     */
    public static void delHome(Player player, String homeName) {
        String UUID = player.getUniqueId().toString();
        String PATH = "homes." + UUID + "." + homeName;

        if (homes.get(PATH) == null) {
            return; // Exit if the home does not exist
        }

        homes.set(PATH, null); // Remove the home entry
        save();
    }

    /**
     * Checks if a player has a home with the specified name.
     * @param player   The player being checked.
     * @param homeName The name of the home.
     * @return true if the player has the home, false otherwise.
     */
    public static boolean hasHome(Player player, String homeName) {
        String UUID = player.getUniqueId().toString();
        String PATH = "homes." + UUID + "." + homeName;

        return homes.get(PATH) != null;
    }

    /**
     * Gets the count of homes a player has set.
     * @param player The player whose homes are being counted.
     * @return The number of homes the player has set.
     */
    public static int getHomeCount(Player player) {
        String UUID = player.getUniqueId().toString();
        String PATH = "homes." + UUID;

        if (homes.getConfigurationSection(PATH) == null) {
            return 0;
        }

        return homes.getConfigurationSection(PATH).getKeys(false).size();
    }

    /**
     * Retrieves a list of home names set by the player.
     * @param player The player whose home list is being retrieved.
     * @return A list of home names, or null if the player has no homes set.
     */
    public static List<String> getHomeList(Player player) {
        String UUID = player.getUniqueId().toString();
        String PATH = "homes." + UUID;

        if (homes.getConfigurationSection(PATH) == null) {
            return new ArrayList<>();  // Return empty list instead of null
        }

        return new ArrayList<>(homes.getConfigurationSection(PATH).getKeys(false));
    }
}

package com.watsonllc.craft.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

/**
 * Manages player-specific data including groups, nicknames, bans, and warnings.
 * This class handles saving, loading, and modifying player data stored in YAML format.
 */
public class PlayerData {

    private static File playerDataFile = new File(Main.instance.getDataFolder(), "playerData.yml");
    public static YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);

    /**
     * Creates the player data file if it does not already exist.
     */
    public static void create() {
        if (playerDataFile.exists())
            return;
        save();
    }

    /**
     * Saves the player data to the file system.
     */
    public static void save() {
        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reloads the player data from the file.
     */
    public static void reload() {
        try {
            playerData.load(playerDataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves an object from the player data.
     *
     * @param path The path to the data.
     * @return The object at the specified path.
     */
    public static Object get(String path) {
        return playerData.get(path);
    }

    /**
     * Retrieves a string from the player data and applies color formatting.
     *
     * @param path The path to the data.
     * @return The formatted string.
     */
    public static String getString(String path) {
        return Utils.color(playerData.getString(path));
    }

    /**
     * Retrieves the difficulty level of the specified player.
     *
     * @param player The player whose difficulty is being queried.
     * @return The difficulty level as a string.
     */
    public static String getDifficulty(Player player) {
        return playerData.getString("playerData." + player.getUniqueId().toString() + ".difficulty");
    }

    /**
     * Retrieves the group of the specified player.
     *
     * @param player The player whose group is being queried.
     * @return The group of the player.
     */
    public static String getGroup(Player player) {
        return playerData.getString("playerData." + player.getUniqueId().toString() + ".group");
    }

    /**
     * Sets the group of the specified player and saves the data.
     *
     * @param player The player whose group is being set.
     * @param group  The new group to assign to the player.
     */
    public static void setGroup(Player player, String group) {
        String UUID = player.getUniqueId().toString();
        String PATH = "playerData." + UUID + ".";
        set(PATH + "group", group);
        save();
    }

    /**
     * Sets a value at the specified path in the player data.
     *
     * @param path The path to set the value.
     * @param obj  The value to set.
     */
    public static void set(String path, Object obj) {
        playerData.set(path, obj);
        save();
    }

    /**
     * Updates the nickname of the specified player.
     *
     * @param player   The player whose nickname is being updated.
     * @param nickname The new nickname to assign.
     */
    public static void updateNickname(Player player, String nickname) {
        String UUID = player.getUniqueId().toString();
        String PATH = "playerData." + UUID + ".";
        set(PATH + "nickname", nickname);
        save();
        Main.instance.getLogger().info("Updating '" + player.getName() + "' nickname to: " + nickname);
    }

    /**
     * Checks if the specified player has a nickname.
     *
     * @param player The player to check.
     * @return The nickname if it exists, otherwise returns null.
     */
    public static String hasNickname(Player player) {
        String UUID = player.getUniqueId().toString();
        String PATH = "playerData." + UUID + ".nickname";
        return getString(PATH);
    }

    /**
     * Creates initial player data for the specified player.
     *
     * @param player The player to initialize data for.
     */
    public static void createData(Player player) {
        String UUID = player.getUniqueId().toString();
        String PATH = "playerData." + UUID + ".";
        set(PATH + "username", player.getName());
        set(PATH + "group", "default");
        set(PATH + "difficulty", "NORMAL");
        save();
    }

    /**
     * Checks if the player data exists for the specified player.
     *
     * @param player The player to check.
     * @return true if no data exists, false otherwise.
     */
    public static boolean dataNull(Player player) {
        String UUID = player.getUniqueId().toString();
        String PATH = "playerData." + UUID;
        return !playerData.contains(PATH);
    }

    /**
     * Checks if the specified player is banned.
     *
     * @param uuid The UUID of the player.
     * @return true if the player is banned, false otherwise.
     */
    public static boolean isBanned(String uuid) {
        String PATH = "playerData." + uuid + ".banned";
        return playerData.contains(PATH);
    }

    /**
     * Adds a warning to the specified player's data.
     *
     * @param uuid    The UUID of the player.
     * @param warning The warning message to add.
     */
    public static void addWarning(String uuid, String warning) {
        String PATH = "playerData." + uuid + ".warnings";
        List<String> warnings = playerData.getStringList(PATH);
        warnings.add(warning);
        playerData.set(PATH, warnings);
        save();
    }

    /**
     * Retrieves the list of warnings for the specified player.
     *
     * @param uuid The UUID of the player.
     * @return A list of warnings associated with the player.
     */
    public static List<String> getWarnings(String uuid) {
        String PATH = "playerData." + uuid + ".warnings";
        return playerData.getStringList(PATH);
    }
    
    /**
     * Mutes a player by setting their mute status to true and recording the reason.
     *
     * @param uuid   The UUID of the player to mute.
     * @param reason The reason for muting the player.
     */
    public static void mutePlayer(String uuid, String reason) {
        String PATH = "playerData." + uuid + ".muted";
        playerData.set(PATH + ".status", true);
        playerData.set(PATH + ".reason", reason);
        save();
    }

    /**
     * Unmutes a player by setting their mute status to false and clearing the reason.
     *
     * @param uuid The UUID of the player to unmute.
     */
    public static void unmutePlayer(String uuid) {
        String PATH = "playerData." + uuid + ".muted";
        playerData.set(PATH + ".status", false);
        playerData.set(PATH + ".reason", null);
        save();
    }

    /**
     * Checks if a player is currently muted.
     *
     * @param uuid The UUID of the player to check.
     * @return true if the player is muted, false otherwise.
     */
    public static boolean isMuted(String uuid) {
        String PATH = "playerData." + uuid + ".muted.status";
        return playerData.getBoolean(PATH, false);
    }

    /**
     * Retrieves the reason why a player is muted.
     *
     * @param uuid The UUID of the player.
     * @return The reason for the mute, or a default message if no reason is specified.
     */
    public static String getMuteReason(String uuid) {
        String PATH = "playerData." + uuid + ".muted.reason";
        return playerData.getString(PATH, "No reason specified.");
    }
}
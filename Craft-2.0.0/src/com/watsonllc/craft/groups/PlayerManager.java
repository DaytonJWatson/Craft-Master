package com.watsonllc.craft.groups;

import org.bukkit.entity.Player;
import com.watsonllc.craft.config.PlayerData;

/**
 * Manages player-specific data, including group assignments and data persistence.
 */
public class PlayerManager {

    private final Player player;
    private final String PATH;

    /**
     * Constructs a PlayerManager for a specific player.
     *
     * @param player The player to manage.
     */
    public PlayerManager(Player player) {
        this.player = player;
        this.PATH = "playerData." + player.getUniqueId() + ".";
    }

    /**
     * Sets the group of the player and saves the data.
     *
     * @param group The name of the group to assign to the player.
     */
    public void setGroup(String group) {
        PlayerData.set(PATH + "group", group);
        PlayerData.save();
    }

    /**
     * Retrieves the group assigned to the player.
     *
     * @return The name of the player's group, or "null" if no group is assigned.
     */
    public String getGroup() {
        String group = PlayerData.getGroup(player);
        return (group != null) ? group : "null";
    }

    /**
     * Retrieves the prefix of the player's group.
     *
     * @return The prefix for the player's group, or null if unavailable.
     */
    public String getPrefix() {
        String group = getGroup();
        if (group.equals("null")) {
            return null;
        }
        return GroupManager.getGroupPrefix(group);
    }
}
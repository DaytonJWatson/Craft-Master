package com.watsonllc.craft.groups;

import java.util.List;
import com.watsonllc.craft.config.Groups;

/**
 * Manages player groups and their associated permissions.
 * Provides utilities to fetch group prefixes and verify permissions.
 */
public class GroupManager {
    
    private final String group;
    private final List<String> permissions;

    /**
     * Constructs a GroupManager for a specified group.
     *
     * @param group The name of the group to manage.
     */
    public GroupManager(String group) {
        this.group = group;
        this.permissions = Groups.getStringList("groups." + group + ".permissions");
    }

    /**
     * Retrieves the prefix for a specified group.
     * This is a static method to fetch the prefix directly.
     *
     * @param group The name of the group.
     * @return The prefix of the group, or an empty string if not found.
     */
    public static String getGroupPrefix(String group) {
        String prefix = Groups.getString("groups." + group + ".prefix");
        return (prefix != null) ? prefix : "[null]";  // Return null group string if null
    }

    /**
     * Retrieves the prefix for the current group.
     *
     * @return The prefix of the group, or an empty string if not found.
     */
    public String getPrefix() {
        return getGroupPrefix(group);
    }

    /**
     * Checks if the current group has a specified permission.
     *
     * @param permission The permission to check.
     * @return true if the group has the permission, false otherwise.
     */
    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }

    /**
     * Retrieves the list of permissions for the current group.
     *
     * @return A list of permissions.
     */
    public List<String> getPermissions() {
        return permissions;
    }
}

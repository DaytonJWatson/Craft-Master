package com.watsonllc.craft.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIManager {
    private static Map<Player, GUICreator> activeGUIs = new HashMap<>();

    public static void openGUI(Player player, GUICreator gui) {
        activeGUIs.put(player, gui);
        gui.open();
    }

    public static void handleGUIClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GUICreator gui = activeGUIs.get(player);
        if (gui != null) {
            gui.handleClick(event);
        }
    }

    public static void closeGUI(Player player) {
        activeGUIs.remove(player);
    }
}
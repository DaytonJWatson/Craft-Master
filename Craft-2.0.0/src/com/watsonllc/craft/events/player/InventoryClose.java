package com.watsonllc.craft.events.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.watsonllc.craft.gui.GUIManager;

public class InventoryClose implements Listener {

	@EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        GUIManager.closeGUI((Player) event.getPlayer());
    }
}

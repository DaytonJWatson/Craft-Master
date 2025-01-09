package com.watsonllc.craft.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.watsonllc.craft.gui.GUIManager;

public class InventoryClick implements Listener {
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        GUIManager.handleGUIClick(event);
    }
}
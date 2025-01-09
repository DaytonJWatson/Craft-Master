package com.watsonllc.craft.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class GUICreator {
    protected Inventory inventory;
    protected Player player;

    public GUICreator(Player player, String title, int size) {
        this.player = player;
        this.inventory = Bukkit.createInventory(null, size, title);
        initializeItems();
    }

    public abstract void initializeItems();  // Define GUI items here

    public void open() {
        player.openInventory(inventory);
    }

    public abstract void handleClick(InventoryClickEvent event);  // Handle clicks
}
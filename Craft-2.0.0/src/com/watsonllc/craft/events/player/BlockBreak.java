package com.watsonllc.craft.events.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.watsonllc.craft.logic.TreeChop;

public class BlockBreak implements Listener {
	
    private final TreeChop tc = new TreeChop();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	tc.logic(event);
    }
}

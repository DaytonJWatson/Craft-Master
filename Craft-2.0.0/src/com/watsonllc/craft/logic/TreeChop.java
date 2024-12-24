package com.watsonllc.craft.logic;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.config.Config;

/**
 * Handles tree chopping mechanics by breaking entire trees when a log is broken.
 * Accelerates leaf decay and reduces axe durability accordingly.
 */
public class TreeChop {

    private static final boolean ENABLED = Config.getBoolean("treeChop.enabled");
    private static final int TREE_CHECK_RADIUS = Config.getInt("treeChop.treeCheckRadius");
    private static final int DECAY_DEPTH_LIMIT = Config.getInt("treeChop.decayDepthLimit");
    private static final boolean REDUCE_DURABILITY = Config.getBoolean("treeChop.reduceDurability");

    /**
     * Handles tree chopping logic when a player breaks a block.
     *
     * @param event The block break event triggered by the player.
     */
    public void logic(BlockBreakEvent event) {
        if (!ENABLED) return;

        Block block = event.getBlock();
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();

        if (!event.getPlayer().hasPermission("qol.treeChop")) return;
        if (event.getPlayer().isSneaking()) return;
        if (!isAxe(tool.getType())) return;
        if (!isLog(block.getType())) return;
        if (!isTree(block)) return;

        int brokenLogs = breakTree(block);

        if (REDUCE_DURABILITY) {
            reduceAxeDurability(tool, brokenLogs);
        }
    }

    /**
     * Checks if a material is a type of log.
     */
    public boolean isLog(Material material) {
        return material.name().endsWith("_LOG");
    }

    /**
     * Checks if a material is a type of axe.
     */
    public boolean isAxe(Material material) {
        return material.name().endsWith("_AXE");
    }

    /**
     * Checks if a material is a type of leaf.
     */
    public boolean isLeaf(Material material) {
        return material.name().endsWith("_LEAVES");
    }

    /**
     * Determines if the block is part of a tree by checking for nearby leaves.
     *
     * @param block The block to check.
     * @return true if leaves are detected within the check radius.
     */
    public boolean isTree(Block block) {
        Set<Block> visited = new HashSet<>();
        Queue<Block> toCheck = new LinkedList<>();
        toCheck.add(block);

        while (!toCheck.isEmpty()) {
            Block current = toCheck.poll();
            if (!visited.add(current)) continue;

            for (Block relative : getAdjacentBlocks(current)) {
                if (isLeaf(relative.getType()) && withinRadius(block, relative)) {
                    return true;
                }
                if (isLog(relative.getType())) {
                    toCheck.add(relative);
                }
            }
        }
        return false;
    }

    private boolean withinRadius(Block origin, Block relative) {
        return origin.getLocation().distanceSquared(relative.getLocation()) <= TREE_CHECK_RADIUS * TREE_CHECK_RADIUS;
    }

    /**
     * Breaks the entire tree starting from the given block.
     *
     * @param block The starting block.
     * @return The number of logs broken.
     */
    public int breakTree(Block block) {
        Set<Block> logs = new HashSet<>();
        findLogs(block, logs);

        for (Block log : logs) {
            log.breakNaturally();
        }

        accelerateLeafDecay(logs, DECAY_DEPTH_LIMIT);
        return logs.size();
    }

    private void findLogs(Block block, Set<Block> logs) {
        Queue<Block> toCheck = new LinkedList<>();
        toCheck.add(block);

        while (!toCheck.isEmpty()) {
            Block current = toCheck.poll();
            if (logs.add(current) && isLog(current.getType())) {
                toCheck.addAll(getAdjacentBlocks(current));
            }
        }
    }

    private Set<Block> getAdjacentBlocks(Block block) {
        Set<Block> adjacent = new HashSet<>();
        int[][] directions = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
        for (int[] dir : directions) {
            adjacent.add(block.getRelative(dir[0], dir[1], dir[2]));
        }
        return adjacent;
    }

    private void accelerateLeafDecay(Set<Block> logs, int depthLimit) {
        Set<Block> checkedLeaves = new HashSet<>();

        for (Block log : logs) {
            for (Block relative : getAdjacentBlocks(log)) {
                if (isLeaf(relative.getType()) && checkedLeaves.add(relative)) {
                    relative.setMetadata("decayDistance", new FixedMetadataValue(Main.instance, 1));
                    relative.breakNaturally();
                    checkAdjacentLeaves(relative, checkedLeaves, depthLimit - 1);
                }
            }
        }
    }

    private void checkAdjacentLeaves(Block block, Set<Block> checkedLeaves, int depthLimit) {
        if (depthLimit <= 0) return;

        for (Block relative : getAdjacentBlocks(block)) {
            if (isLeaf(relative.getType()) && checkedLeaves.add(relative)) {
                relative.setMetadata("decayDistance", new FixedMetadataValue(Main.instance, 1));
                relative.breakNaturally();
                checkAdjacentLeaves(relative, checkedLeaves, depthLimit - 1);
            }
        }
    }

    private void reduceAxeDurability(ItemStack tool, int amount) {
        if (tool.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(damageable.getDamage() + amount);
            tool.setItemMeta((ItemMeta) damageable);
        }
    }
}

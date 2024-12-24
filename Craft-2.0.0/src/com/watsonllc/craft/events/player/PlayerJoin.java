package com.watsonllc.craft.events.player;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;
import com.watsonllc.craft.config.Config;
import com.watsonllc.craft.config.PlayerData;
import com.watsonllc.craft.logic.DiscordBot;
import com.watsonllc.craft.logic.Spawn;

public class PlayerJoin implements Listener {

    private String header;
    private String joined;
    private String newJoin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        handlePlayerJoin(event);
    }

    /**
     * Handles the player join event by determining if the player is new or existing,
     * applying relevant actions, and notifying Discord.
     * 
     * @param event The PlayerJoinEvent to process.
     */
    private void handlePlayerJoin(PlayerJoinEvent event) {
        if (event == null || event.getPlayer() == null) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            newPlayer(player);
        }

        existingPlayer(event, player);
        setHeader(player);
        DiscordBot.joinMessage(event);
    }

    /**
     * Handles logic for existing players, setting join messages and player list names.
     * 
     * @param event The PlayerJoinEvent to process. 
     * @param player The player who joined.
     */
    private void existingPlayer(PlayerJoinEvent event, Player player) {
        joined = Utils.color("&7%player% &ajoined");
        joined = joined.replace("%player%", Utils.color(player.getDisplayName()));
        event.setJoinMessage(joined);
        Main.instance.getLogger().info("Player " + player.getName() + " joined with group: " + PlayerData.getGroup(player));

        if (PlayerData.dataNull(player)) {
            PlayerData.createData(player);
            Main.instance.getLogger().info("Creating " + player.getName().toUpperCase() + " playerData");
        }

        if (player.isOp()) {
            player.setPlayerListName(Utils.color("&c" + Utils.color(player.getDisplayName())));
        }

        if (PlayerData.getGroup(player).equals("trusted")) {
            player.setPlayerListName(Utils.color("&6" + Utils.color(player.getDisplayName())));
        }
    }

    /**
     * Applies special handling for new players, including assigning groups, broadcasting messages,
     * and giving starter kits.
     * 
     * @param player The new player joining the server.
     */
    private void newPlayer(Player player) {
        int current = Config.getInt("unique");
        int newAmount = current + 1;
        Config.set("unique", newAmount);
        Config.save();
        PlayerData.createData(player);
        if (Config.getInt("unique") <= 500) {
            PlayerData.setGroup(player, "founder");
        }
        Main.instance.getLogger().info("Creating " + player.getName().toUpperCase() + " playerData");

        newJoin = Utils.color("&6Welcome to the server %player%, there have been %unique% unique players!");
        newJoin = newJoin.replace("%player%", Utils.color(player.getDisplayName()));
        newJoin = newJoin.replace("%unique%", String.valueOf(newAmount));
        starterKit(player);
        Bukkit.broadcastMessage(newJoin);

        if (!Spawn.isNull()) {
            player.teleport(Spawn.location());
        }
    }

    /**
     * Sets the header for the player list, displaying server information.
     * 
     * @param player The player whose list header will be set.
     */
    private void setHeader(Player player) {
        header = Utils.color("&7%name% \n&6&ojust another forever server \n&fv%version%");
        header = header.replace("%name%", Main.name);
        header = header.replace("%version%", Main.version);
        player.setPlayerListHeader(header);
    }

    /**
     * Provides a starter kit to new players, including tools and essential items.
     * 
     * @param player The new player receiving the starter kit.
     */
    private void starterKit(Player player) {
        ItemStack pickaxe = Utils.item(Material.STONE_PICKAXE, 1, "&6Starter Pickaxe");
        ItemStack sword = Utils.item(Material.STONE_SWORD, 1, "&6Starter Sword");
        ItemStack axe = Utils.item(Material.STONE_AXE, 1, "&6Starter Axe");
        ItemStack shovel = Utils.item(Material.STONE_SHOVEL, 1, "&6Starter Shovel");
        ItemStack food = Utils.item(Material.GOLDEN_CARROT, 8, "&6Starter Food");
        ItemStack ticket = Utils.item(Material.PAPER, 1, "&6Ticket #" + Config.getInt("unique"), Utils.color("&cDont lose this!"));
        ItemStack claimTool = Utils.item(Material.PAPER, 1, Utils.color("&bClaim How-to"),
                Utils.color("&7Select two boundaries with the &aClaim Tool"),
                Utils.color("&cClaims do not extend from the top to bottom of map automatically,"),
                Utils.color("&cyou have to manually select your Y region to cover your location"),
                Utils.color("&8/&7claim tool &a(Use this command first)"),
                Utils.color("&8/&7claim create &8<&7name&8>"),
                Utils.color("&8/&7claim edit &8<&7name&8> <&7flag&8> <&7value&8>"),
                Utils.color("&8/&7claim remove &8<&7name&8>"),
                Utils.color("&8/&7claim howto"));

        player.getInventory().addItem(pickaxe, sword, axe, shovel, food, ticket, claimTool);
    }
}
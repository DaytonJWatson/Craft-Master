package com.watsonllc.craft.logic;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.watsonllc.craft.Main;
import com.watsonllc.craft.Utils;

public enum MobRarity {
    COMMON("\u00a77Common"),
    RARE("\u00a79Rare"),
    EPIC("\u00a75Epic"),
    LEGENDARY("\u00a76Legendary");

    private final String displayName;
    private static final Random random = new Random();

    MobRarity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MobRarity getRandomRarity() {
        double chance = Math.random();
        if (chance < 0.7) return COMMON;
        if (chance < 0.9) return RARE;
        if (chance < 0.98) return EPIC;
        return LEGENDARY;
    }

    public static void assignRarity(CreatureSpawnEvent event) {
        if (!Utils.HOSTILE_MOBS.contains(event.getEntityType())) return;

        LivingEntity entity = event.getEntity();
        MobRarity rarity = getRandomRarity();
        String newName = rarity.getDisplayName() + " " + entity.getType().name().replace("_", " ");

        entity.setCustomName(newName);
        entity.getPersistentDataContainer().set(new NamespacedKey(Main.instance, "MobRarity"),
                PersistentDataType.STRING, rarity.name());
    }

    public static void rarityLoot(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        PersistentDataContainer data = entity.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.instance, "MobRarity");

        if (data.has(key, PersistentDataType.STRING)) {
            String rarity = data.get(key, PersistentDataType.STRING);
            MobRarity mobRarity = MobRarity.valueOf(rarity);

            switch (mobRarity) {
                case RARE -> addLoot(event, rareItems);
                case EPIC -> addLoot(event, epicItems);
                case LEGENDARY -> addLoot(event, legendaryItems);
                default -> {}
            }
        }
    }

    private static final List<ItemStack> rareItems = Arrays.asList(
        
    );

    private static final List<ItemStack> epicItems = Arrays.asList(
        
    );

    private static final List<ItemStack> legendaryItems = Arrays.asList(
        
    );

    private static void addLoot(EntityDeathEvent event, List<ItemStack> lootTable) {
        if (lootTable.isEmpty()) return;
        if (random.nextDouble() < 0.9) {
            ItemStack item = lootTable.get(random.nextInt(lootTable.size()));
            event.getDrops().add(item);
        }
    }
}

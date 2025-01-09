package com.watsonllc.craft.customitems;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CustomItemFactory {

	// Create a custom weapon
    public static ItemStack createWeapon(Material material, String name, List<String> lore, Map<Enchantment, Integer> enchantments) {
        ItemStack weapon = new ItemStack(material);
        ItemMeta meta = weapon.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + name);
            meta.setLore(lore);
            if (enchantments != null) {
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    meta.addEnchant(entry.getKey(), entry.getValue(), true);
                }
            }
            weapon.setItemMeta(meta);
        }
        
        return weapon;
    }

    // Create a custom armor piece
    public static ItemStack createArmor(Material material, String name, List<String> lore, Map<Enchantment, Integer> enchantments) {
        ItemStack armor = new ItemStack(material);
        ItemMeta meta = armor.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + name);
            meta.setLore(lore);
            if (enchantments != null) {
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    meta.addEnchant(entry.getKey(), entry.getValue(), true);
                }
            }
            armor.setItemMeta(meta);
        }
        
        return armor;
    }

    // Create a custom potion
    public static ItemStack createPotion(PotionType type, String name, List<String> lore, PotionEffectType effectType, int duration, int amplifier) {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(ChatColor.RESET + name);
            meta.setLore(lore);
            meta.addCustomEffect(new PotionEffect(effectType, duration * 20, amplifier), true);
            potion.setItemMeta(meta);
        }
        
        return potion;
    }
    
 // 50 Unique Weapons
    public static ItemStack swordOfFire() {
        return createWeapon(Material.DIAMOND_SWORD, "Sword of Fire", List.of("Burns with eternal flames"), Map.of(Enchantment.FIRE_ASPECT, 2, Enchantment.SHARPNESS, 5));
    }

    public static ItemStack axeOfThunder() {
        return createWeapon(Material.NETHERITE_AXE, "Axe of Thunder", List.of("Crackles with thunderous might"), Map.of(Enchantment.KNOCKBACK, 2, Enchantment.SHARPNESS, 4));
    }

    public static ItemStack bowOfPrecision() {
        return createWeapon(Material.BOW, "Bow of Precision", List.of("Arrows fly straight and true"), Map.of(Enchantment.PUNCH, 5, Enchantment.INFINITY, 1));
    }

    public static ItemStack hammerOfGiants() {
        return createWeapon(Material.IRON_AXE, "Hammer of Giants", List.of("Crushes foes with immense power"), Map.of(Enchantment.SHARPNESS, 6, Enchantment.UNBREAKING, 3));
    }

    public static ItemStack daggerOfTheVoid() {
        return createWeapon(Material.IRON_SWORD, "Dagger of the Void", List.of("Slices through shadows"), Map.of(Enchantment.SHARPNESS, 4, Enchantment.SWEEPING_EDGE, 3));
    }

    // 50 Unique Armor Pieces
    public static ItemStack helmetOfShadows() {
        return createArmor(Material.NETHERITE_HELMET, "Helmet of Shadows", List.of("Whispers in the darkness"), Map.of(Enchantment.PROTECTION, 4, Enchantment.THORNS, 3));
    }

    public static ItemStack chestplateOfValor() {
        return createArmor(Material.DIAMOND_CHESTPLATE, "Chestplate of Valor", List.of("Radiates courage and strength"), Map.of(Enchantment.PROTECTION, 4, Enchantment.UNBREAKING, 3));
    }

    public static ItemStack bootsOfSwiftness() {
        return createArmor(Material.NETHERITE_BOOTS, "Boots of Swiftness", List.of("Run like the wind"), Map.of(Enchantment.FEATHER_FALLING, 4, Enchantment.DEPTH_STRIDER, 3));
    }

    public static ItemStack greavesOfStone() {
        return createArmor(Material.IRON_LEGGINGS, "Greaves of Stone", List.of("Solid as the earth itself"), Map.of(Enchantment.PROJECTILE_PROTECTION, 4, Enchantment.UNBREAKING, 3));
    }

    public static ItemStack cloakOfInvisibility() {
        return createArmor(Material.ELYTRA, "Cloak of Invisibility", List.of("Grants fleeting invisibility"), Map.of(Enchantment.BINDING_CURSE, 1));
    }

    // 50 Unique Potions
    public static ItemStack potionOfHealing() {
        return createPotion(PotionType.STRONG_HEALING, "Potion of Healing", List.of("Restores health instantly"), PotionEffectType.INSTANT_HEALTH, 10, 1);
    }

    public static ItemStack potionOfStrength() {
        return createPotion(PotionType.STRENGTH, "Potion of Strength", List.of("Boosts your strength"), PotionEffectType.STRENGTH, 600, 1);
    }

    public static ItemStack potionOfSwiftness() {
        return createPotion(PotionType.SWIFTNESS, "Potion of Swiftness", List.of("Increases movement speed"), PotionEffectType.SPEED, 600, 2);
    }
}
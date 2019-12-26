package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemStacks
{

    private ItemStack big_bp, tiny_bp, bp_key, ender_bp;
    private ItemStack iron_helmet, iron_chestplate, iron_leggings, iron_boots,
            chainmail_helmet, chainmail_chestplate, chainmail_leggings, chainmail_boots,
            diamond_helmet, diamond_chestplate, diamond_leggings, diamond_boots;
    private ItemStack diamondHelmetReplacement, diamondChestplateReplacement, diamondLeggingsReplacement, diamondBootsReplacement;
    private ItemStack leather_horse, iron_horse, diamond_horse;

    private List<Material> disabledDefaultRecipes;

    public ItemStacks() {
        init();
    }

    private void init() {

        disabledDefaultRecipes = new ArrayList<>();
        disabledDefaultRecipes.addAll(Arrays.asList(
                Material.DIAMOND_BOOTS,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_HELMET,
                Material.LEATHER_HORSE_ARMOR,
                Material.IRON_HELMET,
                Material.IRON_CHESTPLATE,
                Material.IRON_LEGGINGS,
                Material.IRON_BOOTS));

        //BIG BP
        big_bp = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta big_bpM = (LeatherArmorMeta) big_bp.getItemMeta();
        big_bpM.setColor(Color.RED);
        big_bpM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        big_bpM.setDisplayName(BackpackType.LARGE.getName());
        big_bpM.setLore(Arrays.asList("§7In diesen Rucksack passen viele, weitere Dinge rein", "ID: #-1"));
        big_bp.setItemMeta(big_bpM);

        //TINY BP
        tiny_bp = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta tiny_bpM = (LeatherArmorMeta) tiny_bp.getItemMeta();
        tiny_bpM.setColor(Color.GREEN);
        tiny_bpM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        tiny_bpM.setDisplayName(BackpackType.TINY.getName());
        tiny_bpM.setLore(Arrays.asList("§7Dieser nützliche Rucksack hat Platz für viele Sachen", "ID: #-1"));
        tiny_bp.setItemMeta(tiny_bpM);

        //ENDER BP
        ender_bp = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta ender_bpM = (LeatherArmorMeta) ender_bp.getItemMeta();
        ender_bpM.setColor(Color.PURPLE);
        ender_bpM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ender_bpM.setDisplayName(BackpackType.ENDER.getName());
        tiny_bpM.setLore(Arrays.asList(BackpackType.ENDER.getLore()));
        ender_bp.setItemMeta(ender_bpM);

        //BP KEY
        bp_key = new ItemStack(Material.FEATHER, 1);
        ItemMeta bp_keyM = bp_key.getItemMeta();
        bp_keyM.setDisplayName("§5Rucksack Schlüssel");
        bp_key.setItemMeta(bp_keyM);

        iron_helmet = new ItemStack(Material.IRON_HELMET, 1);
        iron_chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        iron_leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        iron_boots = new ItemStack(Material.IRON_BOOTS, 1);

        chainmail_helmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        chainmail_chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        chainmail_leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        chainmail_boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);

        diamond_helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        diamond_chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 2);
        diamond_leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        diamond_boots = new ItemStack(Material.DIAMOND_BOOTS, 1);

        leather_horse = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        iron_horse = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
        diamond_horse = new ItemStack(Material.DIAMOND_HORSE_ARMOR, 1);

        diamondHelmetReplacement = new ItemStack(Material.DIAMOND, 5);
        diamondChestplateReplacement = new ItemStack(Material.DIAMOND, 8);
        diamondLeggingsReplacement = new ItemStack(Material.DIAMOND, 7);
        diamondBootsReplacement = new ItemStack(Material.DIAMOND, 4);

    }

    public ItemStack getBig_bp()
    {
        return big_bp;
    }

    public ItemStack getTiny_bp()
    {
        return tiny_bp;
    }

    public ItemStack getBp_key()
    {
        return bp_key;
    }

    public ItemStack getEnder_bp()
    {
        return ender_bp;
    }

    public ItemStack getIron_chestplate() {
        return iron_chestplate;
    }

    public ItemStack getChainmail_chestplate() {
        return chainmail_chestplate;
    }

    public ItemStack getIron_helmet() {
        return iron_helmet;
    }

    public ItemStack getIron_leggings() {
        return iron_leggings;
    }

    public ItemStack getIron_boots() {
        return iron_boots;
    }

    public ItemStack getChainmail_helmet() {
        return chainmail_helmet;
    }

    public ItemStack getChainmail_leggings() {
        return chainmail_leggings;
    }

    public ItemStack getChainmail_boots() {
        return chainmail_boots;
    }

    public ItemStack getDiamond_helmet() {
        return diamond_helmet;
    }

    public ItemStack getDiamond_chestplate() {
        return diamond_chestplate;
    }

    public ItemStack getDiamond_leggings() {
        return diamond_leggings;
    }

    public ItemStack getDiamond_boots() {
        return diamond_boots;
    }

    public ItemStack getLeather_horse() {
        return leather_horse;
    }

    public ItemStack getIron_horse() {
        return iron_horse;
    }

    public ItemStack getDiamondHelmetReplacement() {
        return diamondHelmetReplacement;
    }

    public ItemStack getDiamondChestplateReplacement() {
        return diamondChestplateReplacement;
    }

    public ItemStack getDiamondLeggingsReplacement() {
        return diamondLeggingsReplacement;
    }

    public ItemStack getDiamondBootsReplacement() {
        return diamondBootsReplacement;
    }

    public ItemStack getDiamond_horse() {
        return diamond_horse;
    }

    public List<Material> getDisabledDefaultRecipes() {
        return disabledDefaultRecipes;
    }

    public ItemStack getAir() {

        return new ItemStack(Material.DIAMOND, 0);

    }
}

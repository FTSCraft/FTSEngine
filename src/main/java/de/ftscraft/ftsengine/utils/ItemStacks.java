package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;

public class ItemStacks
{

    private ItemStack big_bp, tiny_bp, bp_key;

    public ItemStacks() {
        init();
    }

    private void init() {

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

        //BP KEY
        bp_key = new ItemStack(Material.FEATHER, 1);
        ItemMeta bp_keyM = bp_key.getItemMeta();
        bp_keyM.setDisplayName("§5Rucksack Schlüssel");
        bp_key.setItemMeta(big_bpM);

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
}

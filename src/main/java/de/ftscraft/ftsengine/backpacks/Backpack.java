package de.ftscraft.ftsengine.backpacks;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Backpack
{

    private int id;
    private BackpackType type;
    private Inventory inventory;

    private Engine plugin;

    public Backpack(Engine plugin, BackpackType type, int id, Inventory inv) {
        this.plugin = plugin;
        this.type = type;
        this.id = id;
        if(plugin.biggestBpId < id) {
            plugin.biggestBpId = id;
        }
        this.inventory = inv;
        plugin.backpacks.put(this.id, this);
    }


    public Backpack(Engine plugin, BackpackType type, Player p) {
        this.plugin = plugin;
        this.type = type;
        this.inventory = Bukkit.createInventory(null, type.getSize(), type.getName());
        this.id = plugin.biggestBpId + 1;
        plugin.biggestBpId = this.id;

        ItemStack is = p.getInventory().getChestplate().clone();
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(type.getLore(), "ID: #"+this.id));
        is.setItemMeta(im);
        p.getInventory().setChestplate(is);

        p.sendMessage("§eDein Rucksack ist nun Regestriert. " + this.id);

        plugin.backpacks.put(this.id, this);
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

    public void safe() {
        File file = new File(plugin.getDataFolder() + "//backpacks//"+this.id+".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("id", this.id);
        ItemStack[] content = inventory.getContents();
        cfg.set("inventory", content);
        cfg.set("type", this.type.name());

        try
        {
            cfg.save(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}

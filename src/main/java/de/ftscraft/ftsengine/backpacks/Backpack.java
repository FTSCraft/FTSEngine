package de.ftscraft.ftsengine.backpacks;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Backpack
{

    private int id;
    private BackpackType type;
    private Inventory inventory;

    private Engine plugin;

    public Backpack(Engine plugin, BackpackType type, int id) {
        this.plugin = plugin;
        this.type = type;
        this.id = id;
        this.inventory = Bukkit.createInventory(null, type.getSize(), type.getName());
    }


    public Backpack(Engine plugin, BackpackType type, Player p) {
        this.plugin = plugin;
        this.type = type;
        this.inventory = Bukkit.createInventory(null, type.getSize(), type.getName());
        this.id = plugin.biggestBpId + 1;
        plugin.biggestBpId = this.id;



        p.sendMessage("§eDein Rucksack ist nun Regestriert.");
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

}

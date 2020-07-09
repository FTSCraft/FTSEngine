package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Briefkasten {

    private Engine plugin;

    private Location location;
    private Chest chest;

    private UUID player;

    public Briefkasten(Engine plugin, Location location, UUID player) {
        this.plugin = plugin;
        this.location = location;
        this.player = player;
    }

    public boolean putItemIntoChest(ItemStack itemStack) {

        if(!chest.getChunk().isLoaded()) {
            chest.getChunk().load();
        }

        if(!chest.getBlockInventory().contains(Material.AIR))
            return false;

        chest.getBlockInventory().addItem(itemStack);

        return true;
    }

}

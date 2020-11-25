package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.exceptions.BriefkastenNotFoundException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Briefkasten {

    private Engine plugin;

    private Location location;
    private Chest chest;

    private UUID player;

    public Briefkasten(Engine plugin, Location location, UUID player) {
        this.plugin = plugin;
        this.location = location;
        this.player = player;

        if(location.getBlock().getState() instanceof Chest) {
            this.chest = (Chest) location.getBlock().getState();
            plugin.briefkasten.put(player, this);
        } else {
            Logger.getLogger("Minecraft").log(Level.WARNING, "Briefkasten ist keine Chest. Koordinaten: " + getLocation().getX() + " " + getLocation().getY() + " " + getLocation().getZ());
        }

    }

    public boolean putItemIntoChest(ItemStack itemStack) {

        this.chest = (Chest) location.getBlock().getState();

        if(!chest.getChunk().isLoaded()) {
            chest.getChunk().load();
        }

        if(chest.getBlockInventory().firstEmpty() == -1) {
            return false;
        }

        chest.getBlockInventory().addItem(itemStack);

        //chest.update();

        return true;
    }

    public Chest getChest() {
        return chest;
    }


    public Location getLocation() {
        return location;
    }

    public UUID getPlayer() {
        return player;
    }

}

package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;
import org.bukkit.block.Container;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Briefkasten {

    private final Location location;
    private Container container;

    private final UUID player;

    public Briefkasten(Engine plugin, Location location, UUID player) {
        this.location = location;
        this.player = player;

        if(location.getBlock().getState() instanceof Container state) {
            this.container = state;
            plugin.briefkasten.put(player, this);
        } else {
            Logger.getLogger("Minecraft").log(Level.WARNING, "Briefkasten ist kein Container. Koordinaten: " + getLocation().getX() + " " + getLocation().getY() + " " + getLocation().getZ());
        }

    }

    public boolean putItemIntoChest(ItemStack itemStack) {

        this.container = (Container) location.getBlock().getState();

        if(!container.getChunk().isLoaded()) {
            container.getChunk().load();
        }
        if(container.getInventory().firstEmpty() == -1) {
            return false;
        }

        container.getInventory().addItem(itemStack);

        //chest.update();

        return true;
    }

    public Container getContainer() {
        return container;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getPlayer() {
        return player;
    }

}

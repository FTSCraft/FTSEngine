package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;

public class OvenListener implements Listener {

    private final Engine plugin;

    public OvenListener(Engine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        System.out.println("event here");
        this.plugin = plugin;
        plugin.getLogger().info("Oven Event enabled");
    }

    @EventHandler
    public void onOven(FurnaceBurnEvent event) {
        if (event.getFuel().getType() == Material.LAVA_BUCKET) {
            event.setCancelled(true);
        } else if (event.getFuel().getType() == Material.CHARCOAL) {
            event.setCancelled(false);
            event.setBurnTime(30 * 20);
        }
    }

}

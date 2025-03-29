package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.quivers.Quiver;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class ItemSwitchListener implements Listener {

    private final Engine plugin;

    public ItemSwitchListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent e) {
        Inventory inv = e.getPlayer().getInventory();
        ItemStack newItem = inv.getItem(e.getNewSlot());
        ItemStack previousItem = inv.getItem(e.getPreviousSlot());

        if (newItem != null && newItem.getType() == Material.FILLED_MAP) {
            Brief brief = plugin.briefe.get(((MapMeta) newItem.getItemMeta()).getMapId());
            if (brief != null) {
                brief.loadMap(newItem);
            }
        } else if (previousItem != null && previousItem.getType() == Material.FILLED_MAP) {
            Brief brief = plugin.briefe.get(((MapMeta) previousItem.getItemMeta()).getMapId());
            if (brief != null) {
                brief.unloadMap(previousItem);
            }
        }

        if (previousItem != null && (previousItem.getType() == Material.BOW || previousItem.getType() == Material.CROSSBOW)) {
            Quiver.storeArrowInQuiver(e.getPlayer(), plugin);
        }
    }
}
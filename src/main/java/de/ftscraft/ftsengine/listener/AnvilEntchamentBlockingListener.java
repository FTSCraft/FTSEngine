package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnvilEntchamentBlockingListener implements Listener {
    private final List<Integer> customModelValues = new ArrayList<>(Arrays.asList(1000, 1001));

    public AnvilEntchamentBlockingListener(Engine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAnvil(InventoryClickEvent event) {
        // check if an item is valid
        if (event.getInventory().getType() != InventoryType.ANVIL) return;
        if (event.getCurrentItem() == null) return;
        if (!event.getCurrentItem().hasItemMeta()) return;
        if (!event.getCurrentItem().getItemMeta().hasCustomModelData()) return;


        ItemMeta currentItem = event.getCurrentItem().getItemMeta();

        if (checkModelData(currentItem)) {
            event.setCancelled(true);
        }

    }

    /**
     * Checks if an item has an CustomModelData value that is known to the system
     * @param item item to check
     * @return false if no key was found
     */
    private boolean checkModelData(ItemMeta item) {

        for (Integer integer : customModelValues) {
            if (item.getCustomModelData() == integer) return true;
        }
        return false;
    }
}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class VillagerTradeListener implements Listener {

    private final Engine plugin;

    public VillagerTradeListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTrade(InventoryOpenEvent event) {

        if (event.getInventory().getType() == InventoryType.MERCHANT) {

            if (event.getInventory().getHolder() instanceof Entity e) {

                if (!plugin.getShopkeepersPlugin().getShopkeeperRegistry().isShopkeeper(e)) {

                    event.getPlayer().sendMessage("§7[§cFTS-System§7] §7Du darfst mit diesen Typen nicht handeln!");
                    event.setCancelled(true);

                }

            }

        }

    }

}

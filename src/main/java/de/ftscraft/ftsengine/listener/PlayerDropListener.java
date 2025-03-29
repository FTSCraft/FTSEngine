package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.feature.durchsuchen.DurchsuchenManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.quivers.Quiver;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDropListener implements Listener {

    private final Engine plugin;

    public PlayerDropListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (DurchsuchenManager.isHideInventory(event.getPlayer().getOpenInventory().getTopInventory())) {
            event.setCancelled(true);
            return;
        }

        ItemStack droppedItem = event.getItemDrop().getItemStack();

        if (droppedItem.getType() == Material.BOW || droppedItem.getType() == Material.CROSSBOW) {
            Quiver.returnArrowToQuiver(event.getPlayer(), plugin);
        }
    }
}

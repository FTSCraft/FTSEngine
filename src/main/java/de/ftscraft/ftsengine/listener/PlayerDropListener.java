package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.feature.roleplay.durchsuchen.DurchsuchenManager;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener {

    public PlayerDropListener(Engine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (DurchsuchenManager.isHideInventory(event.getPlayer().getOpenInventory().getTopInventory()))
            event.setCancelled(true);
    }
}

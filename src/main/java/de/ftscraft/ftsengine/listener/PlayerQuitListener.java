package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final Engine plugin;

    public PlayerQuitListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (e.getPlayer().getVehicle() != null) {
            e.getPlayer().getVehicle().removePassenger(e.getPlayer());
        }

        // User in Datenbank speichern
        if (plugin.getPlayer().containsKey(e.getPlayer())) {
            plugin.getDatabaseHandler().getUserStorageManager().saveUser(plugin.getPlayer().get(e.getPlayer()));
        }

        plugin.getPlayer().remove(e.getPlayer());
    }

}

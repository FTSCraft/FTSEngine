package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

public class SneakListener implements Listener {

    private final Engine plugin;

    public SneakListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDismount(EntityDismountEvent e) {


    }

}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SneakListener implements Listener
{

    private Engine plugin;

    public SneakListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,  plugin);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        if(plugin.getPlayer().get(e.getPlayer()).isSitting()) {
            plugin.getPlayer().get(e.getPlayer()).abortSitting();
        }
    }

}

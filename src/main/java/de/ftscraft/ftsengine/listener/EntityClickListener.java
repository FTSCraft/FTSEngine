package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityClickListener implements Listener
{

    private Engine plugin;

    public EntityClickListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e)
    {
        if (plugin.getReiter().contains(e.getPlayer()))
        {
            e.getRightClicked().addPassenger(e.getPlayer());
            plugin.getReiter().remove(e.getPlayer());
        }

        for (Player reiter : plugin.getReiter())
        {
            if (e.getPlayer().getPassengers().contains(reiter))
            {
                e.getPlayer().removePassenger(reiter);
            }
        }

    }

}

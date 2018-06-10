package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener
{

    private Engine plugin;

    public PlayerJoinListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        FTSUser user = new FTSUser(plugin, e.getPlayer());
        plugin.getPlayer().put(e.getPlayer(), user);
    }

}

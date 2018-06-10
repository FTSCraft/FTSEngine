package de.ftscraft.personalausweis.listener;

import de.ftscraft.personalausweis.main.Engine;
import de.ftscraft.personalausweis.main.FTSUser;
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

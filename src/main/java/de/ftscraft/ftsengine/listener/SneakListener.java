package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class SneakListener implements Listener
{

    private Engine plugin;

    public SneakListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,  plugin);
    }

    @EventHandler
    public void onSneak(EntityDismountEvent e) {

        /*plugin.getPlayer().get(e.getPlayer()).abortSitting();*/

        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(plugin.getPlayer().get(p) == null) {
                return;
            }
            if (plugin.getPlayer().get(p).isSitting()) {
                plugin.getPlayer().get(p).abortSitting();
            }
        }
    }

}

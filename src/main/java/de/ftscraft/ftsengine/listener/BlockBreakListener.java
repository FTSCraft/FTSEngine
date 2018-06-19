package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener
{

    public Engine plugin;

    public BlockBreakListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        if(event.getBlock().getType() == Material.SIGN || event.getBlock().getType() == Material.WALL_SIGN || event.getBlock().getType() == Material.SIGN_POST) {
            Sign sign = (Sign) event.getBlock().getState();
            if(sign.getLine(0).equalsIgnoreCase("§b[Briefkasten]")) {
                if(sign.getLine(1).contains(event.getPlayer().getName())) {
                    plugin.briefkasten.get(event.getPlayer().getUniqueId().toString()).destory();
                    event.getPlayer().sendMessage(plugin.msgs.PREFIX+"Du hast erfolgreich dein Briefkasten zerstört. Herzlichen Glückwunsch! (das kannst du bezahlen)");
                } else {
                    if(event.getPlayer().hasPermission("ftsengine.destroy")) {
                        event.setCancelled(false);
                        return;
                    }
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§eDu kannst kein Briefkasten von jemand anderen zerstören. Falls du dich umbenannt hast, sag einem Admin gescheid");
                }
            }
        }

        //Schwarzes Brett
        if (event.getBlock().getType() == Material.SIGN || event.getBlock().getType() == Material.WALL_SIGN || event.getBlock().getType() == Material.SIGN_POST)
        {
            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase("§4Schwarzes Brett"))
            {
                if (!(event.getPlayer().hasPermission("blackboard.remove")) && !plugin.bretter.get(sign.getLocation()).getCreator().toString().equals(event.getPlayer().getUniqueId().toString()))
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du darfst das nicht kaputt machen!");
                } else
                {
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du hast erfolgreich das Schwarze Brett entfernt");
                    Brett brett = plugin.bretter.get(event.getBlock().getLocation());
                    brett.remove();
                }
            }
        }
    }

}

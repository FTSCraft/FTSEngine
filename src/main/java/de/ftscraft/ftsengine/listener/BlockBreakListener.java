package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;
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

        //Schwarzes Brett
        if (event.getBlock().getBlockData() instanceof WallSign || event.getBlock().getBlockData() instanceof org.bukkit.block.data.type.Sign)
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

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignWriteListener implements Listener
{

    private Engine plugin;

    public SignWriteListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWirte(SignChangeEvent event)
    {
        if (event.getPlayer().hasPermission("blackboard.create"))
        {
            if (event.getLine(0).equalsIgnoreCase("Schwarzes Brett"))
                if (event.getLine(1).length() > 3)
                {
                    String name = event.getLine(1);
                    if (event.getLine(2).equalsIgnoreCase(""))
                        if (event.getLine(3).equalsIgnoreCase(""))
                        {

                            Sign sign = (Sign) event.getBlock().getState();
                            event.setLine(0, "§4Schwarzes Brett");
                            plugin.bretter.put(event.getBlock().getLocation(), new Brett(sign, event.getBlock().getLocation(), event.getPlayer().getName(), name, plugin));
                            event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du hast das Schwarze Brett erfolgreich erstellt");

                        }
                } else
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Der Name (2. Zeile) muss mind. 4 Zeichen haben!");
            return;
        }
        if (!(event.getPlayer().hasPermission("blackboard.create")))
        {
            if (event.getLine(0).equalsIgnoreCase("&4Schwarzes Brett"))
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Mach sowas nicht! Das könnte Fehler verursachen!");
            }
        }
    }

}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.logging.Level;

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

        if (event.getLine(0).equalsIgnoreCase("[Briefkasten]"))
        {
            if (plugin.getPlayer().get(event.getPlayer()).hasBriefkasten())
            {
                event.getPlayer().sendMessage("§eDu hast schon ein Briefkasten! Trottel.. Spaß!");
                return;
            }
            org.bukkit.material.Sign s = (org.bukkit.material.Sign) event.getBlock().getState().getData();
            Block chest = event.getBlock().getRelative(s.getAttachedFace());
            if (chest.getType() == Material.CHEST)
            {
                Chest chestState = (Chest) chest.getState();
                chestState.setCustomName("My Chest Inventory Title!");

                event.setLine(0, "§b[Briefkasten]");
                event.setLine(1, ChatColor.DARK_RED + event.getPlayer().getName());
                new Briefkasten(plugin, event.getPlayer().getUniqueId().toString(), chest.getLocation());
                event.getPlayer().sendMessage(plugin.msgs.PREFIX + "Du hast erfolgreich ein Briefkasten erstellt JUHU!");
                plugin.getLogger().log(Level.INFO, event.getPlayer().getName() + " hat einen Briefkasten erstellt");
            }
        }

        //Schwarzes Brett
        if (event.getLine(0).equalsIgnoreCase("Schwarzes Brett"))
            if (event.getLine(1).length() > 3)
            {
                String name = event.getLine(1);
                if (event.getLine(2).equalsIgnoreCase(""))
                    if (event.getLine(3).equalsIgnoreCase(""))
                    {

                        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getBlock().getState();
                        event.setLine(0, "§4Schwarzes Brett");
                        for(Brett all : plugin.bretter.values()) {
                            if(all.getName().equals(name)) {
                                event.getPlayer().sendMessage("§cDieser Name ist schon vorhanden. Probier ein anderen");
                                return;
                            }
                        }
                        plugin.bretter.put(event.getBlock().getLocation(), new Brett(sign, event.getBlock().getLocation(), event.getPlayer().getUniqueId(), name, plugin));
                        event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du hast das Schwarze Brett erfolgreich erstellt");

                    }
            } else
                event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Der Name (2. Zeile) muss mind. 4 Zeichen haben!");
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

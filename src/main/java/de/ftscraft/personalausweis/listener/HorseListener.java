package de.ftscraft.personalausweis.listener;

import de.ftscraft.personalausweis.main.Engine;
import de.ftscraft.personalausweis.pferd.Pferd;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class HorseListener implements Listener
{

    public Engine plugin;

    public HorseListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onHorseRide(VehicleEnterEvent e) {
        if(e.getEntered() instanceof Player)
        {
            if (e.getVehicle() instanceof Horse)
            {
                if (plugin.horseIsDa(e.getVehicle().getUniqueId()))
                {
                    Pferd pferd = plugin.getPferde().get(e.getVehicle().getUniqueId());
                    if(pferd.getPrice() >= 0) {
                        Player p = (Player) e.getEntered();
                        p.sendMessage("§cDieses Pferd steht für §e"+pferd.getPrice()+" §czum Verkauf. Tippe §e/pferd kaufen §cum das Pferd zu kaufen!");
                    }
                    if(!pferd.isOwner((Player)e.getEntered())) {
                        if(pferd.isLocked()) {
                            e.getEntered().sendMessage(plugin.msgs.PREFIX + "Dieses Pferd ist abgeschlossen!");
                        }
                    } else {
                        if(pferd.isLocked()) {
                            pferd.lock((Player) e.getEntered());
                        }
                        e.getEntered().sendMessage(plugin.msgs.PREFIX + "Schönen Ritt!");
                    }
                }

            }
        }
    }

    @EventHandler
    public void onHorseDeath(EntityDeathEvent e) {
        if(e.getEntity() instanceof Horse) {
            if(plugin.horseIsDa(e.getEntity().getUniqueId())) {
                Pferd pferd = plugin.getPferde().get(e.getEntity().getUniqueId());
                pferd.dead();
            }
        }
    }


}

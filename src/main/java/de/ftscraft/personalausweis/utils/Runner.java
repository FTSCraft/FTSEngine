package de.ftscraft.personalausweis.utils;

import de.ftscraft.personalausweis.main.Engine;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class Runner implements Runnable
{

    private Engine plugin;

    private int seconds_armorstand = 0;

    public Runner(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    @Override
    public void run()
    {

        seconds_armorstand++;

        if(seconds_armorstand == 180) {

            for(World w : plugin.getServer().getWorlds()) {
                for(Entity e : w.getEntitiesByClass(ArmorStand.class)) {
                    if(!((ArmorStand)e).isVisible()) {
                        if(e.getPassengers().size() == 0) {
                            e.remove();
                        }
                    }
                }
            }

            System.out.println("[FTSEngine INFO] Removed Armorstands");

        }


    }

}

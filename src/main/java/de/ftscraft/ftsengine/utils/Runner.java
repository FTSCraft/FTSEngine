package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.courier.BriefLieferung;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

import java.util.logging.Level;

public class Runner implements Runnable
{

    private Engine plugin;

    private int seconds = 1600;

    public Runner(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    @Override
    public void run()
    {

        seconds++;

        if(seconds == 60 * 15) {

            for(World w : plugin.getServer().getWorlds()) {
                for(Entity e : w.getEntitiesByClass(ArmorStand.class)) {
                    if(!((ArmorStand)e).isVisible()) {
                        if(e.getPassengers().size() == 0) {
                            e.remove();
                        }
                    }
                }

            }

            plugin.safeAll();

            seconds = 0;
            plugin.getLogger().log(Level.INFO, "Speierete Pferde, Rucksäcke und Ausweise");

        }

        try
        {
            for (BriefLieferung lieferung : plugin.lieferungen)
            {
                lieferung.setSeconds(lieferung.getSeconds() - 1);
            }
        } catch (Exception ignored) {

        }

    }

}

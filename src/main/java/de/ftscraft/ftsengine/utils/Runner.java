package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.courier.BriefLieferung;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public class Runner implements Runnable
{

    private Engine plugin;
    private int seconds_tab = 0;
    private int tablist_status = 0;
    private ArrayList<String> footer;

    public Runner(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    @Override
    public void run()
    {

        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (plugin.getChat().getPrimaryGroup(p).equalsIgnoreCase("reisender"))
            {
                if (p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60 >= 30)
                {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + p.getName() + " parent set bürger");
                }
            }
        }

        try
        {
            //Lieferungen
            for (BriefLieferung lieferung : plugin.lieferungen)
            {
                lieferung.setSeconds(lieferung.getSeconds() - 1);
            }
        } catch (Exception ignored)
        {

        }

    }


}



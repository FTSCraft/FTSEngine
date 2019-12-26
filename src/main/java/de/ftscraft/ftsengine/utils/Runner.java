package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;

import java.util.ArrayList;

public class Runner implements Runnable
{

    private Engine plugin;
    private int seconds_tab = 0;
    private int tablist_status = 0;
    private ArrayList<String> footer;

    public Runner(Engine plugin)
    {
        this.plugin = plugin;
        //plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    @Override
    public void run()
    {
        /*for (Player p : Bukkit.getOnlinePlayers()) {
            if (plugin.getChat().getPrimaryGroup(p).equalsIgnoreCase("default")) {
                if (p.getStatistic(Statistic.PLAY_ONE_MINUTE) >= 30 * 60 * 60 * 20) {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + p.getName() + " parent set bürger");
                }
            }
        }*/

    }


}



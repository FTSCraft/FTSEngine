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
        /*footer = new ArrayList<>();

        footer.add("        §dKaiser §r- §bHelfer §r- §2Herzog        ");
        footer.add("         §bHelfer §r- §2Herzog §r- §2König        ");
        footer.add("         §2Herzog §r- §2König §r- §2Fürst         ");
        footer.add("        §2König §r- §2Fürst §r- §2Stadtherr       ");
        footer.add("    §2Fürst §r- §2Stadtherr §r- §2Bürgermeister   ");
        footer.add("   §2Stadtherr §r- §2Bürgermeister §r- §2Siedler  ");
        footer.add("  §2Bürgermeister §r- §2Siedler §r- §9Erzmeister  ");
        footer.add("     §2Siedler §r- §9Erzmeister §r- §9Meister     ");
        footer.add("     §9Erzmeister §r- §9Meister §r- §4Walküre     ");
        footer.add("      §9Meister §r- §4Walküre §r- §4Einherjer     ");
        footer.add("      §4Walküre §r- §4Einherjer §r- §6Bürger      ");
        footer.add("     §4Einherjer §r- §6Bürger §r- §6Reisender     ");
        footer.add("       §6Bürger §r- §6Reisender §r- §dKaiser      ");
        footer.add("       §6Reisender §r- §dKaiser §r- §bHelfer      ");*/
    }

    @Override
    public void run()
    {

        /*seconds_tab ++;
        if(seconds_tab == 2) {
            seconds_tab = 0;
            tablist_status++;
            if(tablist_status == 14)
                tablist_status = 0;

            for(Player all : Bukkit.getOnlinePlayers())
                plugin.sendTablistHeaderAndFooter(all, "§6§lFTS-Craft", footer.get(tablist_status));

        }*/


        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (Objects.requireNonNull(plugin.getLpapi().getUser(p.getUniqueId())).getPrimaryGroup().equals("reisender"))
            {
                if (p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60 >= 30)
                {
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + p.getName() + " parent set bürger");
                } else p.sendMessage("" + p.getStatistic(Statistic.PLAY_ONE_TICK) / 20 / 60 / 60);
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



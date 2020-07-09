package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Runner implements Runnable
{

    private Engine plugin;
    private int seconds_tab = 0;
    private int tablist_status = 0;
    private ArrayList<String> footer;

    private ArrayList<Player> sentUserMessage;

    public Runner(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    @Override
    public void run()
    {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (plugin.getChat().getPrimaryGroup(p).equalsIgnoreCase("default")) {
                if (p.getStatistic(Statistic.PLAY_ONE_MINUTE) >= 50 * 60 * 60 * 20) {
                    if(!sentUserMessage.contains(p)) {
                        if(p.hasPermission("group.reisender")) {
                            sentUserMessage.add(p);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                p.sendMessage("§cDu spielst seit mehr als 50 Stunden auf Parsifal und bist noch immer Reisender? Bitte erstelle dir langsam einen Ausweis und eine Charvorstellung im Forum!");
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
                            }, 20 * 5);
                        }
                    }
                }
            }
        }

    }


}



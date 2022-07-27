package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CountdownScheduler implements Runnable {

    private final Engine plugin;
    private final Player p;
    private Player t;
    private String msg;
    private int seconds;
    private final int taskid;
    private SchedulerType type;

    public CountdownScheduler(Engine plugin, int seconds, Player p) {
        this.plugin = plugin;
        this.p = p;
        this.seconds = seconds;
        this.taskid = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    public CountdownScheduler(Engine plugin, int seconds, Player p, Player t, String msg) {
        this.plugin = plugin;
        this.seconds = seconds;
        this.p = p;
        this.type = SchedulerType.Taube;
        this.t = t;
        this.msg = msg;
        this.taskid = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    @Override
    public void run() {
        if (type == SchedulerType.Countdown) {
            seconds--;
            if (seconds != 0) {
                if (p != null) {
                    for (Entity e : p.getNearbyEntities(15, 15, 15)) {
                        if (e instanceof Player) {
                            e.sendMessage("§7Noch §c" + seconds + " §7Sekunden! (Countdown von §c" + p.getName() + "§7)");
                        }
                    }
                    p.sendMessage("§7Noch §c" + seconds + " §7Sekunden! (Countdown von §c" + p.getName() + "§7)");

                }
            } else {
                plugin.getServer().getScheduler().cancelTask(taskid);
                for (Entity e : p.getNearbyEntities(15, 15, 15)) {
                    if (e instanceof Player) {
                        e.sendMessage("§cDer Countdown ist abgelaufen!");
                    }
                }
                p.sendMessage("§cDer Countdown ist abgelaufen!");

            }

        } else if (type == SchedulerType.Taube) {

            seconds--;

            if (seconds == 0) {
                t.playSound(t.getLocation(), Sound.ENTITY_BAT_LOOP, 3, -20);
                p.sendMessage(Messages.PREFIX + "Deine Taube ist angekommen!");
                t.sendMessage("§c---------");
                t.sendMessage("§eEine Brieftaube von §c" + p.getName() + "§e hat dich erreicht!");
                t.sendMessage(" ");
                t.sendMessage(ChatColor.YELLOW + msg);

                for (Player playerInRadius : t.getLocation().getNearbyEntitiesByType(Player.class, 10)) {
                    if (!playerInRadius.equals(t))
                        playerInRadius.sendMessage(Component.text(ChatColor.RED + t.getName() + " erhielt eine Brieftaube!"));
                }

                Bukkit.getScheduler().cancelTask(taskid);
            }

        }
    }
}

enum SchedulerType {
    Countdown, Taube
}

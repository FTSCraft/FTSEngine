package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.commands.CMDtaube;
import de.ftscraft.ftsengine.main.Engine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CountdownScheduler implements Runnable {

    private final Engine plugin;
    private final Player p;
    private Player t;
    private CMDtaube.TaubeMessage msg;
    private int seconds;
    private final int taskid;
    private SchedulerType type = SchedulerType.Countdown; // Default to Countdown to avoid null pointer if not set

    public CountdownScheduler(Engine plugin, int seconds, Player p) {
        this.plugin = plugin;
        this.p = p;
        this.seconds = seconds;
        this.taskid = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    public CountdownScheduler(Engine plugin, int seconds, Player p, Player t, CMDtaube.TaubeMessage taubeMessage) {
        this.plugin = plugin;
        this.seconds = seconds;
        this.p = p;
        this.type = SchedulerType.Taube;
        this.t = t;
        this.msg = taubeMessage;
        this.taskid = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 20, 20);
    }

    private boolean shouldReceiveTaubeNotification(Player player) {
        return player.getGameMode() != GameMode.SPECTATOR && !isPlayerVanished(player);
    }

    private boolean isPlayerVanished(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player) && !onlinePlayer.canSee(player)) {
                return true;
            }
        }
        return false;
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
                if (p != null) {
                    for (Entity e : p.getNearbyEntities(15, 15, 15)) {
                        if (e instanceof Player) {
                            e.sendMessage("§cDer Countdown ist abgelaufen!");
                        }
                    }
                    p.sendMessage("§cDer Countdown ist abgelaufen!");
                }
            }

        } else if (type == SchedulerType.Taube) {

            seconds--;

            if (seconds <= 0) { // Safety check: <= 0 instead of == 0
                if (t != null && t.isOnline()) {
                    t.playSound(t.getLocation(), Sound.ENTITY_BAT_LOOP, 3, -20);

                    if (p != null && p.isOnline()) {
                        p.sendMessage(Messages.PREFIX + "Deine Taube ist angekommen!");
                    }

                    t.sendMessage(Component.text("§c---------------"));

                    // FIX IS HERE: We use msg.senderDisplayName() instead of p.getName()
                    t.sendMessage(Component.text("§eEine Brieftaube von §c" + msg.senderDisplayName() + "§e hat dich erreicht!"));

                    t.sendMessage(Component.text(" "));
                    t.sendMessage(Component.text(msg.message(), NamedTextColor.YELLOW));
                    t.sendMessage(Component.text("§c---------------"));

                    // clickable message for Brief
                    Component clickableMessage = Component.text()
                            .append(Component.text(Messages.PREFIX + "§7[§aBrief!§7]", NamedTextColor.YELLOW))
                            .clickEvent(ClickEvent.runCommand("/taube get " + msg.uuid()))
                            .hoverEvent(HoverEvent.showText(Component.text("§7Klicke um den Brief von der Taube zu nehmen.", NamedTextColor.GRAY)))
                            .build();

                    t.sendMessage(clickableMessage);

                    if (shouldReceiveTaubeNotification(t)) {
                        for (Player playerInRadius : t.getLocation().getNearbyEntitiesByType(Player.class, 10)) {
                            if (!playerInRadius.equals(t)) {
                                playerInRadius.sendMessage(Component.text(t.getName() + " erhielt eine Brieftaube!", NamedTextColor.RED));
                            }
                        }
                    }
                }

                Bukkit.getScheduler().cancelTask(taskid);
            }
        }
    }
}

enum SchedulerType {
    Countdown, Taube
}
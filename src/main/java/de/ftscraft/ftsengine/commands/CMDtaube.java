package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.CountdownScheduler;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDtaube implements CommandExecutor {

    private final Engine plugin;

    public CMDtaube(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("taube").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length > 1) {
            Player p = (Player) cs;


            String[] player = args[0].split(",");

            Location pl = p.getLocation();

            //If you want to send a taube to only one or more specific player*s

            for (String s : player) {

                Player t = Bukkit.getPlayer(s);
                if (t == null) {
                    p.sendMessage(Messages.PREFIX + "§7Der Spieler §c" + s + " §7wurde nicht gefunden");
                } else {
                    Location tl = t.getLocation();
                    int seconds;
                    if (pl.getWorld() != tl.getWorld()) {
                        seconds = 120;
                    } else {
                        double distance = t.getLocation().distance(p.getLocation());
                        if (distance < 350) {
                            seconds = 5;
                        } else {
                            seconds = (int) distance / 70;
                        }
                        if (seconds > 120)
                            seconds = 120;
                    }

                    StringBuilder msg = new StringBuilder();

                    for (int i2 = 1; i2 < args.length; i2++) {
                        msg.append(" ").append(args[i2]);
                    }

                    p.sendMessage(Messages.PREFIX + "Eine Taube fliegt zu §c" + t.getName() + "§7.");

                    p.playSound(p.getLocation(), Sound.ENTITY_BAT_LOOP, 3, -20);

                    new CountdownScheduler(plugin, seconds, p, t, msg.toString());
                }

            }

            return true;

        }

        return false;
    }
}

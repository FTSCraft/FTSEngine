package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.CountdownScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDtaube implements CommandExecutor
{

    private Engine plugin;

    public CMDtaube(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("taube").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        if(args.length > 1) {
            Player p = (Player)cs;


            String[] player = args[0].split(",");

            Location pl = p.getLocation();

            for(int i = 0; i<player.length; i++) {
                Player t = Bukkit.getPlayer(player[i]);
                if(t == null) {
                    p.sendMessage(plugin.msgs.PREFIX+"§7Der Spieler §c"+player[i]+" §7wurde nicht gefunden");
                } else {
                    Location tl = t.getLocation();
                    if(pl.getWorld().getName() != tl.getWorld().getName()) {
                        p.sendMessage(plugin.msgs.NOT_IN_WORLD.replace("%s",args[i]));
                        return true;
                    }
                    int disctance = (int) pl.distance(tl);
                    int seconds = disctance / 5;

                    String msg = "";

                    for(int i2 = 1; i2<args.length; i2++) {
                        msg += " " + args[i2];
                    }

                    p.sendMessage("§eEine Taube fliegt zu §c"+t.getName()+"§7.");

                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BAT_LOOP, 200, -20);

                    new CountdownScheduler(plugin, seconds, p, t, msg);
                }
            }

        }

        return false;
    }
}

package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.reisepunkt.Reisepunkt;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static java.lang.Math.*;

public class CMDreisepunkt implements CommandExecutor {

    private Engine plugin;

    public CMDreisepunkt(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("reisepunkt").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }
        Player p = (Player) cs;

        if (args.length >= 1) {

            /*
             *
             * Sntax: Createpoint:
             * /reisepunk createpoint <Abreiseort Name> <Ankunftsort Warp> <Alle wv. Sekunden> <Radius>
             *
             */

            if (args[0].equalsIgnoreCase("createpoint")) {
                if (args.length == 5) {
                    String abreise = args[1];
                    String warp = args[2];
                    int seconds;
                    int radius;
                    try {
                        seconds = Integer.valueOf(args[3]);
                        radius = Integer.valueOf(args[4]);
                    } catch (NumberFormatException e) {
                        cs.sendMessage("§cDas ist keine gültige Zahl");
                        return true;
                    }

                    new Reisepunkt(plugin, ((Player) cs).getLocation(), abreise, warp, seconds, radius, 2);
                    cs.sendMessage("§cDer Reisepunk wurde erstellt");
                }
            }

        }

        return true;

    }
}

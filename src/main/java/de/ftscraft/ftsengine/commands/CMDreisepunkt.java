package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.reisepunkt.Reisepunkt;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDreisepunkt implements CommandExecutor
{

    private Engine plugin;

    public CMDreisepunkt(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("reisepunkt").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if (!(cs instanceof Player))
        {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }
        Player p = (Player) cs;

        p.sendMessage("§cDieser Command ist derzeit in Wartung. Tut uns leid");
        return true;

        /* TODO: ENTFERNEN
        Location loc = p.getLocation();
        int duration = 0;
        if (cs.hasPermission("ftsengine.reisepunkt"))
        {
            if (args.length > 0)
            {
                if (args[0].equalsIgnoreCase("setzen"))
                {
                    if (args.length == 3)
                    {
                        String name = args[1];

                        try
                        {
                            duration = Integer.valueOf(args[2]);
                        } catch (NumberFormatException e)
                        {
                            p.sendMessage(plugin.msgs.USAGE_NUMBER);
                            return true;
                        }

                        new Reisepunkt(plugin, name, loc, duration);

                        p.sendMessage("§cDu hast erfolgreich ein Reisepunkt erstellt!");
                    } else plugin.getVar().sendReisepunkHelpMsg(p);
                } else if (args[0].equalsIgnoreCase("list"))
                {
                    if (args.length == 1)
                    {
                        for (Reisepunkt a : plugin.reisepunkte)
                        {
                            p.sendMessage("§c- " + a.getName());
                        }
                    } else plugin.getVar().sendReisepunkHelpMsg(p);

                } else if (args[0].equalsIgnoreCase("löschen"))
                {
                    String name = args[0];
                    boolean found = false;
                    for(Reisepunkt a : plugin.reisepunkte) {
                        if(name.equalsIgnoreCase(a.getName())) {
                            a.delete();
                            found = true;
                            break;
                        }
                    }
                    if(found)
                        p.sendMessage("§cDer Reisepunkt wurde erfolgreich gelöscht.");
                    else p.sendMessage("§cDer Reisepunkt wurde nicht gefunden und daher nicht gelöscht.");
                } else if(args[0].equalsIgnoreCase("ziel")) {
                    if(args.length == 2) {
                        String name = args[0];
                        boolean found = false;
                        for(Reisepunkt a : plugin.reisepunkte) {
                            if(name.equalsIgnoreCase(a.getName())) {
                                a.setZiel(p.getLocation());
                                found = true;
                                break;
                            }
                        }
                        if(found)
                            p.sendMessage("§cDas Ziel wurde erfolgreich gesetzt");
                        else p.sendMessage("§cDer Reisepunk wurde nicht gefunden.");
                    } else plugin.getVar().sendReisepunkHelpMsg(p);
                }
            } else plugin.getVar().sendReisepunkHelpMsg(p);
        } else p.sendMessage(plugin.msgs.PREFIX + "Dafür hast du keine Rechte!");
        return false;
       */
    }
}

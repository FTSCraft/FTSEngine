package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDbrief implements CommandExecutor
{

    private Engine plugin;

    public CMDbrief(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("brief").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if (!(cs instanceof Player))
        {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        /*if(cs instanceof Player) {
            cs.sendMessage("§eDieser Command ist noch in Arbeit!");
            return true;
        }*/

        Player p = (Player) cs;

        if (args.length >= 1)
        {

            String msg = "";
            String name = p.getName();
            for (int i = 0; i < args.length; i++)
            {
                msg += " " + args[i];
            }
            msg = msg.replace("ä", "ae");
            msg = msg.replaceAll("ü", "ue");
            msg = msg.replaceAll("ß", "ss");
            msg = msg.replaceAll("ö", "oe");
            if (msg.startsWith(" anonym"))
            {
                if (plugin.getEcon().has(p, 2))
                {
                    plugin.getEcon().withdrawPlayer(p, 2);
                    p.sendMessage("§cDieser Brief ist nun Anonym!");
                    msg = msg.replaceFirst(" anonym", " ");
                    name = "XXXX";
                } else
                {
                    p.sendMessage("Dafür hast du kein Geld!");
                    return true;
                }
            }


            Brief brief = new Brief(plugin, name, msg, p.getWorld().getName());
            p.getInventory().addItem(brief.getMap(p.getWorld()));

            return false;
        }


        return true;
    }

}


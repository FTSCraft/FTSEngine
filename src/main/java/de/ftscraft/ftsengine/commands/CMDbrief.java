package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.courier.BriefLieferung;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.courier.FTSMapRenderer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.UUID;

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

            if (args[0].equalsIgnoreCase("senden"))
            {
                if (args.length == 2)
                {

                    OfflinePlayer t = Bukkit.getOfflinePlayer(args[1]);

                    new BriefLieferung(plugin, p.getUniqueId().toString(), t.getUniqueId().toString());
                    return true;
                }
            }

            String msg = "";
            for (int i = 0; i < args.length; i++)
            {
                msg += " " + args[i];
            }
            msg = msg.replace("ä", "ae");
            msg = msg.replaceAll("ü", "ue");
            msg = msg.replaceAll("ß", "ss");
            msg = msg.replaceAll("ö", "oe");

            Brief brief = new Brief(plugin, p.getName(), msg, p.getWorld().getName());
            p.getInventory().addItem(brief.getMap(p.getWorld()));

            return false;
        }

        return true;
    }
}


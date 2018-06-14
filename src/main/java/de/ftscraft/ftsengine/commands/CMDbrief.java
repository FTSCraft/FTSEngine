package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        if(!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        Player p = (Player)cs;

        ItemStack mapItem = new ItemStack(Material.EMPTY_MAP, 1);
        String msg = "";
        for(int i = 0; i < args.length; i++) {
            msg += " " + args[i];
        }
        msg = msg.replace("ä", "ae");
        msg = msg.replaceAll("ü", "ue");
        msg = msg.replaceAll("ß", "ss");
        msg = msg.replaceAll("ö", "oe");
        plugin.briefMsg.add(msg);
        p.getInventory().addItem(mapItem);

        return false;
    }
}

package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CMDremovearmorstand implements CommandExecutor
{

    private final Engine plugin;

    public CMDremovearmorstand(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("removearmorstand").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(cs.hasPermission("ftsengine.armorstand")) {
            if(!(cs instanceof Player)) {
                cs.sendMessage(Messages.PREFIX);
                return true;
            }

            Player p = (Player)cs;

            for (ArmorStand armorStand : p.getLocation().getNearbyEntitiesByType(ArmorStand.class, 5)) {
                if(!armorStand.isVisible())
                    armorStand.remove();
            }

        }
        return false;
    }
}

package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDremovearmorstand implements CommandExecutor
{

    public CMDremovearmorstand(Engine plugin)
    {
        plugin.getCommand("removearmorstand").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        if(cs.hasPermission("ftsengine.armorstand")) {
            if(!(cs instanceof Player p)) {
                cs.sendMessage(Messages.ONLY_PLAYER);
                return true;
            }

            for (ArmorStand armorStand : p.getLocation().getNearbyEntitiesByType(ArmorStand.class, 5)) {
                if(!armorStand.isVisible())
                    armorStand.remove();
            }

        } else cs.sendMessage(Messages.NO_PERMISSIONS);
        return false;
    }
}

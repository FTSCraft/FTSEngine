package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
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
                cs.sendMessage(plugin.msgs.PREFIX);
                return true;
            }

            Player p = (Player)cs;

            for(Entity e : p.getWorld().getEntities()) {
                if(e instanceof ArmorStand) {
                    if(!((ArmorStand) e).isVisible()) {
                        e.remove();
                    }
                }
            }

        }
        return false;
    }
}

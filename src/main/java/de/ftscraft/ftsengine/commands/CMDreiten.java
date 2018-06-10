package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDreiten implements CommandExecutor
{

    private Engine plugin;

    public CMDreiten(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("reiten").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        Player p = (Player)cs;

        if(plugin.getReiter().contains(p)) {
            p.sendMessage(plugin.msgs.NEED_TO_CLICK_ENTITY);
            return true;
        } else plugin.getReiter().add(p);
        p.sendMessage(plugin.msgs.NEED_TO_CLICK_ENTITY);

        return false;
    }
}

package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDreiten implements CommandExecutor
{

    private final Engine plugin;

    public CMDreiten(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("reiten").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        Player p = (Player)cs;

        if(plugin.getReiter().contains(p)) {
            p.sendMessage(Messages.NEED_TO_CLICK_ENTITY);
            return true;
        } else plugin.getReiter().add(p);
        p.sendMessage(Messages.NEED_TO_CLICK_ENTITY);

        return false;
    }
}

package de.ftscraft.personalausweis.commands;

import de.ftscraft.personalausweis.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDroleplay implements CommandExecutor
{

    private Engine plugin;

    public CMDroleplay(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("roleplay").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        Player p = (Player)cs;
        if(plugin.getTeam().hasPlayer(p))
        {
            plugin.getTeam().removePlayer(p);
            p.sendMessage(plugin.msgs.NOT_IN_ROLEPLAY);
            return true;
        }
        plugin.getTeam().addPlayer(p);
        p.sendMessage(plugin.msgs.NOW_IN_ROLEPLAY);

        return false;
    }
}

package de.ftscraft.personalausweis.commands;

import de.ftscraft.personalausweis.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CMDengine implements CommandExecutor
{

    private Engine plugin;

    public CMDengine(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("engine").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("roleplay")) {
                
            }
        }

        return false;

    }
}

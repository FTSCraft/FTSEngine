package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CMDchannel implements CommandExecutor, TabCompleter
{

    public CMDchannel(Engine plugin)
    {
        plugin.getCommand("channel").setExecutor(this);
        plugin.getCommand("channel").setTabCompleter(this);
    }

    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if(args.length == 1) {

        }

        return false;
    }

    public List<String> onTabComplete(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args)
    {
        final List<String> com = new ArrayList<>(Arrays.asList("roleplay", "handel", "flüstern", "rufen"));
        //StringUtil.copyPartialMatches(args[0], com, com);
        Collections.sort(com);
        return com;
    }
}

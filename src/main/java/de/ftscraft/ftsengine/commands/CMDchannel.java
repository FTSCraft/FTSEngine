package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.chat.ChatChannels;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CMDchannel implements CommandExecutor, TabCompleter
{

    private Engine plugin;

    public CMDchannel(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("channel").setExecutor(this);
        plugin.getCommand("channel").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        if(args.length == 1) {
            String channel = args[0];
            Player p = (Player)cs;
            if(channel.equalsIgnoreCase("roleplay")) {
                plugin.getChats().put(p, ChatChannels.ROLEPLAY);
                p.sendMessage(plugin.msgs.NOW_IN_CHANNEL.replace("%s", "Roleplay"));
            } else if(channel.equalsIgnoreCase("handel")) {
                plugin.getChats().put(p, ChatChannels.HANDEL);
                p.sendMessage(plugin.msgs.NOW_IN_CHANNEL.replace("%s", "Handel"));
            } else if(channel.equalsIgnoreCase("flüstern")) {
                plugin.getChats().put(p, ChatChannels.FLÜSTERN);
                p.sendMessage(plugin.msgs.NOW_IN_CHANNEL.replace("%s", "Flüster"));
            } else if(channel.equalsIgnoreCase("rufen")) {
                plugin.getChats().put(p, ChatChannels.RUFEN);
                p.sendMessage(plugin.msgs.NOW_IN_CHANNEL.replace("%s", "Ruf"));
            }

        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args)
    {
        final List<String> com = new ArrayList<>(Arrays.asList("roleplay", "handel", "flüstern", "rufen"));
        //StringUtil.copyPartialMatches(args[0], com, com);
        Collections.sort(com);
        return com;
    }
}

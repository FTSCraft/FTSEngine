package de.ftscraft.ftsengine.chat;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatStringFormatter
{

    private Engine plugin;

    public ChatStringFormatter(Engine plugin)
    {
        this.plugin = plugin;
    }

    public String format(String msg, Player p, ChatChannel chatChannel)
    {
        /*
        String format = chatChannel.getFormat();
        MPlayer mPlayer = MPlayer.get(p);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("{faction}", mPlayer.getFactionName());
        format = format.replace("{playername}", p.getName());
        format = format.replace("{groupprefix}", ChatColor.translateAlternateColorCodes('&', plugin.getChat().getGroupPrefix(p.getWorld(), plugin.getChat().getPrimaryGroup(p))));
        format = format.replace("{playerprefix}", ChatColor.translateAlternateColorCodes('&', plugin.getChat().getPlayerPrefix(p)));
        format = format.replace("{channelprefix}", chatChannel.getPrefix());
        format = format.replace("{channelname}", chatChannel.getName());
        if (p.hasPermission("ftsengine.chat.color"))
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        format = format.replace("{msg}", msg);

        return format;
        */
        return "";
    }

}

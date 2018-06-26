package de.ftscraft.ftsengine.chat;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ChatChannel
{

    private Engine plugin;
    private ChatStringFormatter formatter;

    private ArrayList<OfflinePlayer> players;
    private String name;
    private String prefix;
    private String format;
    private int distance;
    private String permission;


    public ChatChannel(Engine plugin, String name, String prefix, String format, int distance)
    {
        this.plugin = plugin;
        this.formatter = new ChatStringFormatter(plugin);
        this.players = new ArrayList<>();
        this.name = name;
        this.prefix = prefix;
        this.format = format;
        this.distance = distance;
    }

    public void sendMessage(Player p, String msg) {
        String message = formatter.format(msg, p, this);
        for(Player a : Bukkit.getOnlinePlayers()) {
            boolean reached = false;
            if(distance != -1)
            {
                if (a.getLocation().distance(p.getLocation()) <= distance)
                {
                    reached = true;
                }
            } else reached = true;

            if(reached) {
                a.sendMessage(message);
            }
        }
    }

    public String getName()
    {
        return name;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public ArrayList<OfflinePlayer> getPlayers()
    {
        return players;
    }

    public int getDistance()
    {
        return distance;
    }

    public String getFormat()
    {
        return format;
    }

}

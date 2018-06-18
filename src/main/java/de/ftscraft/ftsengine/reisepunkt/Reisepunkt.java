package de.ftscraft.ftsengine.reisepunkt;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;

public class Reisepunkt
{

    private String name;
    private Location loc;
    private Location ziel;
    private int duration;

    private Engine plugin;

    public Reisepunkt(Engine plugin, String name, Location loc, int duration)
    {
        this.name = name;
        this.loc = loc;
        this.duration = duration;
        this.plugin = plugin;
        this.ziel = null;
    }


    public String getName()
    {
        return name;
    }

    public void delete()
    {

    }

    public void setZiel(Location ziel)
    {
        this.ziel = ziel;
    }

    public Location getLocation()
    {
        return loc;
    }

    public Location getZiel()
    {
        return ziel;
    }

    public int getDuration()
    {
        return duration;
    }
}

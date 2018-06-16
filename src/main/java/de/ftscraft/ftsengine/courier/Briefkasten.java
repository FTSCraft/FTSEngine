package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;
import org.bukkit.block.Chest;

import java.util.ArrayList;
import java.util.UUID;

public class Briefkasten
{

    private Engine plugin;
    private UUID owner;
    private ArrayList<Brief> briefe;
    private Chest chest;

    public Briefkasten(Engine plugin, UUID owner, Location loc)
    {
        this.plugin = plugin;
        this.owner = owner;
        this.briefe = new ArrayList<>();
        this.chest = (Chest) loc.getBlock();
    }

    public UUID getOwner()
    {
        return owner;
    }

    public ArrayList<Brief> getBriefe()
    {
        return briefe;
    }

    public Chest getChest()
    {
        return chest;
    }
}

package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Briefkasten
{

    private Engine plugin;
    private String owner;
    private ArrayList<Brief> briefe;
    private Chest chest;

    public Briefkasten(Engine plugin, String owner, Location loc)
    {
        this.plugin = plugin;
        this.owner = owner;
        this.briefe = new ArrayList<>();
        if(loc.getBlock().getType() == Material.CHEST || loc.getBlock().getType() == Material.TRAPPED_CHEST)
            this.chest = (Chest) loc.getBlock().getState();
        else destory();

        this.chest.setCustomName("§2Briefkasten §c"+ Bukkit.getOfflinePlayer(UUID.fromString(owner)).getName());
        plugin.briefkasten.put(owner, this);
    }

    public String getOwner()
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

    public void safe() {
        File file = new File(plugin.getDataFolder() + "//briefkasten//"+owner+".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("owner", owner);
        cfg.set("location.x", chest.getLocation().getX());
        cfg.set("location.y", chest.getLocation().getY());
        cfg.set("location.z", chest.getLocation().getZ());
        cfg.set("location.world", chest.getLocation().getWorld().getName());

        try
        {
            cfg.save(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void destory() {
        File file = new File(plugin.getDataFolder() + "//briefkasten//"+owner.toString()+".yml");
        file.delete();
        plugin.briefkasten.remove(owner);
    }

}

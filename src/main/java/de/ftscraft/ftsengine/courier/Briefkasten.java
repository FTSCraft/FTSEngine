package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;
import org.bukkit.block.Chest;

import java.util.UUID;

public class Briefkasten {

    private Engine plugin;

    private Location location;
    private Chest chest;

    private UUID player;

    public Briefkasten(Engine plugin, Location location, UUID player) {
        this.plugin = plugin;
        this.location = location;
        this.player = player;
    }
}

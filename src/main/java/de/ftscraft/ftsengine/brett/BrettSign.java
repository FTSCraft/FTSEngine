package de.ftscraft.ftsengine.brett;

import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.UUID;

public class BrettSign
{

    private Sign sign;
    private Location location;
    private UUID creator;

    BrettSign(Sign sign, Location location, UUID creator) {
        this.creator = creator;
        this.location = location;
        this.sign = sign;
    }

    public Location getLocation()
    {
        return location;
    }

    public Sign getSign()
    {
        return sign;
    }

    public UUID getCreator()
    {
        return creator;
    }


}

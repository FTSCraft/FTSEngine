package de.ftscraft.ftsengine.brett;

import org.bukkit.Location;
import org.bukkit.block.Sign;

public class BrettSign
{

    private Sign sign;
    private Location location;
    private String creator;

    BrettSign(Sign sign, Location location, String creator) {
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

    public String getCreator()
    {
        return creator;
    }


}

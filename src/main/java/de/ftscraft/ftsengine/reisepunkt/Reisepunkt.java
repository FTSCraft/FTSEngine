package de.ftscraft.ftsengine.reisepunkt;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.sin;

public class Reisepunkt
{

    private String abreise, warp;
    private final int seconds, radius;

    double t = 0;
    double r;


    private int duration;

    private Location location;

    private Engine plugin;

    public Reisepunkt(Engine plugin, Location location, String abreise, String warp, int seconds, int radius, int costs)
    {
        this.plugin = plugin;
        this.abreise = abreise;
        this.warp = warp;
        this.seconds = seconds;
        this.radius = radius;
        this.location = location;
        this.duration = seconds;
        plugin.reisepunkte.add(this);
    }


    public void secondDown() {

        drawCircle(location, radius);

        duration--;
        if(duration == 15) {

        }
        if(duration == 0) {
            for(final Entity e : location.getWorld().getNearbyEntities(location, radius, 1, radius)) {
                if(e instanceof Player) {
                    ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 2*50, 100));
                    ((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 2*50, 2));
                    ((Player) e).sendTitle("Du kommst in "+warp+" an.", "", 5, 20, 5);
                    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "warp "+warp+" "+e.getName());
                        }
                    }, 40);
                }
            }
            duration = seconds;
        }
    }

    public String getAbreise() {
        return abreise;
    }

    public String getWarp() {
        return warp;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getRadius() {
        return radius;
    }

    public int getDuration() {
        return duration;
    }

    private void drawCircle(Location loc, int radius) {
        for (double t = 0; t < 50; t += 0.5) {
            float x = radius * (float) sin(t);
            float z = radius * (float) cos(t);
            double y = loc.getY();
            loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, x + loc.getX(), y, z + loc.getZ(), 0);
        }
    }

}

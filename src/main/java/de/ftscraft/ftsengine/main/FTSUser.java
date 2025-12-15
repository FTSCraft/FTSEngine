package de.ftscraft.ftsengine.main;

import de.ftscraft.ftsengine.feature.brett.Brett;
import org.bukkit.entity.Player;

public class FTSUser {

    private final Engine plugin;
    private final Player player;
    private Brett currentBrett = null;

    public FTSUser(Engine plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    private int lanzenschlaege;

    public int getLanzenschlaege() {
        return lanzenschlaege;
    }

    public void setLanzenschlaege(int lanzenschlaege) {
        this.lanzenschlaege = lanzenschlaege;
    }

    public Brett getBrett() {
        return currentBrett;
    }

    public void setBrett(Brett brett) {
        this.currentBrett = brett;
    }
}

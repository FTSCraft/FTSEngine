package de.ftscraft.ftsengine.main;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Var;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.material.Stairs;

public class FTSUser {

    private final Engine plugin;
    private final Player player;

    private Location beforeSitting;

    private WrappedGameProfile oldProfile;
    private WrappedGameProfile newProfile;

    private String disguiseName;

    private Briefkasten briefkasten;

    private Brett currentBrett = null;

    public FTSUser(Engine plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        Ausweis ausweis;
        if (plugin.getAusweis(player) != null)
            ausweis = plugin.getAusweis(player);
        else ausweis = null;
        sits = false;

    }

    private boolean sits = false;
    ArmorStand sit_stand;
    private int lanzenschlaege;

    public void setSitting(Block block) {

        this.beforeSitting = player.getLocation().clone();

        Location loc = block.getLocation();
        if (player.getLocation().distance(loc) >= 2) {
            return;
        }

        if (sits)
            return;

        if (sit_stand != null) {
            sit_stand.remove();
            sit_stand = null;
        }

        if (block.getBlockData() instanceof Slab) {
            Location loc2 = block.getLocation().clone().subtract(-0.5D, 1.2D, -0.4D);
            loc2.setYaw(player.getLocation().getYaw());
            sit_stand = block.getLocation().getWorld().spawn(loc2, ArmorStand.class);
        } else {

            if (!plugin.mats.contains(block.getType())) {
                if (plugin.getVar().getNoStairs().contains(block.getType())) {
                    Location loc2 = block.getLocation().clone().subtract(-0.5D, 1.2D, -0.4D);
                    sit_stand = block.getLocation().getWorld().spawn(loc2, ArmorStand.class);
                } else {
                    try {
                        BlockState state = block.getState();
                        Stairs stair = (Stairs) state.getData();
                        BlockFace face = stair.getFacing();
                        Block up = block.getLocation().clone().add(0, 1, 0).getBlock();
                        if (up.getState().getType() != Material.AIR)
                            return;
                        if (face == BlockFace.DOWN)
                            return;
                        Location loc2 = block.getLocation().clone().subtract(-0.5D, 1.2D, -0.4D);
                        loc2.setYaw(plugin.getVar().getYawByBlockFace(face));
                        sit_stand = block.getLocation().getWorld().spawn(loc2, ArmorStand.class);
                        player.teleport(sit_stand);
                    } catch (Exception ignored) {

                    }
                }
            } else {
                Location loc2 = block.getLocation().clone().add(0.5D, 0.0D, 0.5D).subtract(0, 0.75D, 0);
                if (Var.getCarpets().contains(block.getType())) {
                    loc2 = block.getLocation().clone().add(0.5D, 0.0D, 0.5D).subtract(0, 1.65D, 0);
                }
                loc2.setYaw(player.getLocation().getYaw());
                sit_stand = block.getLocation().getWorld().spawn(loc2, ArmorStand.class);
            }
        }

        sit_stand.setGravity(false);
        sit_stand.setVisible(false);
        sit_stand.addPassenger(player);

        this.sits = true;
    }

    public boolean isSitting() {
        return sits;
    }

    public void abortSitting() {

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            sit_stand.remove();
            if (player.teleport(beforeSitting)) {

            }
            sits = false;

        }, 3);

    }

    public int getLanzenschlaege() {
        return lanzenschlaege;
    }

    public void setLanzenschlaege(int lanzenschlaege) {
        this.lanzenschlaege = lanzenschlaege;
    }

    public void leave() {
        plugin.getPlayer().remove(player);
        if (sits)
            abortSitting();
    }

    public void sendMsg(String msg) {
        if (player != null)
            player.sendMessage(msg);
    }


    public String getDisguiseName() {
        return disguiseName;
    }

    public WrappedGameProfile getOldProfile() {
        return oldProfile;
    }

    public WrappedGameProfile getNewProfile() {
        return newProfile;
    }


    public Brett getBrett() {

        return currentBrett;

    }

    public void setBrett(Brett brett) {

        this.currentBrett = brett;

    }
}

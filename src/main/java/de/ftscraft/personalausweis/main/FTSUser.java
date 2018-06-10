package de.ftscraft.personalausweis.main;

import de.ftscraft.personalausweis.pferd.Pferd;
import de.ftscraft.personalausweis.utils.Ausweis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.material.Stairs;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;

public class FTSUser
{

    private Engine plugin;
    private Player player;
    private ArrayList<Pferd> pferde;

    public FTSUser(Engine plugin, Player player)
    {
        this.plugin = plugin;
        this.player = player;
        if(plugin.getAusweis(player) != null)
            this.ausweis = plugin.getAusweis(player);
        else this.ausweis = null;
        sits = false;
        pferde = new ArrayList<>();

        for(Pferd p : plugin.pferde.values()) {
            if(p.getOwner().toString().equals(player.getUniqueId().toString())) {
                this.pferde.add(p);
            }
        }

    }

    private Ausweis ausweis;
    private boolean sits;
    private ArmorStand sit_stand;
    private int lanzenschlaege;

    public void setSitting(Block block) {
        ArmorStand seat;
        if(block.getType() != Material.GRASS_PATH)
        {
            BlockState state = block.getState();
            Stairs stair = (Stairs)state.getData();
            BlockFace face = stair.getFacing();
            Block up = block.getLocation().clone().add(0, 1, 0).getBlock();
            if(up.getState().getType() != Material.AIR)
                return;
            if(face == BlockFace.DOWN)
                return;
            Location loc2 = block.getLocation().clone().subtract(-0.5D, 1.2D, -0.4D);
            loc2.setYaw(plugin.getVar().getYawByBlockFace(face));
            seat = block.getLocation().getWorld().spawn(loc2, ArmorStand.class);
            player.teleport(seat);
        }
        else {
            seat = block.getLocation().getWorld().spawn(block.getLocation().clone().add(0.25D, 0.0D, 0.5D).subtract(0,0.75D,0), ArmorStand.class);
        }
        seat.setGravity(false);
        seat.setVisible(false);
        seat.addPassenger(player);
        this.sit_stand = seat;
        this.sits = true;
    }

    public boolean isSitting() {
        return sits;
    }

    public void abortSitting() {
        sit_stand.remove();
        sit_stand = null;
        sits = false;
    }

    public int getLanzenschlaege()
    {
        return lanzenschlaege;
    }

    public void setLanzenschlaege(int lanzenschlaege)
    {
        this.lanzenschlaege = lanzenschlaege;
    }

    public void leave()
    {
        plugin.getPlayer().remove(player);
        if(sits)
            abortSitting();
    }

    public void addPferd(Pferd pf) {
        pferde.add(pf);
    }

    public ArrayList<Pferd> getPferde()
    {
        return pferde;
    }

    public boolean ownsHorse(Horse h) {
        for(Pferd pferd : pferde) {
            if(pferd.getUUID().toString().equalsIgnoreCase(h.getUniqueId().toString()))
                return true;
        }
        return false;
    }

    public boolean pferdIDIsDa(int id) {
        for(Pferd a : pferde) {
            if(a.getPersID() == id)
                return true;
        }
        return false;
    }

    public Pferd getChosedPferd() {
        for(Pferd pferd : pferde) {
            if(pferd.isChosed())
            {
                return pferd;
            }
        }
        return null;
    }

    public void sendMsg(String msg) {
        if(player != null)
            player.sendMessage(msg);
    }

    public void setChosedPferd(String chosedPferd)
    {
        for(Pferd p : pferde) {
            if(p.isChosed()) {
                p.setChosed(false);
            }
            if(p.getName().equalsIgnoreCase(chosedPferd)) {
                p.setChosed(true);
                player.sendMessage("§eDu hast nun "+p.getName() + " ausgewählt!");
                return;
            }
        }
        sendMsg("§eDu hast kein Pferd was so heißt!");
    }

    public void removePferd(Pferd pferd)
    {
        if(pferde.contains(pferd))
            pferde.remove(pferd);
    }
}

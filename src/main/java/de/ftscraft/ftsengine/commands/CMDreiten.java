package de.ftscraft.ftsengine.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class CMDreiten implements CommandExecutor {

    private final Engine plugin;

    public CMDreiten(Engine plugin) {
        this.plugin = plugin;
        //noinspection DataFlowIssue
        plugin.getCommand("reiten").setExecutor(this);
        addPacketListener();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        plugin.getReiter().add(p);
        p.sendMessage(Messages.NEED_TO_CLICK_ENTITY);

        return false;
    }

    private void addPacketListener() {
        plugin.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {

                    PacketContainer pc = event.getPacket();
                    Player player = event.getPlayer();
                    Entity vehicle = player.getVehicle();
                    if (vehicle == null || vehicle instanceof Vehicle || vehicle instanceof Player)
                        return;

                    float side = pc.getFloat().read(0);

                    float forw = pc.getFloat().read(1);

                    boolean jump = pc.getBooleans().read(0);
                    if (jump && vehicle.isOnGround()) {
                        vehicle.setVelocity(vehicle.getVelocity().add(new Vector(0.0, 0.55, 0.0)));
                    }

                    Vector vel = getVelocityVector(vehicle.getVelocity(), player, side, forw);
                    vehicle.setVelocity(vel);
                    vehicle.setRotation(player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch());
                    vehicle.getLocation().setYaw(player.getEyeLocation().getYaw());
                    vehicle.getLocation().setPitch(player.getEyeLocation().getPitch());
                }
            }
        });
    }

    private static Vector getVelocityVector(Vector vector, Player player, float side, float forw) {
        vector.setX(0.0);
        vector.setZ(0.0);

        Vector mot = new Vector(forw * -1.0, 0, side);

        if (mot.length() > 0.0) {
            mot.rotateAroundY(Math.toRadians(player.getLocation().getYaw() * -1.0F + 90.0F));
            mot.normalize().multiply(0.25F);
        }
        return mot.add(vector);
    }

}

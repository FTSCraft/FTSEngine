package de.ftscraft.ftsengine.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.*;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;

public class CMDreiten implements CommandExecutor {

    private final Engine plugin;
    private final Map<Player, Vector> playerVectorMap = new ConcurrentHashMap<>();

    public CMDreiten(Engine plugin) {
        this.plugin = plugin;
        //noinspection DataFlowIssue
        Objects.requireNonNull(plugin.getCommand("reiten"), "tried registering reiten command but is null").setExecutor(this);
        addPacketListener();
        startLoop();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String @NotNull [] args) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        plugin.getReiter().add(p);
        p.sendMessage(Messages.NEED_TO_CLICK_ENTITY);

        return false;
    }

    private void startLoop() {

        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> playerVectorMap.forEach((player, vector) -> {
            Entity vehicle = player.getVehicle();
            if (vehicle == null) {
                playerVectorMap.remove(player);
                return;
            }

            Vector vel = getVelocityVector(vehicle.getVelocity(), player, (float) vector.getZ(), (float) vector.getX());

            vehicle.setVelocity(vel);
            vehicle.setRotation(player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch());
            vehicle.getLocation().setYaw(player.getEyeLocation().getYaw());
            vehicle.getLocation().setPitch(player.getEyeLocation().getPitch());

            if (vehicle instanceof Mob mob)
                mob.getPathfinder().stopPathfinding();

        }), 0, 1);

    }

    private void addPacketListener() {
        plugin.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.STEER_VEHICLE) {

                    PacketContainer pc = event.getPacket();
                    Player player = event.getPlayer();
                    Entity vehicle = player.getVehicle();
                    if (vehicle == null || vehicle instanceof Vehicle || vehicle instanceof Player) return;

                    InternalStructure input = pc.getStructures().read(0);

                    int forward = input.getBooleans().read(0) ? 1 : 0;
                    int backward = input.getBooleans().read(1) ? -1 : 0;
                    int left = input.getBooleans().read(2) ? 1 : 0;
                    int right = input.getBooleans().read(3) ? -1 : 0;
                    boolean jump = input.getBooleans().read(4);

                    float side = left + right;
                    float forw = forward + backward;

                    if (jump && vehicle.isOnGround()) {
                        vehicle.setVelocity(vehicle.getVelocity().add(new Vector(0.0, 0.55, 0.0)));
                    }

                    playerVectorMap.put(player, new Vector(forw, 0, side));
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

package de.ftscraft.ftsengine.commands.emotes;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CMDstreicheln implements CommandExecutor {
    private final Engine plugin;
    private final Map<UUID, Long> activePetters = new HashMap<>();
    public final long PET_MODE_DURATION = 10000;

    public CMDstreicheln(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("streicheln").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if(!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (plugin.getAusweis(p) == null) {
            p.sendMessage(Messages.PREFIX + "Du hast noch kein Ausweis. Mach dir einen mit /ausweis!");
            return true;
        }

        addActivePetter(p.getUniqueId());
        return true;
    }

    public void addActivePetter(UUID playerUUID) {
        activePetters.put(playerUUID, System.currentTimeMillis() + PET_MODE_DURATION);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (activePetters.containsKey(playerUUID) &&
                    System.currentTimeMillis() >= activePetters.get(playerUUID)) {
                    activePetters.remove(playerUUID);
                }
            }
        }.runTaskLater(plugin, 200L);
    }

    public boolean isActivePetter(UUID playerUUID) {
        if (!activePetters.containsKey(playerUUID)) return false;
        if (System.currentTimeMillis() > activePetters.get(playerUUID)) {
            activePetters.remove(playerUUID);
            return false;
        }
        return true;
    }

    public void removeActivePetter(UUID playerUUID) {
        activePetters.remove(playerUUID);
    }

    public boolean validateTarget(Player p, Entity target) {
        if (target.getWorld() != p.getWorld()) {
            p.sendMessage(Messages.NOT_IN_WORLD);
            return false;
        }
        if (target.getLocation().distance(p.getLocation()) > 5) {
            p.sendMessage(Messages.PLAYER_TOO_FAR_AWAY);
            return false;
        }
        return true;
    }

    public Location extractInteractLocation(PlayerInteractEntityEvent event, Entity target) {
        BoundingBox box = target.getBoundingBox();
        Location targetLoc = target.getLocation();
        
        double x = targetLoc.getX();
        double y = targetLoc.getY() + (box.getMaxY() - box.getMinY()) * 0.2;
        double z = targetLoc.getZ();
        
        return new Location(target.getWorld(), x, y, z);
    }

    public void handlePlayerPet(Player p, Player t, Location interactLocation) {
        p.swingMainHand();
        BoundingBox box = t.getBoundingBox();
        double y = t.getLocation().getY() + (box.getMaxY() - box.getMinY()) + 0.2;
        spawnHearts(t.getWorld(), t.getX(), y, t.getZ());

        String petterName = formatPlayerName(p);
        String targetName = formatPlayerName(t);

        MiniMsg.msg(p, Messages.MINI_PREFIX + "Du hast " + targetName + "<yellow> gestreichelt.</yellow>");
        MiniMsg.msg(t, Messages.MINI_PREFIX + "Du wurdest von " + petterName + "<yellow> gestreichelt.</yellow>");
    }

    public void handleAnimalPet(Player p, Animals animal, Location interactLocation) {
        p.swingMainHand();
        BoundingBox box = animal.getBoundingBox();
        double y = animal.getLocation().getY() + (box.getMaxY() - box.getMinY()) + 0.2;
        spawnHearts(animal.getWorld(), animal.getX(), y, animal.getZ());

        String animalType;
        try {
            if (animal.customName() != null) {
                animalType = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(animal.customName()));
            } else {
                animalType = "<lang:entity.minecraft." + animal.getType().toString().toLowerCase() + ">";
            }
        } catch (Exception e) {
            animalType = animal.getType().toString().toLowerCase().replace("_", " ");
        }

        String playerName = formatPlayerName(p);
        MiniMsg.msg(p, Messages.MINI_PREFIX + "Du hast <red>" + animalType + "</red><yellow> gestreichelt.</yellow>");

        for (Player nearby : animal.getWorld().getPlayers()) {
            if (!nearby.equals(p) && nearby.getLocation().distance(animal.getLocation()) <= 10) {
                MiniMsg.msg(nearby, Messages.MINI_PREFIX + playerName + "<yellow> hat <red>" + animalType + "</red> gestreichelt.</yellow>");
            }
        }
    }

    private String formatPlayerName(Player p) {
        var ausweis = plugin.getAusweis(p);
        if (ausweis != null) {
            return "<red>" + ausweis.getFirstName() + " " + ausweis.getLastName() + "</red>";
        }
        return "<red>" + p.getName() + "</red>";
    }

    private void spawnHearts(World world, double x, double y, double z) {
        for (int i = 0; i < 4; i++) {
            double offsetX = (Math.random() - 0.5) * 0.6;
            double offsetZ = (Math.random() - 0.5) * 0.6;
            world.spawnParticle(Particle.HEART, x + offsetX, y, z + offsetZ, 1);
        }
    }
}
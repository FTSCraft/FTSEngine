package de.ftscraft.ftsengine.commands.emotes;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CMDstreicheln implements CommandExecutor {

    private final Engine plugin;

    private static final Map<UUID, Long> activePetters = new HashMap<>();

    public static final long PET_MODE_DURATION = 10000;

    public CMDstreicheln(Engine plugin) {
        this.plugin = plugin;
        //noinspection DataFlowIssue
        plugin.getCommand("streicheln").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(cs instanceof Player p)) {
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

    private static void addActivePetter(UUID playerUUID) {
        activePetters.put(playerUUID, System.currentTimeMillis() + PET_MODE_DURATION);
    }

    private static boolean isActivePetter(UUID playerUUID) {
        if (!activePetters.containsKey(playerUUID)) return false;
        if (System.currentTimeMillis() > activePetters.get(playerUUID)) {
            activePetters.remove(playerUUID);
            return false;
        }
        return true;
    }

    private static void removeActivePetter(UUID playerUUID) {
        activePetters.remove(playerUUID);
    }

    private static boolean validateTarget(Player p, Entity target) {
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

    private static void handlePlayerPet(Player p, Player t) {
        p.swingMainHand();
        BoundingBox box = t.getBoundingBox();
        double y = t.getLocation().getY() + (box.getMaxY() - box.getMinY()) + 0.2;
        spawnHearts(t.getWorld(), t.getX(), y, t.getZ());

        String petterName = formatPlayerName(p);
        String targetName = formatPlayerName(t);

        MiniMsg.msg(p, Messages.MINI_PREFIX + "Du hast " + targetName + "<yellow> gestreichelt.</yellow>");
        MiniMsg.msg(t, Messages.MINI_PREFIX + "Du wurdest von " + petterName + "<yellow> gestreichelt.</yellow>");
    }

    private static void handleAnimalPet(Player p, Animals animal) {
        p.swingMainHand();
        BoundingBox box = animal.getBoundingBox();
        double y = animal.getLocation().getY() + (box.getMaxY() - box.getMinY()) + 0.2;
        spawnHearts(animal.getWorld(), animal.getX(), y, animal.getZ());

        String animalType = getAnimalName(animal);

        String playerName = formatPlayerName(p);
        MiniMsg.msg(p, Messages.MINI_PREFIX + "Du hast <red>" + animalType + "</red><yellow> gestreichelt.</yellow>");

        for (Player nearby : animal.getWorld().getPlayers()) {
            if (!nearby.equals(p) && nearby.getLocation().distance(animal.getLocation()) <= 10) {
                MiniMsg.msg(nearby, Messages.MINI_PREFIX + playerName + "<yellow> hat <red>" + animalType + "</red> gestreichelt.</yellow>");
            }
        }
    }

    private static @NotNull String getAnimalName(Animals animal) {
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
        return animalType;
    }

    private static String formatPlayerName(Player p) {
        var ausweis = Engine.getInstance().getAusweis(p);
        if (ausweis != null) {
            return "<red>" + ausweis.getFirstName() + " " + ausweis.getLastName() + "</red>";
        }
        return "<red>" + p.getName() + "</red>";
    }

    private static void spawnHearts(World world, double x, double y, double z) {
        for (int i = 0; i < 4; i++) {
            double offsetX = (Math.random() - 0.5) * 0.6;
            double offsetZ = (Math.random() - 0.5) * 0.6;
            world.spawnParticle(Particle.HEART, x + offsetX, y, z + offsetZ, 1);
        }
    }

    public static void petEntity(Player player, Entity target) {
        if (!isActivePetter(player.getUniqueId()))
            return;
        if (!validateTarget(player, target))
            return;

        if (target instanceof Player) {
            handlePlayerPet(player, (Player) target);
        } else if (target instanceof Animals) {
            handleAnimalPet(player, (Animals) target);
        } else
            return;

        removeActivePetter(player.getUniqueId());
    }

}
package de.ftscraft.ftsengine.commands.emotes;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CMDschlagen implements CommandExecutor {

    public CMDschlagen(Engine plugin) {
        Objects.requireNonNull(plugin.getCommand("schlagen"), "tried registering schlagen command but is null").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length == 1) {
            boolean isNear = false;
            Player t = null;
            for (Entity e : p.getNearbyEntities(5, 5, 5)) {
                if (e.getName().equalsIgnoreCase(args[0])) {
                    t = (Player) e;
                    Location loc = t.getLocation();
                    loc.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, loc.clone().add(0, 1.5D, 0), 4, 0.3, 0, 0.3);
                    t.sendMessage("§e" + p.getName() + " hat dich geschlagen");
                    isNear = true;
                }
            }

            if (!isNear) {
                p.sendMessage(Messages.PREFIX + "Der Spieler ist nicht in Schlagreichweite");
                return true;
            }

            for (Entity e : p.getNearbyEntities(5, 5, 5)) {
                if (e instanceof Player) {
                    e.sendMessage("§e" + p.getName() + " §7hat §e" + t.getName() + " §7geschlagen!");
                }
            }

            p.sendMessage("§eDu hast erfolgreich §c" + t.getName() + " §7geschlagen!");

        } else p.sendMessage(Messages.NO_PLAYER_GIVEN);

        return false;
    }
}

package de.ftscraft.ftsengine.commands.emotes;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDstreicheln implements CommandExecutor {

    public CMDstreicheln(Engine plugin) {
        plugin.getCommand("streicheln").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {

        if(!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length != 1) {
            p.sendMessage(Messages.NO_PLAYER_GIVEN);
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(Messages.IS_NOT_ON);
            return true;
        }

        if (t.getWorld() != p.getWorld()) {
            p.sendMessage(Messages.NOT_IN_WORLD);
            return true;
        }

        if (t.getLocation().distance(p.getLocation()) > 5) {
            p.sendMessage(Messages.PLAYER_TOO_FAR_AWAY);
            return true;
        }

        World w = p.getWorld();
        w.spawnParticle(Particle.HEART, t.getLocation(), 4);

        MiniMsg.msg(p, Messages.MINI_PREFIX + "Du hast <red>" + t.getName() + "</red> gestreichelt.");
        MiniMsg.msg(t, Messages.MINI_PREFIX + "Du wurdest von <red>" + p.getName() + "</red> gestreichelt.");

        return false;
    }
}

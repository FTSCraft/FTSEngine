package de.ftscraft.ftsengine.commands.emotes;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CMDspucken implements CommandExecutor {

    public CMDspucken(Engine plugin) {
        Objects.requireNonNull(plugin.getCommand("spucken"), "tried registering spucken command but is null").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length == 1) {

            String tString = args[0];

            Player t = Bukkit.getPlayer(tString);

            if (t == null) {
                p.sendMessage(Messages.PREFIX + "Dieser Spieler ist nicht online!");
                return true;
            }

            if (t.getLocation().distance(p.getLocation()) > 4) {
                p.sendMessage(Messages.PREFIX + "Dieser Spieler ist zu weit weg!");
                return true;
            }

            Location pLoc = p.getLocation();

            LlamaSpit llamaSpit = p.launchProjectile(LlamaSpit.class, pLoc.getDirection().multiply(0.5));
            llamaSpit.setShooter(p);

            MiniMsg.msg(p, "<yellow>Du hast den Spieler <red>%s</red> angespuckt".formatted(t.getName()));
            MiniMsg.msg(t, "<yellow>Der Spieler <red>%s</red> hat dich angespuckt!".formatted(p.getName()));

        } else p.sendMessage(Messages.NO_PLAYER_GIVEN);


        return false;
    }


}

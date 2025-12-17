package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.CountdownScheduler;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CMDcountdown implements CommandExecutor {

    private final Engine plugin;


    public CMDcountdown(Engine plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("countdown"), "tried registering countdown command but is null").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length == 1) {
            int secs;

            try {
                secs = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                cs.sendMessage(Messages.USAGE_NUMBER);
                return true;
            }

            if (secs > 30) {
                cs.sendMessage(Messages.PREFIX + "Maximal 30 Sekunden!");
                return true;
            }

            CountdownScheduler scheduler = new CountdownScheduler(plugin, secs, (Player) cs);
            scheduler.run();

            cs.sendMessage(Messages.PREFIX + "Der Countdown wurde gestartet!");

        }

        return false;
    }
}

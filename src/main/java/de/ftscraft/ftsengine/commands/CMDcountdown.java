package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.CountdownScheduler;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDcountdown implements CommandExecutor {

    private final Engine plugin;


    public CMDcountdown(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("countdown").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length == 1) {
            int secs;

            try {
                secs = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                cs.sendMessage(Messages.USAGE_NUMBER);
                return true;
            }

            if (secs > 30) {
                cs.sendMessage(Messages.PREFIX + "Maximal 30 Sekunden!");
                return true;
            }

            new CountdownScheduler(plugin, secs, (Player) cs);

            cs.sendMessage(Messages.PREFIX + "Der Countdown wurde gestartet!");

        }

        return false;
    }
}

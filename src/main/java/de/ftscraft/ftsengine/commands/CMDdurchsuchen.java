package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.feature.roleplay.durchsuchen.DurchsuchenManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CMDdurchsuchen implements CommandExecutor {

    private final Engine plugin;

    public CMDdurchsuchen(Engine plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("durchsuchen"), "tried registering durchsuchen command but is null").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        Player player = ((Player) cs).getPlayer();

        if (args.length != 1) {
            player.sendMessage(Messages.PREFIX + "§6Bitte verwende den Befehl so: §c/durchsuchen [Spielername]");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(Messages.IS_NOT_ON);
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(Messages.PREFIX + "§6Du kannst dich nicht selbst durchsuchen!");
            return true;
        }

        DurchsuchenManager.request(target, player);

        return true;
    }
}

package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.feature.roleplay.durchsuchen.DurchsuchenManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CMDsearchreact implements CommandExecutor {

    public CMDsearchreact(Engine plugin) {
        Objects.requireNonNull(plugin.getCommand("searchreact"), "tried registering searchreact command but is null").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        Player player = ((Player) cs).getPlayer();

        if (!DurchsuchenManager.isSearched(player)) {
            return true;
        }

        if (args.length < 1) {
            return true;
        }

        String reaction = args[0];
        switch (reaction) {
            case "accept":
                DurchsuchenManager.accept(player);
                break;
            case "deny":
                DurchsuchenManager.deny(player);
                break;
            case "hide":
                DurchsuchenManager.hide(player);
                break;
            default:
                return true;
        }
        return true;
    }
}

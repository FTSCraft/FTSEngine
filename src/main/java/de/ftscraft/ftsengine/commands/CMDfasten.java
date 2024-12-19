package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDfasten implements CommandExecutor {

    public CMDfasten(Engine plugin) {
        plugin.getCommand("fasten").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        if(!(cs instanceof Player p)) {
            cs.sendMessage(Messages.PREFIX);
            return true;
        }

        if(p.getFoodLevel() < 20) {
            p.sendMessage(Messages.PREFIX + "Du hast schon Hunger");
            return true;
        }

        ((Player) cs).setFoodLevel(10);
        cs.sendMessage(Messages.PREFIX + "Du hast erfolgreich gefastet!");

        return false;
    }
}

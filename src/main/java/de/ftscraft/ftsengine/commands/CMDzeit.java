package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.time.TimeManager;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDzeit implements CommandExecutor {

    public CMDzeit(Engine plugin) {
        plugin.getCommand("zeit").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(TimeManager.getFormattedTime());
            return true;
        }
        MiniMsg.msg(p, Messages.MINI_PREFIX + "Es ist folgendes Datum auf Eldoria: \n  <red>" + TimeManager.getFormattedTime() + " Uhr<gray>.");

        return false;
    }
}

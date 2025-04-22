package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.feature.time.TimeManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class CMDzeit implements CommandExecutor {

    public CMDzeit(Engine plugin) {
        //noinspection DataFlowIssue
        plugin.getCommand("zeit").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(TimeManager.getFormattedTime());
            return true;
        }
        int year = TimeManager.getCalendar().get(Calendar.YEAR);
        MiniMsg.msg(p, Messages.MINI_PREFIX + "Datum und Uhrzeit: <red>" + TimeManager.getFormattedTime() + "</red> Uhr \n" +
                "Menschenjahr: <red>" + getHumanYear(year) + "</red>\n" +
                "Eldoriajahr: <red>" + year + "</red>");

        return false;
    }

    private int getHumanYear(int eldoriaYear) {

        int elapsedEldoriaYears = eldoriaYear - 24_000;
        int elapsedHumanYears = elapsedEldoriaYears / 4;
        return elapsedHumanYears + 1100;

    }

}

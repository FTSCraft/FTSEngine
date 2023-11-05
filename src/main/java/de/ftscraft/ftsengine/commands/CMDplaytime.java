package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class CMDplaytime implements CommandExecutor {


    public CMDplaytime(Engine plugin) {
        plugin.getCommand("spielzeit").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (cs instanceof Player) {
            Player p = (Player) cs;

            int second = p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
            int minute = second / 60;
            int hour = minute / 60;

            p.sendMessage(Messages.PREFIX + "Du spielst schon §c" + hour + " Stunden§b. \n§7Ab §c50 Stunden §bsolltest du eine Charaktervorstellung im Forum schreiben \n§7Du willst wissen wie das geht? Schau hier: §chttps://forum.ftscraft.de/t/tutorial-eine-charaktervorstellung-schreiben/1586 ");

        } else {
            cs.sendMessage("Du musst ein Spieler sein");
        }
        return false;
    }
}

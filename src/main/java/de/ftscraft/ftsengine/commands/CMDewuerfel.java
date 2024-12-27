package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;

public class CMDewuerfel implements CommandExecutor {

    public CMDewuerfel(Engine plugin) {

        plugin.getCommand("ewürfel").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(cs instanceof Player p)) {
            return true;
        }

        if (args.length == 1) {
            String arg = args[0];
            String[] num = arg.split("w");
            if (num.length != 2) {
                p.sendMessage(help());
                return true;
            }
            int amount = Integer.parseInt(num[0]);
            int size = Integer.parseInt(num[1]);

            if (amount > 20) {
                System.out.println(Messages.PREFIX + amount + " sind zu viele Würfel. Maximal 20");
                return true;
            }
            if (size > 500) {
                System.out.println(Messages.PREFIX + size + " ist ein zu großer Wert. Maximal w500 Würfel sind erlaubt");
                return true;
            }

            int[] dices = new int[amount];
            int total = 0;

            for (int i = 0; i < amount; i++) {
                Random random = new Random();
                int n = random.nextInt(size) + 1;
                dices[i] = n;
                total += n;
            }

            Var.sendMessageToPlayersNear(p.getLocation(), 15, Messages.PREFIX + "Der Spieler " + p.getName() + " hat folgendes gewürfelt: " + ChatColor.RED + Arrays.toString(dices) + ChatColor.GRAY + " = " + total + " (" + args[0] + ")");


        } else {
            p.sendMessage(help());
        }


        return false;
    }

    private String help() {
        return Messages.PREFIX + "Benutz den Befehl so: /würfel [Anzahl]w[Augenzahl] \n" +
                "Beispiel: /würfel 1w6 für einen sechsseitigen Würfel oder 2w12 für zwei Zwölfseitige";
    }

}

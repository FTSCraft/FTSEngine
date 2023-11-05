package de.ftscraft.ftsengine.commands.emotes;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDwinken implements CommandExecutor {
    private final Engine plugin;

    public CMDwinken(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("winken").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player p = (Player) commandSender;

        if (plugin.getAusweis(p) == null) {
            p.sendMessage(Messages.PREFIX + "Du hast noch kein Ausweis. Mach dir einen mit /ausweis!");
            return true;
        }

        if (args.length == 1) {

            String tName = args[0];
            Player target = Bukkit.getPlayer(tName);

            if (target != null) {
                if (plugin.getAusweis(target) != null) {
                    if (target.getLocation().distance(p.getLocation()) < 40) {
                        String message = "§c" + plugin.getAusweis(p).getFirstName() + " " + plugin.getAusweis(p).getLastName() + "§e winkt §c" + plugin.getAusweis(target).getFirstName() + " " + plugin.getAusweis(target).getLastName() + "§e zu.";
                        p.sendMessage(message);
                        for (Entity n : p.getNearbyEntities(45, 45, 45)) {
                            if (n instanceof Player) {
                                n.sendMessage(message);
                            }
                        }
                    } else p.sendMessage(Messages.PLAYER_TOO_FAR_AWAY);
                } else p.sendMessage(Messages.TARGET_NO_AUSWEIS);
            } else p.sendMessage(Messages.PREFIX + "Der Spieler ist derzeit nicht online!");
        } else p.sendMessage(Messages.NO_PLAYER_GIVEN);
        return false;
    }
}

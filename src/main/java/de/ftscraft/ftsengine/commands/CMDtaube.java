package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.CountdownScheduler;
import de.ftscraft.ftsengine.utils.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

public class CMDtaube implements CommandExecutor {

    private final Engine plugin;

    public CMDtaube(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("taube").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length > 1) {
            Player p = (Player) cs;

            // Handle brief command
            if (args[0].equalsIgnoreCase("brief")) {
                if (args.length < 3) {
                    p.sendMessage(Messages.PREFIX + "§cUngültige Verwendung des Befehls!");
                    return true;
                }

                String senderName = args[1];
                StringBuilder message = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    message.append(" ").append(args[i]);
                }
                message = new StringBuilder(message.toString().trim());

                // Check if player has already claimed this message
                if (CountdownScheduler.hasClaimedMessage(p, message.toString())) {
                    p.sendMessage(Messages.PREFIX + "§cDu hast diesen Brief bereits erhalten!");
                    return true;
                }

                // Check if player has inventory space
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage(Messages.PREFIX + "§cDu brauchst mindestens einen freien Inventarplatz für den Brief!");
                    return true;
                }

                ItemStack bookItemStack = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta bookMeta = (BookMeta) bookItemStack.getItemMeta();
                bookMeta.displayName(Component.text("Brief von " + senderName).color(NamedTextColor.YELLOW));
                bookMeta.addPages(Component.text(message.toString()));
                bookItemStack.setItemMeta(bookMeta);

                p.getInventory().addItem(bookItemStack);
                p.sendMessage(Messages.PREFIX + "§7Du hast den Brief von der Taube genommen!");

                CountdownScheduler.markMessageAsClaimed(p, message.toString());
                return true;
            }

            String[] player = args[0].split(",");

            Location pl = p.getLocation();

            //If you want to send a taube to only one or more specific player*s

            for (String s : player) {

                Player t = Bukkit.getPlayer(s);
                if (t == null) {
                    p.sendMessage(Messages.PREFIX + "§7Der Spieler §c" + s + " §7wurde nicht gefunden");
                } else {
                    Location tl = t.getLocation();
                    int seconds;
                    if (pl.getWorld() != tl.getWorld()) {
                        seconds = 120;
                    } else {
                        double distance = t.getLocation().distance(p.getLocation());
                        if (distance < 350) {
                            seconds = 5;
                        } else {
                            seconds = (int) distance / 70;
                        }
                        if (seconds > 120)
                            seconds = 120;
                    }

                    StringBuilder msg = new StringBuilder();

                    for (int i2 = 1; i2 < args.length; i2++) {
                        msg.append(" ").append(args[i2]);
                    }

                    p.sendMessage(Messages.PREFIX + "Eine Taube fliegt zu §c" + t.getName() + "§7.");

                    p.playSound(p.getLocation(), Sound.ENTITY_BAT_LOOP, 3, -20);

                    new CountdownScheduler(plugin, seconds, p, t, msg.toString());
                }

            }

            return true;

        }

        return false;
    }
}

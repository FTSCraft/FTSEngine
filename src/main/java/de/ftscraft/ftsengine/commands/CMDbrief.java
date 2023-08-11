package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class CMDbrief implements CommandExecutor {

    private final Engine plugin;

    public CMDbrief(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("brief").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        Player p = (Player) cs;

        if (args.length == 2) {

            if (args[0].equalsIgnoreCase("senden")) {

                String tName = args[1];
                UUID tUUID = Bukkit.getOfflinePlayer(tName).getUniqueId();

                if (!plugin.briefkasten.containsKey(tUUID)) {

                    p.sendMessage("§cDieser Spieler besitzt keinen Briefkasten!");

                    return true;

                }

                Briefkasten briefkasten = plugin.briefkasten.get(tUUID);

                ItemStack item = p.getInventory().getItemInMainHand();

                if (item.getType() == Material.FILLED_MAP || item.getType() == Material.WRITTEN_BOOK || item.getType() == Material.WRITABLE_BOOK) {

                    boolean sucess = briefkasten.putItemIntoChest(item);

                    if (sucess) {

                        p.sendMessage("§cDein Brief wurde erfolgreich versendet!");

                        item.setAmount(0);

                    } else p.sendMessage("§cDa hat was nicht geklappt! (wahrscheinlich fehlt platz im kasten)");

                    return true;

                } else p.sendMessage("§cDu musst einen Brief oder ein Buch senden!");

                return true;

            }

        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("removekasten")) {

                if(!plugin.briefkasten.containsKey(p.getUniqueId())) {
                    p.sendMessage("§cDu hast keinen Briefkasten!");
                    return true;
                }

                plugin.briefkasten.remove(p.getUniqueId());

                File file = new File(plugin.getDataFolder() + "//briefkasten//" + p.getUniqueId() + ".yml");

                file.getName();

                file.delete();

                p.sendMessage("§cDu hast deinen Briefkasten erfolgreich entfernt!");

                return true;

            }
        }

        if (args.length >= 1) {

            StringBuilder msg = new StringBuilder();
            String name = p.getName();
            for (String arg : args) {
                msg.append(" ").append(arg);
            }
            msg = new StringBuilder(msg.toString().replace("ä", "ae"));
            msg = new StringBuilder(msg.toString().replaceAll("ü", "ue"));
            msg = new StringBuilder(msg.toString().replaceAll("ß", "ss"));
            msg = new StringBuilder(msg.toString().replaceAll("ö", "oe"));
            msg = new StringBuilder(msg.toString().replaceAll("[^a-zA-Z0-9]", ""));
            if (msg.toString().startsWith(" anonym")) {
                if (plugin.getEcon().has(p, 2)) {
                    plugin.getEcon().withdrawPlayer(p, 2);
                    p.sendMessage("§cDieser Brief ist nun Anonym!");
                    msg = new StringBuilder(msg.toString().replaceFirst(" anonym", " "));
                    name = "XXXX";
                } else {
                    p.sendMessage("Dafür hast du kein Geld!");
                    return true;
                }
            }


            Brief brief = new Brief(plugin, name, msg.toString(), p.getWorld().getName());
            if(!brief.isError())
                p.getInventory().addItem(brief.getMap(p.getWorld()));

            return false;
        }


        return true;
    }

}


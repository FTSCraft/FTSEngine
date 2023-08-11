package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Gender;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMDausweis implements CommandExecutor, TabCompleter {

    private final Engine plugin;
    private final ArrayList<String> arguments;

    public CMDausweis(Engine plugin) {
        this.plugin = plugin;
        Messages msgs = plugin.msgs;
        this.arguments = new ArrayList<>(Arrays.asList("name", "geschlecht", "rasse", "aussehen", "link", "anschauen"));
        plugin.getCommand("ausweis").setExecutor(this);
    }

    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
        }

        Player p = (Player) cs;
        if (args.length > 0) {
            String sub = args[0];

            switch (sub) {
                case "name":
                    if (args.length == 3) {
                        String fName = args[1];
                        String lName = args[2];

                        if (plugin.hasAusweis(p)) {
                            plugin.getAusweis(p).setFirstName(fName);
                            plugin.getAusweis(p).setLastName(lName);
                        } else {
                            Ausweis a = new Ausweis(plugin, p);
                            a.setFirstName(fName);
                            a.setLastName(lName);
                            plugin.addAusweis(a);
                        }
                        p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Name").replace("%v", fName + " " + lName));
                    } else
                        p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis name [Vorname] [Nachname]");
                    break;
                case "link":
                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {
                        String link = args[1];
                        if (!link.startsWith("https://forum.ftscraft.de/")) {
                            p.sendMessage("§cDer Link muss mit unserer URL des Forums anfangen! (https://forum.ftscraft.de/)");
                            return true;
                        }
                        plugin.getAusweis(p).setForumLink(link);
                        p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Charaktervorstellung").replace("%v", link));
                    } else
                        p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so: §c/ausweis link [Forumlink deiner Charvorstellung]");
                    break;
                case "geschlecht":
                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {
                        Gender g = null;
                        if (args[1].equalsIgnoreCase("m") || args[1].equalsIgnoreCase("f") || args[1].equalsIgnoreCase("d")) {
                            if (args[1].equalsIgnoreCase("m"))
                                g = Gender.MALE;
                            if (args[1].equalsIgnoreCase("f")) {
                                g = Gender.FEMALE;
                            }
                            if (args[1].equalsIgnoreCase("d")) {
                                g = Gender.DIVERS;
                            }
                            if (g == null) {
                                p.sendMessage(Messages.PREFIX + "Fehler!");
                            }
                            plugin.getAusweis(p).setGender(g);
                            p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Geschlecht").replace("%v", (g == Gender.MALE ? "Mann" : g == Gender.FEMALE ? "Frau" : "Divers")));
                        } else
                            p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis geschlecht [\"m\"/\"f\"/\"d\"]");
                    } else
                        p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis geschlecht [\"m\"/\"f\"]");
                    break;
                case "rasse":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {

                        String race = args[1];
                        plugin.getAusweis(p).setRace(race);
                        p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Rasse").replace("%v", race));
                    } else
                        p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis rasse [Rasse]");
                    break;
                case "aussehen":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }

                    if (args.length > 4) {

                        StringBuilder desc = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            desc.append(" ").append(args[i]);
                        }

                        desc = new StringBuilder(desc.toString().replaceFirst(" ", ""));

                        plugin.getAusweis(p).setDesc(desc.toString());

                        p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Aussehen").replace("%v", desc.toString()));

                    } else
                        p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis aussehen [Aussehen (mind. 4 Wörter)]");

                    break;
                /*case "religion":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {
                        plugin.getAusweis(p).setReligion(args[1]);
                        p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Religion").replace("%v", args[1]));
                    } else
                        p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis religion [Religion]");

                    break;*/
               /* case "spitzname":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {
                        plugin.getAusweis(p).setSpitzname(args[1]);
                        p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Spitzname").replace("%v", args[1]));
                    } else
                        p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis spitzname [Spitzname]");
                    break;*/
                /*case "kopieren":
                    ItemStack item;
                    String name;
                    if (args.length == 1) {
                        if (plugin.hasAusweis(p)) {
                            name = "Dir";
                            item = plugin.getAusweis(p).getAsItem();
                        } else {
                            p.sendMessage(Messages.PREFIX + "Mach dir erstmal einen Ausweis!");
                            return true;
                        }
                    } else if (args.length == 2 && p.hasPermission("ftsengine.helfer")) {
                        name = args[1];
                        if (plugin.hasAusweis(name)) {
                            item = plugin.getAusweis(name).getAsItem();
                        } else {
                            p.sendMessage(Messages.PREFIX + "Dieser Spieler hat keinen Ausweis");
                            return true;
                        }
                    } else {
                        p.sendMessage(Messages.PREFIX + "Dafür hast du keine Rechte");
                        return true;
                    }
                    p.getInventory().addItem(item);
                    p.sendMessage(Messages.SUCC_COPY_AUSWEIS.replace("%s", name));
                    break;*/
                case "anschauen":
                    if(p.hasPermission("ftsengine.ausweis.anschauen")) {
                        if (args.length == 2) {
                            if (plugin.hasAusweis(args[1])) {
                                new Var(plugin).sendAusweisMsg(p, plugin.getAusweis(args[1]));
                            } else p.sendMessage(Messages.TARGET_NO_AUSWEIS);
                        } else p.sendMessage("§cBitte gebe einen Namen an");
                    } else p.sendMessage("§cDafür hast du keine Rechte");
                    break;
                /*case "list":
                    for (Ausweis a : plugin.ausweis.values()) {
                        p.sendMessage("§e" + Bukkit.getOfflinePlayer(UUID.fromString(a.getUUID())).getName());
                    }*/
                case "admin":
                    break;
                default:
                    plugin.getVar().sendHelpMsg(p);
                    break;
            }

            if (sub.equalsIgnoreCase("admin")) {

                if (args.length >= 3) {

                    if (p.hasPermission("ftsengine.admin")) {


                    } else p.sendMessage("§cDafür hast du keine Rechte LOL");
                }

            }

        } else plugin.getVar().sendHelpMsg(p);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        ArrayList<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (String argument : arguments) {
                if (argument.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(argument);
            }
            return result;
        }

        return result;

    }
}

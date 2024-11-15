package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import org.bukkit.attribute.Attribute;
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
import java.util.Objects;

public class CMDausweis implements CommandExecutor, TabCompleter {

    private final Engine plugin;
    private final ArrayList<String> arguments;

    public CMDausweis(Engine plugin) {
        this.plugin = plugin;
        this.arguments = new ArrayList<>(Arrays.asList("name", "geschlecht", "rasse", "aussehen", "größe", "link", "anschauen"));
        plugin.getCommand("ausweis").setExecutor(this);
    }

    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendPlainMessage(Messages.ONLY_PLAYER);
            return true;
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
                        p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Name").replace("%v", fName + " " + lName));
                    } else
                        p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis name [Vorname] [Nachname]");
                    break;
                case "link":
                    if (!plugin.hasAusweis(p)) {
                        p.sendPlainMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {
                        String link = args[1];
                        if (!link.startsWith("https://forum.ftscraft.de/")) {
                            p.sendPlainMessage("§cDer Link muss mit unserer URL des Forums anfangen! (https://forum.ftscraft.de/)");
                            return true;
                        }
                        plugin.getAusweis(p).setForumLink(link);
                        p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Charaktervorstellung").replace("%v", link));
                    } else
                        p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so: §c/ausweis link [Forumlink deiner Charvorstellung]");
                    break;
                case "geschlecht":
                    if (!plugin.hasAusweis(p)) {
                        p.sendPlainMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {
                        Ausweis.Gender g = null;
                        if (args[1].equalsIgnoreCase("m") || args[1].equalsIgnoreCase("f") || args[1].equalsIgnoreCase("d")) {
                            if (args[1].equalsIgnoreCase("m"))
                                g = Ausweis.Gender.MALE;
                            else if (args[1].equalsIgnoreCase("f"))
                                g = Ausweis.Gender.FEMALE;

                            if (g == null) {
                                p.sendPlainMessage(Messages.PREFIX + "Fehler!");
                            }
                            plugin.getAusweis(p).setGender(g);
                            p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Geschlecht").replace("%v", (g == Ausweis.Gender.MALE ? "Mann" : g == Ausweis.Gender.FEMALE ? "Frau" : "Divers")));
                        } else
                            p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis geschlecht [\"m\"/\"f\"]");
                    } else
                        p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis geschlecht [\"m\"/\"f\"]");
                    break;
                case "rasse":

                    if (!plugin.hasAusweis(p)) {
                        p.sendPlainMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length != 2) {
                        p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis rasse [Ork/Zwerg/Mensch/Elf]");
                        return true;
                    }

                    String race = args[1];
                    race = race.substring(0, 1).toUpperCase() + race.substring(1).toLowerCase();

                    switch (race) {
                        case "Ork":
                        case "Zwerg":
                        case "Mensch":
                        case "Elf":
                            plugin.getAusweis(p).setRace(race);
                            p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Rasse").replace("%v", race));
                            break;
                        default:
                            p.sendMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis rasse [Ork/Zwerg/Mensch/Elf].");
                    }

                    break;
                case "größe":

                    if (!plugin.hasAusweis(p)) {
                        p.sendPlainMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }

                    if (args.length != 2) {
                        p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis größe [Größe]");
                        return true;
                    }

                    int height;
                    try {
                        height = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                        p.sendMessage(Messages.PREFIX + "Bitte gebe eine natürliche Zahl für deine Größe (in cm) an.");
                        return true;
                    }

                    if (height < 140 || height > 300) {
                        p.sendMessage(Messages.PREFIX + "Deine Größe müss zwischen 140cm und 300cm liegen.");
                        return true;
                    }

                    plugin.getAusweis(p).setHeight(height);
                    p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Größe").replace("%v", String.valueOf(height)));
                    Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_SCALE)).setBaseValue(height / 200d);

                    break;
                case "aussehen":

                    if (!plugin.hasAusweis(p)) {
                        p.sendPlainMessage(Messages.NEED_AUSWEIS);
                        return true;
                    }

                    if (args.length > 4) {

                        StringBuilder desc = new StringBuilder();
                        for (int i = 1; i < args.length; i++) {
                            desc.append(" ").append(args[i]);
                        }

                        desc = new StringBuilder(desc.toString().replaceFirst(" ", ""));

                        plugin.getAusweis(p).setDesc(desc.toString());

                        p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Aussehen").replace("%v", desc.toString()));

                    } else
                        p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis aussehen [Aussehen (mind. 4 Wörter)]");

                    break;
                case "anschauen":
                    if (p.hasPermission("ftsengine.ausweis.anschauen")) {
                        if (args.length == 2) {
                            if (plugin.hasAusweis(args[1])) {
                                Var.sendAusweisMsg(p, plugin.getAusweis(args[1]));
                            } else p.sendPlainMessage(Messages.TARGET_NO_AUSWEIS);
                        } else {
                            Var.sendAusweisMsg(p, plugin.getAusweis(p));
                        }
                    } else p.sendPlainMessage("§cDafür hast du keine Rechte");
                    break;
                default:
                    sendHelpMsg(p);
                    break;
            }

        } else sendHelpMsg(p);
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

    public static void sendHelpMsg(Player p)
    {
        p.sendMessage("§c----- §e/ausweis §c-----");
        p.sendMessage("§e/ausweis name [Vorname] [Nachname] §bÄndert deinen Namen und erstellt beim 1. Mal einen Ausweis - §cMit Unterstrichen könnt ihr Leerzeichen im Namen haben");
        p.sendMessage("§e/ausweis geschlecht [m/f] §bSetzt die Ansprache (m - Männliche | f - Weibliche)");
        p.sendMessage("§e/ausweis rasse [Ork/Zwerg/Mensch/Elf] §bSetzt deine Rasse");
        p.sendMessage("§e/ausweis aussehen [Beschr.] §bSetzt dein Aussehen (Mind. 4 Wörter)");
        p.sendMessage("§e/ausweis größe [Größe in cm] §bSetzt deine Größe");
        p.sendMessage("§e/ausweis link [Link] §bSetzt den Link zu deiner Charvorstellung im Forum");
        p.sendMessage("§e/ausweis anschauen [Spieler] §bSchau den Ausweis eines Spielers an");
    }

}

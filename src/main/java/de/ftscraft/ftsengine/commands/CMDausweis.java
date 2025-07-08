package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import de.ftscraft.ftsutils.misc.MiniMsg;
import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

    private static final double HEIGHT_COOLDOWN = 1000 * 60 * 60 * 24 * 7;

    public CMDausweis(Engine plugin) {
        this.plugin = plugin;
        this.arguments = new ArrayList<>(Arrays.asList("name", "geschlecht", "rasse", "aussehen", "größe",
                "link", "anschauen", "deckname"));
        plugin.getCommand("ausweis").setExecutor(this);
    }

    private static final int MAX_HEIGHT = 300, MIN_HEIGHT = 90;

    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player p)) {
            cs.sendPlainMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length > 0) {
            String sub = args[0];
            switch (sub) {
                case "name":
                    handleName(args, p);
                    break;
                case "link":
                    if (handleLink(args, p)) return true;
                    break;
                case "geschlecht":
                    if (handleSex(args, p)) return true;
                    break;
                case "rasse":
                    if (handleRace(args, p)) return true;
                    break;
                case "größe":
                    if (handleSize(args, p)) return true;
                    break;
                case "aussehen":
                    if (handleLooks(args, p)) return true;
                    break;
                case "anschauen":
                    handleInspect(args, p);
                    break;
                case "deckname":
                    if (handleUndercoverName(args, p)) return true;
                    break;
                default:
                    sendHelpMsg(p);
                    break;
            }

        } else sendHelpMsg(p);
        return false;
    }

    private boolean handleUndercoverName(String[] args, Player p) {
        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return true;
        }
        if (args.length > 1) {
            String deckname = args[1].replace("_", " ");
            plugin.getAusweis(p).setSpitzname(deckname);
            p.sendMessage(Messages.PREFIX + "Du hast deinen Decknamen als " + deckname + " gesetzt!");
        } else {
            p.sendPlainMessage(Messages.PREFIX +
                    "Bitte benutze den Befehl so:" + " §c/ausweis deckname [Deckname]");
        }
        return false;
    }

    private boolean handleRace(String[] args, Player p) {
        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return true;
        }
        if (args.length != 2) {
            p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" +
                    " §c/ausweis rasse [Ork/Zwerg/Mensch/Elf]");
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
        return false;
    }

    private boolean handleSex(String[] args, Player p) {
        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return true;
        }
        if (args.length == 2) {
            Ausweis.Gender g = null;
            if (args[1].equalsIgnoreCase("m") ||
                    args[1].equalsIgnoreCase("f")) {
                if (args[1].equalsIgnoreCase("m"))
                    g = Ausweis.Gender.MALE;
                else if (args[1].equalsIgnoreCase("f"))
                    g = Ausweis.Gender.FEMALE;

                if (g == null) {
                    p.sendPlainMessage(Messages.PREFIX + "Fehler!");
                }
                plugin.getAusweis(p).setGender(g);
                p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Geschlecht")
                        .replace("%v", (g == Ausweis.Gender.MALE ? "Mann" : "Frau")));
            } else
                p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" +
                        " §c/ausweis geschlecht [\"m\"/\"f\"]");
        } else
            p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" +
                    " §c/ausweis geschlecht [\"m\"/\"f\"]");
        return false;
    }

    private boolean handleLink(String[] args, Player p) {
        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return true;
        }
        if (args.length == 2) {
            String link = args[1];
            if (!link.startsWith("https://forum.ftscraft.de/")) {
                p.sendPlainMessage("§cDer Link muss mit unserer URL des Forums anfangen! " +
                        "(https://forum.ftscraft.de/)");
                return true;
            }
            plugin.getAusweis(p).setForumLink(link);
            p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS
                    .replace("%s", "Charaktervorstellung").replace("%v", link));
        } else
            p.sendPlainMessage(Messages.PREFIX +
                    "Bitte benutze den Befehl so: §c/ausweis link [Forumlink deiner Charvorstellung]");
        return false;
    }

    private void handleName(String[] args, Player p) {
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
            p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Name")
                    .replace("%v", fName + " " + lName));
        } else
            p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" +
                    " §c/ausweis name [Vorname] [Nachname]");
    }

    private void handleInspect(String[] args, Player p) {
        if (!p.hasPermission("ftsengine.ausweis.anschauen")) {
            p.sendPlainMessage("§cDafür hast du keine Rechte");
            return;
        }

        if (args.length == 2) {
            Ausweis a;
            if ((a = plugin.getAusweis(UUIDFetcher.getUUID(args[1]))) != null) {
                Var.sendAusweisMsg(p, a);
            } else p.sendPlainMessage(Messages.TARGET_NO_AUSWEIS);
        } else {
            Var.sendAusweisMsg(p, plugin.getAusweis(p));
        }

    }

    private boolean handleLooks(String[] args, Player p) {
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
        return false;
    }

    private boolean handleSize(String[] args, Player p) {
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

        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            p.sendMessage(Messages.PREFIX +
                    "Deine Größe müss zwischen %d cm und %d cm liegen."
                            .formatted(MIN_HEIGHT, MAX_HEIGHT));
            return true;
        }

        Ausweis ausweis = plugin.getAusweis(p);

        if (ausweis.getLastHeightChange() + HEIGHT_COOLDOWN >= System.currentTimeMillis()) {
            if (!p.hasPermission("ftssurvival.bypass") && !p.hasPermission("ftsengine.mod")) {
                MiniMsg.msg(p, Messages.MINI_PREFIX + "Du darfst deine Größe alle 7 Tage ändern");
                return true;
            }
        }

        ausweis.setHeight(height);
        p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Größe").replace("%v", String.valueOf(height)));
        AttributeInstance scaleAttr = p.getAttribute(Attribute.SCALE);

        if (scaleAttr == null) {
            MiniMsg.msg(p, "Da ist was schiefgelaufen.");
            return true;
        }

        scaleAttr.setBaseValue(height / 200d);
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

        if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            String currentInput = args[1].toLowerCase();

            switch (subCommand) {
                case "geschlecht":
                    ArrayList<String> genderOptions = new ArrayList<>(Arrays.asList("m", "f"));
                    for (String option : genderOptions) {
                        if (option.startsWith(currentInput))
                            result.add(option);
                    }
                    break;
                case "rasse":
                    ArrayList<String> raceOptions = new ArrayList<>(Arrays.asList("Ork", "Zwerg", "Mensch", "Elf"));
                    for (String option : raceOptions) {
                        if (option.toLowerCase().startsWith(currentInput))
                            result.add(option);
                    }
                    break;
            }
        }

        return result;

    }

    public static void sendHelpMsg(Player p) {
        p.sendMessage(Messages.PREFIX + "----- §c/ausweis §7-----");
        p.sendMessage("§7/ausweis name [Vorname] [Nachname] §cÄndert deinen Namen und erstellt beim 1. Mal einen Ausweis - Mit Unterstrichen könnt ihr Leerzeichen im Namen haben");
        p.sendMessage("§7/ausweis geschlecht [m/f] §cSetzt die Ansprache (m - Männliche | f - Weibliche)");
        p.sendMessage("§7/ausweis rasse [Ork/Zwerg/Mensch/Elf] §cSetzt deine Rasse");
        p.sendMessage("§7/ausweis aussehen [Beschr.] §cSetzt dein Aussehen (Mind. 4 Wörter)");
        p.sendMessage("§7/ausweis größe [Größe in cm] §cSetzt deine Größe");
        p.sendMessage("§7/ausweis deckname [Deckname] §cSetzt deinen Decknamen");
        p.sendMessage("§7/ausweis link [Link] §cSetzt den Link zu deiner Charvorstellung im Forum");
        p.sendMessage("§7/ausweis anschauen [Spieler] §cSchau den Ausweis eines Spielers an");
    }

}

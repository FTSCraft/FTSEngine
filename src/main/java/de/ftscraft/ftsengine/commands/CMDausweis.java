package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Gender;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CMDausweis implements CommandExecutor {

    private Engine plugin;
    private Messages msgs;

    public CMDausweis(Engine plugin) {
        this.plugin = plugin;
        this.msgs = plugin.msgs;
        plugin.getCommand("ausweis").setExecutor(this);
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(msgs.ONLY_PLAYER);
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
                        p.sendMessage(msgs.SUCC_CMD_AUSWEIS.replace("%s", "Name").replace("%v", fName + " " + lName));
                        p.sendMessage(msgs.PREFIX + "ACHTUNG! Wenn du deinen Gerburtstag mit /ausweis alter [Geburtstag] eingibst geb das so ein: §5DD-MM §7!");
                        p.sendMessage("§7Also wenn du am §517.2 §7Geburtstag hast, geb §5'17-02' §7ein");
                        p.sendMessage("§7Achso, falls du zum Geburtstag einen Rang möchtest, geb noch unbedingt dein Geschlecht ein.");
                    } else
                        p.sendMessage(msgs.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis name [Vorname] [Nachname]");
                    break;
                case "alter":
                    if (args.length == 2) {
                        if (!plugin.hasAusweis(p)) {
                            p.sendMessage(msgs.NEED_AUSWEIS);
                            return true;
                        }
                        if (plugin.getAusweis(p).birthdaySetuped()) {
                            p.sendMessage(msgs.ALREADY_SETUP_BIRTH);
                            return true;
                        }
                        String dob = args[1];
                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM");
                        Date date;
                        try {
                            date = sdf1.parse(dob);
                            date.setTime(date.getTime() + 1);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            plugin.getAusweis(p).setBirthday(cal);
                            p.sendMessage(msgs.PREFIX + "Okay, du hast am " + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + " Geburtstag!");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case "geschlecht":
                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(msgs.NEED_AUSWEIS);
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
                                p.sendMessage(msgs.PREFIX + "Fehler!");
                            }
                            plugin.getAusweis(p).setGender(g);
                            p.sendMessage(msgs.SUCC_CMD_AUSWEIS.replace("%s", "Geschlecht").replace("%v", (g == Gender.MALE ? "Mann" : g == Gender.FEMALE ? "Frau" : "Divers")));
                        } else
                            p.sendMessage(msgs.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis geschlecht [\"m\"/\"f\"/\"d\"]");
                    } else
                        p.sendMessage(msgs.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis geschlecht [\"m\"/\"f\"]");
                    break;
                case "rasse":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(msgs.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {

                        String race = args[1];
                        plugin.getAusweis(p).setRace(race);
                        p.sendMessage(msgs.SUCC_CMD_AUSWEIS.replace("%s", "Rasse").replace("%v", race));
                    } else p.sendMessage(msgs.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis rasse [Rasse]");
                    break;
                case "nation":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(msgs.NEED_AUSWEIS);
                        return true;
                    }

                    if (args.length == 2) {
                        String nation = args[1];
                        plugin.getAusweis(p).setNation(nation);
                        p.sendMessage(msgs.SUCC_CMD_AUSWEIS.replace("%s", "Nation").replace("%v", nation));
                    } else p.sendMessage(msgs.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis nation [Nation]");

                    break;
                case "beschreibung":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(msgs.NEED_AUSWEIS);
                        return true;
                    }

                    if (args.length > 4) {

                        String desc = "";
                        for (int i = 1; i < args.length; i++) {
                            desc += " " + args[i];
                        }

                        desc = desc.replaceFirst(" ", "");

                        plugin.getAusweis(p).setDesc(desc);

                        p.sendMessage(msgs.SUCC_CMD_AUSWEIS.replace("%s", "Beschreibung").replace("%v", desc));

                    } else
                        p.sendMessage(msgs.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis beschreibung [Beschreibung (mind. 4 Wörter)]");

                    break;
                case "religion":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(msgs.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {
                        plugin.getAusweis(p).setReligion(args[1]);
                        p.sendMessage(msgs.SUCC_CMD_AUSWEIS.replace("%s", "Religion").replace("%v", args[1]));
                    } else
                        p.sendMessage(msgs.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis religion [Religion]");

                    break;
                case "spitzname":

                    if (!plugin.hasAusweis(p)) {
                        p.sendMessage(msgs.NEED_AUSWEIS);
                        return true;
                    }
                    if (args.length == 2) {
                        plugin.getAusweis(p).setSpitzname(args[1]);
                        p.sendMessage(msgs.SUCC_CMD_AUSWEIS.replace("%s", "Spitzname").replace("%v", args[1]));
                    } else
                        p.sendMessage(msgs.PREFIX + "Bitte benutze den Befehl so:" + " §c/ausweis spitzname [Spitzname]");
                    break;
                case "kopieren":
                    if (args.length == 2) {
                        final int PRICE = 0;
                        String name = args[1];
                        if (plugin.getEcon().has(p, PRICE)) {
                            if (plugin.hasAusweis(name)) {
                                p.getInventory().addItem(plugin.getAusweis(name).getAsItem());
                                p.sendMessage(msgs.SUCC_COPY_AUSWEIS.replace("%s", name));
                                plugin.getEcon().withdrawPlayer(p, PRICE);
                            } else p.sendMessage(plugin.msgs.PREFIX + "Dieser Spieler hat kein Ausweis");
                        } else p.sendMessage(plugin.msgs.PREFIX + "Du hast nicht genug Geld!");
                    }
                    break;
                case "list":
                    for (Ausweis a : plugin.ausweis.values()) {
                        p.sendMessage("§e" + Bukkit.getOfflinePlayer(UUID.fromString(a.getUUID())).getName());
                    }
                case "admin":
                    break;
                default:
                    plugin.getVar().sendHelpMsg(p);
                    break;
            }

            if (sub.equalsIgnoreCase("admin")) {

                if(args.length >= 3) {

                    if (p.hasPermission("ftsengine.admin")) {

                        if (args[2].equalsIgnoreCase("geburtstag")) {

                            if(args.length == 4) {

                                String targetString = args[1];
                                Player target = Bukkit.getPlayer(targetString);

                                if (target != null) {

                                    if (!plugin.hasAusweis(target)) {
                                        p.sendMessage("§cDer Spieler hat keinen Ausweis!");
                                        return true;
                                    }

                                    String dob = args[3];
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM");
                                    Date date;
                                    try {
                                        date = sdf1.parse(dob);
                                        date.setTime(date.getTime() + 1);
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(date);
                                        plugin.getAusweis(target).setBirthday(cal);
                                        p.sendMessage(msgs.PREFIX + "Okay, der Spieler " + target.getName() + " hat am " + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + " Geburtstag!");
                                        target.sendMessage(msgs.PREFIX + "Der Spieler " + p.getName() + " hat deinen Geburtstag geändert. (" + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1) + ")");
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                } else p.sendMessage("§cDieser Spieler ist nicht online!");
                            } else p.sendMessage("§cBitte benutze den Befehl so: /ausweis admin SPIELER geburtstag TAG");
                        } else p.sendMessage("§cBitte benutze den Befehl so: /ausweis admin SPIELER geburtstag TAG");
                    } else p.sendMessage("§cDafür hast du keine Rechte LOL");
                }

            }

        } else plugin.getVar().sendHelpMsg(p);
        return false;
    }
}

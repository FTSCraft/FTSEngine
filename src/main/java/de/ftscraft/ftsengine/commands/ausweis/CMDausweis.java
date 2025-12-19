package de.ftscraft.ftsengine.commands.ausweis;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDausweis implements CommandExecutor, TabCompleter {

    private final Engine plugin;
    private final AusweisCommandService service;
    private final AusweisMessageFormatter formatter;
    private final AusweisTabCompleter tabCompleter;

    public CMDausweis(Engine plugin) {
        this.plugin = plugin;
        this.formatter = new AusweisMessageFormatter();
        this.service = new AusweisCommandService(plugin, formatter);
        this.tabCompleter = new AusweisTabCompleter();
        registerCommand();
    }


    private void registerCommand() {
        if (plugin.getCommand("ausweis") != null) {
            plugin.getCommand("ausweis").setExecutor(this);
            plugin.getCommand("ausweis").setTabCompleter(this);
        } else {
            plugin.getLogger().warning("Command 'ausweis' is not registered in plugin.yml!");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player p) {
                showAusweisOverview(p);
            } else {
                sender.sendMessage("Benutzung: /ausweis <subcommand> [...]");
            }
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "neu" -> handleNeu(sender, args);
            case "name" -> handleName(sender, args);
            case "geschlecht" -> handleGeschlecht(sender, args);
            case "rasse" -> handleRasse(sender, args);
            case "aussehen" -> handleAussehen(sender, args);
            case "größe", "große" -> handleGroesse(sender, args);
            case "link" -> handleLink(sender, args);
            case "anschauen" -> handleAnschauen(sender, args);
            case "deckname" -> handleDeckname(sender, args);
            case "wechseln" -> handleWechseln(sender);
            case "switchausweis" -> handleSwitchAusweis(sender, args);
            case "löschen" -> handleDeletion(sender, args);
            case "resetcooldown" -> handleResetCooldown(sender, args);
            case "skin" -> handleSkin(sender, args);
            case "list" -> handleList(sender, args);
            case "help" -> handleHelp(sender);
            default -> handleHelp(sender);
        }

        return true;
    }

    private void handleDeletion(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length == 2) {
            Integer i;
            try {
                i = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
                MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Bitte gib eine gültige Ausweis-ID an!</red>");
                return;
            }
            service.sendAusweisDeleteConfirmation(p, i);
        } else if(args.length == 3 && args[2].equalsIgnoreCase("confirm")) {
            Integer i;
            try {
                i = Integer.valueOf(args[1]);
            } catch (NumberFormatException e) {
                MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Bitte gib eine gültige Ausweis-ID an!</red>");
                return;
            }
            service.deleteAusweis(p, i);
        }
    }

    private void handleNeu(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 3) {
            sender.sendMessage("Benutzung: /ausweis neu [Vorname] [Nachname]");
            return;
        }

        service.createNewAusweis(p, args[1], args[2]);
        showAusweisOverview(p);
    }

    private void handleName(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 3) {
            sender.sendMessage("Benutzung: /ausweis name [Vorname] [Nachname]");
            return;
        }

        service.setName(p, args[1], args[2]);
    }

    private void handleGeschlecht(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage("Benutzung: /ausweis geschlecht [m/f]");
            return;
        }

        service.setGender(p, args[1]);
    }

    private void handleRasse(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage("Benutzung: /ausweis rasse [Ork/Zwerg/Mensch/Elf]");
            return;
        }

        service.setRace(p, args[1]);
    }

    private void handleAussehen(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 5) {
            sender.sendMessage("Benutzung: /ausweis aussehen [Aussehen (mind. 4 Wörter)]");
            return;
        }

        service.setAppearance(p, joinArgs(args, 1));
    }

    private void handleGroesse(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage("Benutzung: /ausweis größe [Größe in cm]");
            return;
        }

        try {
            int height = Integer.parseInt(args[1]);
            service.setHeight(p, height);
        } catch (NumberFormatException e) {
            sender.sendMessage("Bitte gib eine gültige Zahl an.");
        }
    }

    private void handleLink(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage("Benutzung: /ausweis link [Link]");
            return;
        }

        service.setForumLink(p, joinArgs(args, 1));
    }

    private void handleAnschauen(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (!p.hasPermission("ftsengine.ausweis.anschauen")) {
            sender.sendMessage("Du hast keine Berechtigung, Ausweise anzuschauen.");
            return;
        }

        if (args.length == 1) {
            service.showOwnAusweis(p);
        } else {
            // Check if argument is a number (ID) and player has list permission
            try {
                int ausweisId = Integer.parseInt(args[1]);
                if (p.hasPermission("ftsengine.ausweis.list")) {
                    service.showAusweisByID(p, ausweisId);
                } else {
                    MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast keine Berechtigung, Ausweise über ID anzuschauen.</red>");
                }
            } catch (NumberFormatException e) {
                // Not a number, treat as player name
                service.showOtherAusweis(p, args[1]);
            }
        }
    }

    private void handleDeckname(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage("Benutzung: /ausweis deckname [Deckname]");
            return;
        }

        service.setCoverName(p, joinArgs(args, 1));
    }

    private void handleWechseln(CommandSender sender) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;
        service.sendSwitchAusweisList(p);
    }

    private void handleSwitchAusweis(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 2) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Benutzung: /ausweis switchausweis <ID></red>");
            return;
        }

        int ausweisId;
        try {
            ausweisId = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Bitte gib eine gültige Ausweis-ID an!</red>");
            return;
        }

        service.switchAusweis(p, ausweisId);
    }

    private void handleResetCooldown(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (!sender.hasPermission("ftsengine.resetcooldown")) {
            sender.sendMessage("Du hast keine Berechtigung hierfür.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("Benutzung: /ausweis resetcooldown <all|Spielername>");
            return;
        }

        if (args[1].equalsIgnoreCase("all")) {
            int count = service.resetAllHeightCooldowns();
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>Größen-Cooldown für <yellow>" + count +
                    "</yellow> Spieler wurde erfolgreich zurückgesetzt!</gray>");
            service.notifyAllOnlinePlayersAboutCooldownReset();
        } else {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>Spieler <red>" + args[1] +
                        "</red> ist nicht online oder existiert nicht!</gray>");
                return;
            }

            if (!service.resetHeightCooldown(target)) {
                MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Dieser Spieler hat keinen Ausweis!</red>");
                return;
            }

            MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>Größen-Cooldown für <yellow>" + target.getName() +
                    "</yellow> wurde erfolgreich zurückgesetzt!</gray>");
            MiniMsg.msg(target, Messages.MINI_PREFIX +
                    "<gray>Dein Größen-Cooldown wurde von einem Admin zurückgesetzt. " +
                    "Du kannst deine Größe jetzt wieder ändern!</gray>");
        }
    }

    private void handleSkin(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage("Benutzung: /ausweis skin <Spielername>");
            return;
        }

        service.changeSkin(p, args[1]);
    }

    private void handleList(CommandSender sender, String[] args) {
        if (!requiresPlayer(sender)) return;
        Player p = (Player) sender;

        if (!p.hasPermission("ftsengine.ausweis.list")) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast keine Berechtigung hierfür.</red>");
            return;
        }

        if (args.length < 2) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Benutzung: /ausweis list <Spielername></red>");
            return;
        }

        service.listPlayerAusweise(p, args[1]);
    }

    private void handleHelp(CommandSender sender) {
        if (sender instanceof Player p) {
            formatter.sendHelpMessage(p);
        } else {
            sender.sendMessage("/ausweis - verfügbare Subcommands: neu, name, geschlecht, rasse, aussehen, größe, deckname, link, anschauen, wechseln, resetcooldown, skin");
        }
    }

    private void showAusweisOverview(Player p) {
        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast keine Ausweise! Erstelle einen <blue><click:suggest_command:/ausweis neu >[hier]</click></blue>");
            return;
        }
        Ausweis ausweis = plugin.getAusweis(p);
        formatter.sendAusweisEditMessage(p, ausweis);
    }

    // ===== Helper methods =====

    private boolean requiresPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von Spielern ausgeführt werden.");
            return false;
        }
        return true;
    }

    private String joinArgs(String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            if (i > startIndex) sb.append(' ');
            sb.append(args[i]);
        }
        return sb.toString();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tabCompleter.onTabComplete(sender, command, alias, args);
    }
}

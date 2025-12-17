package de.ftscraft.ftsengine.commands.ausweis;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Tab completion for Ausweis (ID card) commands.
 * Responsible for: Auto-completion of command arguments.
 */
public class AusweisTabCompleter implements TabCompleter {

    private static final String[] SUBCOMMANDS = {
            "neu", "name", "geschlecht", "rasse", "aussehen", "größe", "große",
            "link", "anschauen", "deckname", "wechseln", "resetcooldown", "skin", "help"
    };

    private static final String[] RACES = {"Ork", "Zwerg", "Mensch", "Elf"};

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        // First argument: Only show "anschauen" (as in the original)
        if (args.length == 1) {
            String prefix = args[0].toLowerCase();
            if ("anschauen".startsWith(prefix)) {
                completions.add("anschauen");
            }
            return completions;
        }

        String sub = args[0].toLowerCase();

        // Context-dependent completion
        return switch (sub) {
            case "name", "neu" -> handleNameCompletion(args);
            case "geschlecht" -> handleGenderCompletion(args);
            case "rasse" -> handleRaceCompletion(args);
            case "aussehen" -> handleAppearanceCompletion(args);
            case "link" -> handleLinkCompletion(args);
            case "deckname" -> handleCoverNameCompletion(args);
            case "anschauen", "skin" -> handlePlayerNameCompletion(args);
            case "resetcooldown" -> handleResetCooldownCompletion(args);
            case "größe", "große" -> handleHeightCompletion(args);
            default -> completions;
        };
    }

    private List<String> handleNameCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            if ("<Vorname>".toLowerCase().startsWith(prefix)) {
                completions.add("<Vorname>");
            }
        } else if (args.length == 3) {
            String prefix = args[2].toLowerCase();
            if ("<Nachname>".toLowerCase().startsWith(prefix)) {
                completions.add("<Nachname>");
            }
        }
        return completions;
    }

    private List<String> handleGenderCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            if ("m".startsWith(prefix)) completions.add("m");
            if ("f".startsWith(prefix)) completions.add("f");
        }
        return completions;
    }

    private List<String> handleRaceCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            for (String race : RACES) {
                if (race.toLowerCase().startsWith(prefix)) {
                    completions.add(race);
                }
            }
        }
        return completions;
    }

    private List<String> handleAppearanceCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            completions.add("<Freitext: mind. 4 Wörter>");
        }
        return completions;
    }

    private List<String> handleLinkCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            completions.add("<Freitext: https://forum.ftscraft.de/...>");
        }
        return completions;
    }

    private List<String> handleCoverNameCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            completions.add("<Freitext: Unterstriche für Leerzeichen>");
        }
        return completions;
    }

    private List<String> handlePlayerNameCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            for (Player online : Bukkit.getOnlinePlayers()) {
                String name = online.getName();
                if (name.toLowerCase().startsWith(prefix)) {
                    completions.add(name);
                }
            }
        }
        return completions;
    }

    private List<String> handleResetCooldownCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            if ("all".startsWith(prefix)) {
                completions.add("all");
            }
            for (Player online : Bukkit.getOnlinePlayers()) {
                String name = online.getName();
                if (name.toLowerCase().startsWith(prefix)) {
                    completions.add(name);
                }
            }
        }
        return completions;
    }

    private List<String> handleHeightCompletion(String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            completions.add("<Größe in cm>");
        }
        return completions;
    }
}


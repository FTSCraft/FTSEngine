package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.feature.items.signs.TeachingBoard;
import de.ftscraft.ftsengine.feature.items.signs.TeachingBoardManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsutils.cmd.TabCompleteUtil;
import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

public class CMDlehrtafel implements CommandExecutor, TabCompleter {
    private final Engine plugin;

    public CMDlehrtafel(Engine plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("lehrtafel"), "tried registering lehrtafel command but is null").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (!TeachingBoardManager.getEditingPlayers().containsKey(player)) {
            player.sendMessage("§cKlicke zuerst mit Shift-Rechtsklick eine deiner Lehrtafeln an");
            return true;
        }
        Sign teachingBoardSign = TeachingBoardManager.getEditingPlayers().get(player);
        if (!TeachingBoardManager.near(teachingBoardSign, player.getLocation(), 4)) {
            player.sendMessage("§cDu befindest dich nicht in der Nähe der Lehrtafel.");
            return true;
        }
        TeachingBoard teachingBoard = TeachingBoardManager.fetch(teachingBoardSign);
        if (teachingBoard == null) {
            player.sendMessage("§cEin Fehler ist aufgetreten. §fKontaktiere den Support.");
            return true;
        }
        if (!teachingBoard.getOwners().contains(player.getUniqueId().toString())) {
            player.sendMessage("§cDu bist nicht berechtigt diese Lehrtafel zu bearbeiten.");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("add")) {

                plugin.getChatInputService().request(player, Component.text("Bitte gib den Text für die Lehrtafel ein:"), Duration.ofSeconds(60))
                        .thenAccept(input -> {
                            if (input == null) {
                                player.sendMessage(Component.text("Eingabe wurde abgebrochen oder ist ausgelaufen!").color(NamedTextColor.RED));
                                return;
                            }
                            TeachingBoardManager.addLine(teachingBoardSign, input);
                            TeachingBoardManager.showLines(player, teachingBoardSign, true, true);
                        });
                return true;
            } else if (args[0].equalsIgnoreCase("destroy")) {
                TeachingBoardManager.destroy(teachingBoardSign);
                player.sendMessage("§aDie Lehrtafel wurde erfolgreich zerstört.");
                return true;
            } else if (args[0].equalsIgnoreCase("save")) {
                TeachingBoardManager.save(teachingBoardSign, teachingBoard);
                TeachingBoardManager.getEditingPlayers().remove(player);
                player.sendMessage("§aDie Änderungen wurden erfolgreich gespeichert.");
                return true;
            } else if (args[0].equalsIgnoreCase("cancel")) {
                TeachingBoardManager.getEditingPlayers().remove(player);
                player.sendMessage("§aDie Änderungen wurden erfolgreich verworfen.");
                return true;
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("insert")) {
                int index = 0;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                if (index >= teachingBoard.getLines().size()) {
                    player.sendMessage("§cDer Index muss kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                int finalIndex = index;
                plugin.getChatInputService().request(player, Component.text("Gebe jetzt den Text ein:"), Duration.ofSeconds(60))
                        .thenAccept(lineInput -> {
                            if (lineInput == null) {
                                player.sendMessage(Component.text("Eingabe wurde abgebrochen oder ist ausgelaufen!").color(NamedTextColor.RED));
                                return;
                            }
                            TeachingBoardManager.insertLine(teachingBoardSign, lineInput, finalIndex);
                            TeachingBoardManager.showLines(player, teachingBoardSign, true, true);
                        });
                return true;
            } else if (args[0].equalsIgnoreCase("delete")) {
                int index = 0;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                if (index >= teachingBoard.getLines().size()) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                TeachingBoardManager.removeLine(teachingBoardSign, index);
                TeachingBoardManager.showLines(player, teachingBoardSign, true, false);
                return true;
            } else if (args[0].equalsIgnoreCase("edit")) {
                int index = 0;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                if (index >= teachingBoard.getLines().size()) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                Component editingLine = Component.text("Klicke hier um Zeile zu laden oder gebe einen neuen Text ein:")
                        .color(NamedTextColor.GOLD)
                        .decorate(TextDecoration.BOLD)
                        .clickEvent(ClickEvent.suggestCommand(teachingBoard.getLines().get(index)));

                int finalIndex = index;
                plugin.getChatInputService().request(player, editingLine, Duration.ofSeconds(60))
                        .thenAccept(input -> {
                            if (input == null) {
                                player.sendMessage(Component.text("Eingabe wurde abgebrochen oder ist ausgelaufen!").color(NamedTextColor.RED));
                                return;
                            }
                            TeachingBoardManager.setLine(teachingBoardSign, input, finalIndex);
                            TeachingBoardManager.showLines(player, teachingBoardSign, true, true);
                        });
                return true;
            } else if (args[0].equalsIgnoreCase("allow")) {
                if (!TeachingBoardManager.mainOwner(teachingBoardSign, player)) {
                    player.sendMessage("§cDu bist nicht berechtigt für diese Lehrtafel Berechtigungen zu setzen");
                    return true;
                }
                UUID targetUuid = UUIDFetcher.getUUID(args[1]);
                if (teachingBoard.getOwners().contains(targetUuid.toString())) {
                    player.sendMessage("§cDieser Spieler hat bereits Bearbeitungsrechte auf dieser Lehrtafel");
                    return true;
                }
                TeachingBoardManager.addOwner(teachingBoardSign, targetUuid.toString());
                player.sendMessage("§aDer Spieler hat nun Bearbeitungsrechte auf dieser Lehrtafel");
                return true;
            } else if (args[0].equalsIgnoreCase("disallow")) {
                if (!TeachingBoardManager.mainOwner(teachingBoardSign, player)) {
                    player.sendMessage("§cDu bist nicht berechtigt für diese Lehrtafel Berechtigungen zu setzen");
                    return true;
                }
                UUID targetUuid = UUIDFetcher.getUUID(args[1]);
                if (!teachingBoard.getOwners().contains(targetUuid.toString())) {
                    player.sendMessage("§cDieser Spieler hat keine Bearbeitungsrechte auf dieser Lehrtafel");
                    return true;
                }
                TeachingBoardManager.removeOwner(teachingBoardSign, targetUuid.toString());
                player.sendMessage("§aDer Spieler hat nun keine Bearbeitungsrechte mehr auf dieser Lehrtafel");
                return true;
            } else if (args[0].equalsIgnoreCase("add")) {
                String input = args[1];
                TeachingBoardManager.addLine(teachingBoardSign, input);
                TeachingBoardManager.showLines(player, teachingBoardSign, true, false);
                return true;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("insert")) {
                int index = 0;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                if (index >= teachingBoard.getLines().size()) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                String input = args[2];
                TeachingBoardManager.insertLine(teachingBoardSign, input, index);
                TeachingBoardManager.showLines(player, teachingBoardSign, true, false);
                return true;
            } else if (args[0].equalsIgnoreCase("edit")) {
                int index = 0;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException ignored) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                if (index >= teachingBoard.getLines().size()) {
                    player.sendMessage("§cDer Index muss eine Ganzzahl und kleiner als " + teachingBoard.getLines().size() + " sein");
                    return true;
                }
                String input = args[2];
                TeachingBoardManager.setLine(teachingBoardSign, input, index);
                TeachingBoardManager.showLines(player, teachingBoardSign, true, false);
                return true;
            }
        }
        player.sendMessage("§7----- §6Lehrtafel Hilfe §7-----");
        player.sendMessage("§6Platzhalter:");
        player.sendMessage("§7- %space% §f>> Leere Zeile einfügen (per add, insert oder edit)");
        player.sendMessage(" ");
        player.sendMessage("§6Legende: §7<...>: §fPflichtfeld §7| [...]: §fOptional");
        player.sendMessage(" ");
        player.sendMessage("§6Befehle:");
        player.sendMessage("§7- /lehrtafel add [Text] §f>> Zeile hinzufügen");
        player.sendMessage("§7- /lehrtafel insert <Index> [Text] §f>> Zeile an Position einfügen");
        player.sendMessage("§7- /lehrtafel edit <Index> [Text] §f>> Zeile bearbeiten");
        player.sendMessage("§7- /lehrtafel delete <Index> §f>> Zeile löschen");
        player.sendMessage("§7- /lehrtafel allow <Spieler> §f>> Bearbeitungsrechte geben");
        player.sendMessage("§7- /lehrtafel disallow <Spieler> §f>> Bearbeitungsrechte entziehen");
        player.sendMessage("§7- /lehrtafel destroy §f>> Lehrtafel entfernen");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return TabCompleteUtil.completeArgs(args[0], "add", "allow", "disallow", "insert", "delete", "edit", "destroy");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("allow") || args[0].equalsIgnoreCase("disallow")) {
                return TabCompleteUtil.completePlayers(args[1]);
            }
            if (args[0].equalsIgnoreCase("add")) {
                return TabCompleteUtil.completeArgs(args[1], "[Text]", "%space%");
            }
            return TabCompleteUtil.completeArgs(args[1], "<Index>");
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("insert") || args[0].equalsIgnoreCase("edit")) {
                return TabCompleteUtil.completeArgs(args[2], "[Text]", "%space%");
            }
        }
        return new ArrayList<>();
    }
}

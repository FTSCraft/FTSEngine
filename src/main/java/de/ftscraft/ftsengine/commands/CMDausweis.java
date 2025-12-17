package de.ftscraft.ftsengine.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.SkinService;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import de.ftscraft.ftsutils.misc.MiniMsg;
import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class CMDausweis {

    private final Engine plugin;

    private static final double HEIGHT_COOLDOWN = 1000 * 60 * 60;

    public CMDausweis(Engine plugin) {
        this.plugin = plugin;
        registerCommand();
    }

    private static final int MAX_HEIGHT = 300, MIN_HEIGHT = 90;

    public void registerCommand() {
        Engine.getInstance().getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                commands -> commands.registrar().register(createAusweisCommand())
        );
    }

    private LiteralCommandNode<CommandSourceStack> createAusweisCommand() {
        return Commands.literal("ausweis")
                .requires(sender -> sender.getExecutor() instanceof Player)
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getExecutor();
                    sendHelpMsg(player);
                    return Command.SINGLE_SUCCESS;
                })
                .then(Commands.literal("neu")
                        .requires(sender -> {
                            if (sender.getExecutor() instanceof Player player) {
                                return plugin.hasAusweis(player);
                            }
                            return false;
                        })
                        .then(Commands.argument("vorname", StringArgumentType.word())
                                .then(Commands.argument("nachname", StringArgumentType.word())
                                        .executes(this::handleNeu)
                                )
                        )
                )
                .then(Commands.literal("name")
                        .then(Commands.argument("vorname", StringArgumentType.word())
                                .then(Commands.argument("nachname", StringArgumentType.word())
                                        .executes(this::handleName)
                                )
                        )
                )
                .then(Commands.literal("geschlecht")
                        .then(Commands.argument("gender", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("m");
                                    builder.suggest("f");
                                    return builder.buildFuture();
                                })
                                .executes(this::handleGender)
                        )
                )
                .then(Commands.literal("rasse")
                        .then(Commands.argument("rasse", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("Ork");
                                    builder.suggest("Zwerg");
                                    builder.suggest("Mensch");
                                    builder.suggest("Elf");
                                    return builder.buildFuture();
                                })
                                .executes(this::handleRace)
                        )
                )
                .then(Commands.literal("aussehen")
                        .then(Commands.argument("beschreibung", StringArgumentType.greedyString())
                                .executes(this::handleAppearance)
                        )
                )
                .then(Commands.literal("größe")
                        .then(Commands.argument("größe", IntegerArgumentType.integer(MIN_HEIGHT, MAX_HEIGHT))
                                .executes(this::handleHeight)
                        )
                )
                .then(Commands.literal("link")
                        .then(Commands.argument("url", StringArgumentType.greedyString())
                                .executes(this::handleLink)
                        )
                )
                .then(Commands.literal("anschauen")
                        .requires(sender -> sender.getExecutor() instanceof Player &&
                                sender.getExecutor().hasPermission("ftsengine.ausweis.anschauen"))
                        .executes(this::handleInspectSelf)
                        .then(Commands.argument("spieler", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                        builder.suggest(onlinePlayer.getName());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(this::handleInspectOther)
                        )
                )
                .then(Commands.literal("deckname")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(this::handleCoverName)
                        )
                )
                .then(Commands.literal("wechseln")
                        .executes(this::handleListAusweise)
                )
                .then(Commands.literal("resetcooldown")
                        .requires(sender -> sender.getExecutor() instanceof Player &&
                                sender.getExecutor().hasPermission("ftsengine.resetcooldown"))
                        .then(Commands.argument("ziel", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("all");
                                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                        builder.suggest(onlinePlayer.getName());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(this::handleResetCooldown)
                        )
                )
                .then(Commands.literal("skin")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(this::handleSkin)
                        )
                )
                .build();
    }

    private int handleSkin(CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast noch keinen Ausweis! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        String targetName = StringArgumentType.getString(ctx, "name");
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>Dein Skin wird nun geändert, dies kann einen Moment dauern...</gray>");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            SkinService.SkinData skinData = SkinService.getSkinData(UUIDFetcher.getUUID(targetName));

            // Zurück zum Main-Thread für Datenbank-Operationen
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (skinData == null) {
                    MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Der angegebene Spielername ist ungültig oder der Spieler existiert nicht!</red>");
                    return;
                }

                Ausweis ausweis = plugin.getAusweis(p);
                if (ausweis == null) {
                    MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Dein Ausweis konnte nicht gefunden werden!</red>");
                    return;
                }

                ausweis.setSkinData(skinData);
                plugin.saveAusweis(ausweis);
                ausweis.applySkinToPlayer(p);
                MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Dein Skin wurde erfolgreich auf den von <yellow>" + targetName + "</yellow> geändert!</green>");
            });
        });

        return Command.SINGLE_SUCCESS;
    }

    private int handleNeu(CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String fName = StringArgumentType.getString(ctx, "vorname");
        String lName = StringArgumentType.getString(ctx, "nachname");

        // Die requires-Bedingung stellt sicher, dass der Spieler noch keinen Ausweis hat
        Ausweis a = new Ausweis(p.getUniqueId());
        a.setFirstName(fName);
        a.setLastName(lName);
        plugin.addAusweis(a);

        MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Du hast erfolgreich einen neuen Ausweis mit dem Namen <yellow>" + fName + " " + lName + "</yellow> erstellt!</green>");
        return Command.SINGLE_SUCCESS;
    }

    private int handleName(CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String fName = StringArgumentType.getString(ctx, "vorname");
        String lName = StringArgumentType.getString(ctx, "nachname");

        if (plugin.hasAusweis(p)) {
            plugin.getAusweis(p).setFirstName(fName);
            plugin.getAusweis(p).setLastName(lName);
            plugin.saveAusweis(plugin.getAusweis(p));
        } else {
            Ausweis a = new Ausweis(p.getUniqueId());
            a.setFirstName(fName);
            a.setLastName(lName);
            plugin.addAusweis(a);
        }
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Name wurde erfolgreich auf <yellow>" + fName + " " + lName + "</yellow> gesetzt!</green>");
        return Command.SINGLE_SUCCESS;
    }

    private int handleGender(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String genderStr = StringArgumentType.getString(ctx, "gender");

        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast noch keinen Ausweis! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        Ausweis.Gender g;
        if (genderStr.equalsIgnoreCase("m")) {
            g = Ausweis.Gender.MALE;
        } else if (genderStr.equalsIgnoreCase("f")) {
            g = Ausweis.Gender.FEMALE;
        } else {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Bitte benutze den Befehl so: <yellow>/ausweis geschlecht [m/f]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        plugin.getAusweis(p).setGender(g);
        plugin.saveAusweis(plugin.getAusweis(p));
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Geschlecht wurde erfolgreich auf <yellow>" + (g == Ausweis.Gender.MALE ? "Mann" : "Frau") + "</yellow> gesetzt!</green>");
        return Command.SINGLE_SUCCESS;
    }

    private int handleRace(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String raceStr = StringArgumentType.getString(ctx, "rasse");

        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast noch keinen Ausweis! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        String race = raceStr.substring(0, 1).toUpperCase() + raceStr.substring(1).toLowerCase();

        switch (race) {
            case "Ork":
            case "Zwerg":
            case "Mensch":
            case "Elf":
                plugin.getAusweis(p).setRace(race);
                plugin.saveAusweis(plugin.getAusweis(p));
                MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Rasse wurde erfolgreich auf <yellow>" + race + "</yellow> gesetzt!</green>");
                break;
            default:
                MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Bitte benutze den Befehl so: <yellow>/ausweis rasse [Ork/Zwerg/Mensch/Elf]</yellow></red>");
        }
        return Command.SINGLE_SUCCESS;
    }

    private int handleAppearance(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String desc = StringArgumentType.getString(ctx, "beschreibung");

        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast noch keinen Ausweis! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        // Mindestens 4 Wörter prüfen
        String[] words = desc.split("\\s+");
        if (words.length < 4) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Bitte benutze den Befehl so: <yellow>/ausweis aussehen [Aussehen (mind. 4 Wörter)]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        plugin.getAusweis(p).setDesc(desc);
        plugin.saveAusweis(plugin.getAusweis(p));
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Aussehen wurde erfolgreich auf <yellow>" + desc + "</yellow> gesetzt!</green>");
        return Command.SINGLE_SUCCESS;
    }

    private int handleHeight(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        int height = IntegerArgumentType.getInteger(ctx, "größe");

        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast noch keinen Ausweis! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        Ausweis ausweis = plugin.getAusweis(p);

        if (ausweis.getLastHeightChange() + HEIGHT_COOLDOWN >= System.currentTimeMillis()) {
            if (!p.hasPermission("ftssurvival.bypass") && !p.hasPermission("ftsengine.mod")) {
                MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du darfst deine Größe jede Stunde ändern</red>");
                return Command.SINGLE_SUCCESS;
            }
        }

        ausweis.setHeight(height);
        plugin.saveAusweis(ausweis);
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Größe wurde erfolgreich auf <yellow>" + height + "</yellow> gesetzt!</green>");
        AttributeInstance scaleAttr = p.getAttribute(Attribute.SCALE);

        if (scaleAttr == null) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Da ist was schiefgelaufen.</red>");
            return Command.SINGLE_SUCCESS;
        }

        scaleAttr.setBaseValue(height / 200d);
        return Command.SINGLE_SUCCESS;
    }

    private int handleLink(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String link = StringArgumentType.getString(ctx, "url");

        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast noch keinen Ausweis! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        // Prüfen ob es ein gültiger Link ist
        if (!isValidUrl(link)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Das ist kein gültiger Link! Bitte überprüfe den Link und versuche es erneut.</red>");
            return Command.SINGLE_SUCCESS;
        }

        if (!link.startsWith("https://forum.ftscraft.de/")) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Der Link muss mit unserer URL des Forums anfangen! (https://forum.ftscraft.de/)</red>");
            return Command.SINGLE_SUCCESS;
        }

        plugin.getAusweis(p).setForumLink(link);
        plugin.saveAusweis(plugin.getAusweis(p));
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Charaktervorstellung wurde erfolgreich auf <yellow>" + link + "</yellow> gesetzt!</green>");
        return Command.SINGLE_SUCCESS;
    }

    private boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        // Prüfen ob der String mit http:// oder https:// beginnt
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }

        if (url.contains(" ")) {
            return false;
        }

        // Versuchen, die URL zu parsen
        try {
            new java.net.URL(url);
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        }
    }

    private int handleInspectSelf(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast keinen Ausweis!</red>");
            return Command.SINGLE_SUCCESS;
        }
        Var.sendAusweisMsg(p, plugin.getAusweis(p));
        return Command.SINGLE_SUCCESS;
    }

    private int handleInspectOther(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String targetName = StringArgumentType.getString(ctx, "spieler");

        Ausweis a = plugin.getAusweis(UUIDFetcher.getUUID(targetName));
        if (a != null) {
            Var.sendAusweisMsg(p, a);
        } else {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Dieser Spieler hat keinen Ausweis!</red>");
        }
        return Command.SINGLE_SUCCESS;
    }

    private int handleCoverName(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String deckname = StringArgumentType.getString(ctx, "name").replace("_", " ");

        if (!plugin.hasAusweis(p)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast noch keinen Ausweis! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        plugin.getAusweis(p).setSpitzname(deckname);
        plugin.saveAusweis(plugin.getAusweis(p));
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<green>Du hast deinen Decknamen als <yellow>" + deckname + "</yellow> gesetzt!</green>");
        return Command.SINGLE_SUCCESS;
    }

    private int handleListAusweise(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();

        java.util.List<Ausweis> ausweise = plugin.getDatabaseHandler()
                .getAusweisStorageManager()
                .getAusweiseByUUID(p.getUniqueId());

        if (ausweise == null || ausweise.isEmpty()) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Du hast keine Ausweise! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return Command.SINGLE_SUCCESS;
        }

        Ausweis activeAusweis = plugin.getAusweis(p);

        p.sendMessage(" ");
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>----- Deine Ausweise -----</red>");
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>Klicke auf einen Ausweis, um zu diesem zu wechseln:</gray>");
        p.sendMessage(" ");

        for (Ausweis ausweis : ausweise) {
            boolean isActive = activeAusweis != null && activeAusweis.getId() == ausweis.getId();

            net.kyori.adventure.text.Component ausweisComponent = net.kyori.adventure.text.Component.text()
                    .append(net.kyori.adventure.text.Component.text("  " + (isActive ? "➤ " : "  "))
                            .color(isActive ? net.kyori.adventure.text.format.NamedTextColor.GREEN : net.kyori.adventure.text.format.NamedTextColor.GRAY))
                    .append(net.kyori.adventure.text.Component.text(ausweis.getFirstName() + " " + ausweis.getLastName())
                            .color(isActive ? net.kyori.adventure.text.format.NamedTextColor.YELLOW : net.kyori.adventure.text.format.NamedTextColor.WHITE)
                            .decorate(isActive ? net.kyori.adventure.text.format.TextDecoration.BOLD : net.kyori.adventure.text.format.TextDecoration.ITALIC))
                    .append(net.kyori.adventure.text.Component.text(" " + (isActive ? "(Aktiv)" : "[Wechseln]"))
                            .color(isActive ? net.kyori.adventure.text.format.NamedTextColor.GREEN : net.kyori.adventure.text.format.NamedTextColor.AQUA)
                            .clickEvent(isActive ? null : net.kyori.adventure.text.event.ClickEvent.runCommand("/ftsengine _switchausweis " + ausweis.getId()))
                            .hoverEvent(isActive ? null : net.kyori.adventure.text.event.HoverEvent.showText(
                                    net.kyori.adventure.text.Component.text("Zu " + ausweis.getFirstName() + " " + ausweis.getLastName() + " wechseln")
                                            .color(net.kyori.adventure.text.format.NamedTextColor.YELLOW)
                            )))
                    .build();

            p.sendMessage(ausweisComponent);
        }

        p.sendMessage(" ");

        return Command.SINGLE_SUCCESS;
    }

    private int handleResetCooldown(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String targetName = StringArgumentType.getString(ctx, "ziel");

        if (targetName.equalsIgnoreCase("all")) {
            return handleResetCooldownAll(p);
        } else {
            return handleResetCooldownPlayer(p, targetName);
        }
    }

    private int handleResetCooldownAll(Player p) {
        int resetCount = 0;
        // Über den Ausweis-Cache im AusweisStorageManager iterieren
        for (Ausweis ausweis : plugin.getDatabaseHandler().getAusweisStorageManager().getAusweisCache().values()) {
            ausweis.setLastHeightChange(0);
            plugin.saveAusweis(ausweis);
            resetCount++;
        }
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>Größen-Cooldown für <yellow>" + resetCount +
                "</yellow> Spieler wurde erfolgreich zurückgesetzt!</gray>");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (plugin.hasAusweis(onlinePlayer)) {
                MiniMsg.msg(onlinePlayer, Messages.MINI_PREFIX +
                        "<gray>Dein Größen-Cooldown wurde von einem Admin zurückgesetzt. " +
                        "Du kannst deine Größe jetzt wieder ändern!</gray>");
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    private int handleResetCooldownPlayer(Player p, String targetName) {
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>Spieler <red>" + targetName +
                    "</red> ist nicht online oder existiert nicht!</gray>");
            return Command.SINGLE_SUCCESS;
        }

        if (!plugin.hasAusweis(target)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "<red>Dieser Spieler hat keinen Ausweis!</red>");
            return Command.SINGLE_SUCCESS;
        }

        Ausweis targetAusweis = plugin.getAusweis(target);
        targetAusweis.setLastHeightChange(0);
        plugin.saveAusweis(targetAusweis);

        MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>Größen-Cooldown für <yellow>" + targetName +
                "</yellow> wurde erfolgreich zurückgesetzt!</gray>");
        MiniMsg.msg(target, Messages.MINI_PREFIX +
                "<gray>Dein Größen-Cooldown wurde von einem Admin zurückgesetzt. " +
                "Du kannst deine Größe jetzt wieder ändern!</gray>");

        return Command.SINGLE_SUCCESS;
    }

    public static void sendHelpMsg(Player p) {
        MiniMsg.msg(p, Messages.MINI_PREFIX + "<gray>----- <red>/ausweis</red> -----</gray>");
        MiniMsg.msg(p, "<gray>/ausweis neu [Vorname] [Nachname] <red>Erstellt einen neuen Ausweis</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis wechseln <red>Zeigt alle deine Ausweise und ermöglicht das Wechseln</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis name [Vorname] [Nachname] <red>Ändert deinen Namen und erstellt beim 1. Mal einen Ausweis - Mit Unterstrichen könnt ihr Leerzeichen im Namen haben</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis geschlecht [m/f] <red>Setzt die Ansprache (m - Männliche | f - Weibliche)</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis rasse [Ork/Zwerg/Mensch/Elf] <red>Setzt deine Rasse</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis aussehen [Beschr.] <red>Setzt dein Aussehen (Mind. 4 Wörter)</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis größe [Größe in cm] <red>Setzt deine Größe</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis deckname [Deckname] <red>Setzt deinen Decknamen</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis link [Link] <red>Setzt den Link zu deiner Charvorstellung im Forum</red></gray>");
        MiniMsg.msg(p, "<gray>/ausweis anschauen [Spieler] <red>Schau den Ausweis eines Spielers an</red></gray>");
    }

}

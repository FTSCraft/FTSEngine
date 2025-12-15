package de.ftscraft.ftsengine.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
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
                .build();
    }

    private int handleName(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String fName = StringArgumentType.getString(ctx, "vorname");
        String lName = StringArgumentType.getString(ctx, "nachname");

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
        return Command.SINGLE_SUCCESS;
    }

    private int handleGender(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String genderStr = StringArgumentType.getString(ctx, "gender");

        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return Command.SINGLE_SUCCESS;
        }

        Ausweis.Gender g;
        if (genderStr.equalsIgnoreCase("m")) {
            g = Ausweis.Gender.MALE;
        } else if (genderStr.equalsIgnoreCase("f")) {
            g = Ausweis.Gender.FEMALE;
        } else {
            p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" +
                    " §c/ausweis geschlecht [\"m\"/\"f\"]");
            return Command.SINGLE_SUCCESS;
        }

        plugin.getAusweis(p).setGender(g);
        p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Geschlecht")
                .replace("%v", (g == Ausweis.Gender.MALE ? "Mann" : "Frau")));
        return Command.SINGLE_SUCCESS;
    }

    private int handleRace(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String raceStr = StringArgumentType.getString(ctx, "rasse");

        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return Command.SINGLE_SUCCESS;
        }

        String race = raceStr.substring(0, 1).toUpperCase() + raceStr.substring(1).toLowerCase();

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
        return Command.SINGLE_SUCCESS;
    }

    private int handleAppearance(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String desc = StringArgumentType.getString(ctx, "beschreibung");

        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return Command.SINGLE_SUCCESS;
        }

        // Mindestens 4 Wörter prüfen
        String[] words = desc.split("\\s+");
        if (words.length < 4) {
            p.sendPlainMessage(Messages.PREFIX + "Bitte benutze den Befehl so:" +
                    " §c/ausweis aussehen [Aussehen (mind. 4 Wörter)]");
            return Command.SINGLE_SUCCESS;
        }

        plugin.getAusweis(p).setDesc(desc);
        p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Aussehen").replace("%v", desc));
        return Command.SINGLE_SUCCESS;
    }

    private int handleHeight(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        int height = IntegerArgumentType.getInteger(ctx, "größe");

        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return Command.SINGLE_SUCCESS;
        }

        Ausweis ausweis = plugin.getAusweis(p);

        if (ausweis.getLastHeightChange() + HEIGHT_COOLDOWN >= System.currentTimeMillis()) {
            if (!p.hasPermission("ftssurvival.bypass") && !p.hasPermission("ftsengine.mod")) {
                MiniMsg.msg(p, Messages.MINI_PREFIX + "Du darfst deine Größe jede Stunde ändern");
                return Command.SINGLE_SUCCESS;
            }
        }

        ausweis.setHeight(height);
        p.sendMessage(Messages.SUCC_CMD_AUSWEIS.replace("%s", "Größe").replace("%v", String.valueOf(height)));
        AttributeInstance scaleAttr = p.getAttribute(Attribute.SCALE);

        if (scaleAttr == null) {
            MiniMsg.msg(p, "Da ist was schiefgelaufen.");
            return Command.SINGLE_SUCCESS;
        }

        scaleAttr.setBaseValue(height / 200d);
        return Command.SINGLE_SUCCESS;
    }

    private int handleLink(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String link = StringArgumentType.getString(ctx, "url");

        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return Command.SINGLE_SUCCESS;
        }

        if (!link.startsWith("https://forum.ftscraft.de/")) {
            p.sendPlainMessage("§cDer Link muss mit unserer URL des Forums anfangen! " +
                    "(https://forum.ftscraft.de/)");
            return Command.SINGLE_SUCCESS;
        }

        plugin.getAusweis(p).setForumLink(link);
        p.sendPlainMessage(Messages.SUCC_CMD_AUSWEIS
                .replace("%s", "Charaktervorstellung").replace("%v", link));
        return Command.SINGLE_SUCCESS;
    }

    private int handleInspectSelf(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
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
            p.sendPlainMessage(Messages.TARGET_NO_AUSWEIS);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int handleCoverName(com.mojang.brigadier.context.CommandContext<CommandSourceStack> ctx) {
        Player p = (Player) ctx.getSource().getExecutor();
        String deckname = StringArgumentType.getString(ctx, "name").replace("_", " ");

        if (!plugin.hasAusweis(p)) {
            p.sendPlainMessage(Messages.NEED_AUSWEIS);
            return Command.SINGLE_SUCCESS;
        }

        plugin.getAusweis(p).setSpitzname(deckname);
        p.sendMessage(Messages.PREFIX + "Du hast deinen Decknamen als " + deckname + " gesetzt!");
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
        for (Ausweis ausweis : plugin.ausweis.values()) {
            ausweis.setLastHeightChange(0);
            resetCount++;
        }
        p.sendMessage(Messages.PREFIX + "§7Größen-Cooldown für §e" + resetCount +
                " §7Spieler wurde erfolgreich zurückgesetzt!");

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (plugin.hasAusweis(onlinePlayer)) {
                onlinePlayer.sendMessage(Messages.PREFIX +
                        "§7Dein Größen-Cooldown wurde von einem Admin zurückgesetzt. " +
                        "Du kannst deine Größe jetzt wieder ändern!");
            }
        }
        return Command.SINGLE_SUCCESS;
    }

    private int handleResetCooldownPlayer(Player p, String targetName) {
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            p.sendMessage(Messages.PREFIX + "Spieler §c" + targetName +
                    " §7ist nicht online oder existiert nicht!");
            return Command.SINGLE_SUCCESS;
        }

        if (!plugin.hasAusweis(target)) {
            p.sendMessage(Messages.TARGET_NO_AUSWEIS);
            return Command.SINGLE_SUCCESS;
        }

        Ausweis targetAusweis = plugin.getAusweis(target);
        targetAusweis.setLastHeightChange(0);

        p.sendMessage(Messages.PREFIX + "§7Größen-Cooldown für §e" + targetName +
                " §7wurde erfolgreich zurückgesetzt!");
        target.sendMessage(Messages.PREFIX +
                "§7Dein Größen-Cooldown wurde von einem Admin zurückgesetzt. " +
                "Du kannst deine Größe jetzt wieder ändern!");

        return Command.SINGLE_SUCCESS;
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

package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.CountdownScheduler;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.cmd.TabCompleteUtil;
import de.ftscraft.ftsutils.misc.MiniMsg;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Objects;

public class CMDtaube implements CommandExecutor, TabCompleter {

    private final Engine plugin;
    // Hashmap to store messages with their UUIDs
    private final Map<UUID, TaubeMessage> messages = new HashMap<>();

    public CMDtaube(Engine plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand("taube"), "tried registering taube command but is null").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player player)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        // 1. Case: Get Book ("/taube get <uuid>")
        if (args.length >= 1 && args[0].equalsIgnoreCase("get")) {
            handleGettingBook(args, player);
            return true;
        }

        // 2. Case: Send Pigeon ("/taube [anonym] <player> <message>")
        // We check for "anonym" flag
        boolean isAnonymous = false;
        int targetIndex = 0;    // Standard: Target is args[0]
        int msgStartIndex = 1;  // Standard: Message starts at args[1]

        if (args.length >= 1 && args[0].equalsIgnoreCase("anonym")) {
            isAnonymous = true;
            targetIndex = 1;    // Anonym: Target moves one index further
            msgStartIndex = 2;  // Anonym: Messages moves one index further
        }

        // Validation: Enough arguments provided?
        // Anonym needs at least 3 args, normal at least 2
        if (args.length < (isAnonymous ? 3 : 2)) {
            String usage = isAnonymous
                    ? "Bitte gebe einen Spieler und eine Nachricht an."
                    : "Nutze: /taube <Spieler> <Nachricht> oder /taube anonym <Spieler> <Nachricht>";
            MiniMsg.msg(player, Messages.MINI_PREFIX + usage);
            return false;
        }

        //
        String messageContent = String.join(" ", Arrays.copyOfRange(args, msgStartIndex, args.length));

        // Unterstützung für mehrere Empfänger (durch Komma getrennt)
        String[] targetPlayerNames = args[targetIndex].split(",");

        for (String targetName : targetPlayerNames) {
            sendTaubeToTarget(player, targetName, messageContent, isAnonymous);
        }

        return true;
    }

    private void sendTaubeToTarget(Player sender, String targetName, String messageContent, boolean isAnonymous) {
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            sender.sendMessage(Messages.PREFIX + "§7Der Spieler §c" + targetName + " §7wurde nicht gefunden");
            return;
        }

        int seconds = calculateTime(sender.getLocation(), target.getLocation());
        UUID messageUuid = UUID.randomUUID();

        // WICHTIG: Hier ermitteln wir den finalen Anzeigenamen (RP, Anonym oder Minecraft-Name)
        String displaySenderName = determineSenderName(sender, isAnonymous);

        // Wir speichern diesen Anzeigenamen direkt im Record
        TaubeMessage taubeMessage = new TaubeMessage(displaySenderName, messageContent, messageUuid);
        messages.put(messageUuid, taubeMessage);

        sender.sendMessage(Messages.PREFIX + "Eine Taube fliegt zu §c" + target.getName() + "§7.");
        sender.playSound(sender.getLocation(), Sound.ENTITY_BAT_LOOP, 3, -20);

        // Der Scheduler nutzt später taubeMessage.senderDisplayName() für die "Angekommen"-Nachricht
        new CountdownScheduler(plugin, seconds, sender, target, taubeMessage);
    }

    /**
     * Ermittelt den Namen des Absenders basierend auf RP-Daten (Ausweis) und Anonymität.
     */
    private String determineSenderName(Player player, boolean isAnonymous) {
        // 1. Wenn Anonym -> immer "Unbekannt"
        if (isAnonymous) {
            return "Unbekannt";
        }

        // 2. Try: Get RP Name from Ausweis
        // The Method getAusweis(player) returns the Ausweis object for the player
        Ausweis ausweis = plugin.getAusweis(player);

        if (ausweis != null) {
            String first = ausweis.getFirstName();
            String last = ausweis.getLastName();

            // Verify both names are not null
            if (first != null && last != null) {
                return first + " " + last;
            }
        }

        // 3. Fallback: Minecraft Username
        return player.getName();
    }

    private void handleGettingBook(String[] args, Player player) {
        if (args.length < 2) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "Ein Fehler ist aufgetreten.");
            return;
        }

        UUID messageUuid;
        try {
            messageUuid = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "Ungültige ID.");
            return;
        }

        TaubeMessage taubeMessage = messages.get(messageUuid);

        if (taubeMessage == null) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "Du hast diesen Brief bereits erhalten oder er existiert nicht.");
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(Messages.PREFIX + "§cDu brauchst mindestens einen freien Inventarplatz für den Brief!");
            return;
        }

        // Generate Book with stored senderDisplayName and message
        ItemStack bookItemStack = generateBook(taubeMessage.senderDisplayName(), taubeMessage.message());

        messages.remove(messageUuid);

        player.getInventory().addItem(bookItemStack);
        player.sendMessage(Messages.PREFIX + "§7Du hast den Brief von der Taube genommen!");
    }

    private int calculateTime(Location from, Location to) {
        if (from.getWorld() != to.getWorld()) {
            return 120;
        }

        double distance = from.distance(to);
        if (distance < 350) {
            return 5;
        }

        int calculatedSeconds = (int) (distance / 70);
        return Math.min(calculatedSeconds, 120);
    }

    private @NotNull ItemStack generateBook(String senderDisplayName, String message) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        if (meta != null) {
            meta.setTitle("Brief");

            // Set the Autor to the final sender display name
            meta.setAuthor(senderDisplayName);

            // DisplayName of the book includes the sender display name
            meta.displayName(Component.text("Brief von " + senderDisplayName).color(NamedTextColor.YELLOW));

            meta.addPages(Component.text(message));
            book.setItemMeta(meta);
        }
        return book;
    }

    /**
     * Record saves now the final sender display name directly.
     *
     */
    public record TaubeMessage(String senderDisplayName, String message, UUID uuid) {
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        // Argument 1: Suggest "anonym", "get" OR a Player Name
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            // Add custom keywords
            completions.addAll(TabCompleteUtil.completeArgs(args[0], "anonym"));

            // Add players (for the standard usage /taube PlayerName)
            completions.addAll(TabCompleteUtil.completePlayers(args[0]));

            return completions;
        }

        // Argument 2: Handling specific subcommands
        if (args.length == 2) {
            // If the user typed "/taube anonym", suggest a player name now
            if (args[0].equalsIgnoreCase("anonym")) {
                return TabCompleteUtil.completePlayers(args[1]);
            }
        }

        // For all other cases (typing the message), suggest nothing
        return Collections.emptyList();
    }
}

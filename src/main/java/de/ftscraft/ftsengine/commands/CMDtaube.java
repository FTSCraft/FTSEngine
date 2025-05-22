package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.CountdownScheduler;
import de.ftscraft.ftsengine.utils.Messages;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CMDtaube implements CommandExecutor {

    private final Engine plugin;
    private final HashMap<UUID, TaubeMessage> messages = new HashMap<>();
    private final HashMap<UUID, Set<UUID>> receivedMessages = new HashMap<>();

    public CMDtaube(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("taube").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("get")) {
            if (args.length < 2) {
                MiniMsg.msg(p, Messages.MINI_PREFIX + "Ein Fehler ist aufgetreten.");
                return true;
            }
            handleGettingBook(args, p);
            return true;
        }

        if (args.length < 2) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "Bitte gebe einen Spieler und eine Nachricht an.");
            return false;
        }

        // If you want to send a taube to only one or more specific player*s, split with: ','
        String[] targetPlayerNames = args[0].split(",");

        for (String targetName : targetPlayerNames) {
            sendTaubeToTarget(args, p, targetName);
        }

        return true;
    }

    private void sendTaubeToTarget(String[] args, Player p, String targetName) {
        Location playerLocation = p.getLocation();
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            p.sendMessage(Messages.PREFIX + "§7Der Spieler §c" + targetName + " §7wurde nicht gefunden");
        } else {
            Location targetLocation = target.getLocation();
            int seconds = calculateTime(playerLocation, targetLocation, target, p);

            StringBuilder msg = new StringBuilder();

            for (int i = 1; i < args.length; i++) {
                msg.append(" ").append(args[i]);
            }

            UUID messageUuid = UUID.randomUUID();
            TaubeMessage taubeMessage = new TaubeMessage(p.getName(), msg.toString(), messageUuid);
            messages.put(messageUuid, taubeMessage);

            p.sendMessage(Messages.PREFIX + "Eine Taube fliegt zu §c" + target.getName() + "§7.");
            p.playSound(p.getLocation(), Sound.ENTITY_BAT_LOOP, 3, -20);

            new CountdownScheduler(plugin, seconds, p, target, taubeMessage);
        }
    }

    private static int calculateTime(Location playerLocation, Location targetLocation, Player target, Player p) {
        int seconds;
        if (playerLocation.getWorld() != targetLocation.getWorld()) {
            seconds = 120;
        } else {
            double distance = target.getLocation().distance(p.getLocation());
            if (distance < 350) {
                seconds = 5;
            } else {
                seconds = (int) distance / 70;
            }
            if (seconds > 120)
                seconds = 120;
        }
        return seconds;
    }

    private void handleGettingBook(String[] args, Player p) {
        UUID messageUuid;

        try {
            messageUuid = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "Irgendwas ist schiefgelaufen.");
            return;
        }

        Set<UUID> playerReceivedMessages = receivedMessages.computeIfAbsent(p.getUniqueId(), k -> new HashSet<>());

        // Check if player has already received this message
        if (playerReceivedMessages.contains(messageUuid)) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "Du hast diesen Brief bereits erhalten.");
            return;
        }

        TaubeMessage taubeMessage = messages.get(messageUuid);

        if (taubeMessage == null) {
            MiniMsg.msg(p, Messages.MINI_PREFIX + "Diese Nachricht existiert nicht mehr.");
            return;
        }

        // Check if player has inventory space
        if (p.getInventory().firstEmpty() == -1) {
            p.sendMessage(Messages.PREFIX + "§cDu brauchst mindestens einen freien Inventarplatz für den Brief!");
            return;
        }

        String message = taubeMessage.message;
        String senderName = taubeMessage.sender;

        ItemStack bookItemStack = generateBook(senderName, message, messageUuid);

        // Mark this message as received by this player
        playerReceivedMessages.add(messageUuid);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            p.getInventory().addItem(bookItemStack);
            p.sendMessage(Messages.PREFIX + "§7Du hast den Brief von der Taube genommen!");
        }, 5L);
    }

    private static @NotNull ItemStack generateBook(String senderName, String message, UUID messageUuid) {
        ItemStack bookItemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) bookItemStack.getItemMeta();
        bookMeta.displayName(Component.text("Brief von " + senderName).color(NamedTextColor.YELLOW));
        bookMeta.addPages(Component.text(message));
        bookItemStack.setItemMeta(bookMeta);
        return bookItemStack;
    }

    public static class TaubeMessage {
        String sender, message;
        UUID uuid;

        public TaubeMessage(String sender, String message, UUID uuid) {
            this.sender = sender;
            this.message = message;
            this.uuid = uuid;
        }

        public String getMessage() {
            return message;
        }

        public UUID getUuid() {
            return uuid;
        }
    }
}

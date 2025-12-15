package de.ftscraft.ftsengine.feature.items.logport;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class LogportManager {
    private final Engine plugin;

    public static final String LOCATION_KEY = "logport_location";
    public static final String USES_LEFT_KEY = "uses_left";
    public static final String MAX_USES_KEY = "max_uses";

    private final Map<UUID, BukkitRunnable> teleportTasks = new HashMap<>();
    private final Map<UUID, Location> initialLocations = new HashMap<>();

    public LogportManager(Engine plugin) {
        this.plugin = plugin;
    }

    public void saveLocationToItem(Player player, ItemStack item) {
        Location currentLocation = player.getLocation();
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(new NamespacedKey(plugin, LOCATION_KEY + "_world"), PersistentDataType.STRING, currentLocation.getWorld().getName());
        data.set(new NamespacedKey(plugin, LOCATION_KEY + "_x"), PersistentDataType.DOUBLE, currentLocation.getX());
        data.set(new NamespacedKey(plugin, LOCATION_KEY + "_y"), PersistentDataType.DOUBLE, currentLocation.getY());
        data.set(new NamespacedKey(plugin, LOCATION_KEY + "_z"), PersistentDataType.DOUBLE, currentLocation.getZ());
        data.set(new NamespacedKey(plugin, LOCATION_KEY + "_yaw"), PersistentDataType.FLOAT, currentLocation.getYaw());
        data.set(new NamespacedKey(plugin, LOCATION_KEY + "_pitch"), PersistentDataType.FLOAT, currentLocation.getPitch());

        item.setItemMeta(meta);

        player.sendMessage(Messages.PREFIX + ChatColor.GREEN + "Position im Logport gespeichert!");
    }

    private void teleportToSavedLocation(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        NamespacedKey world_loc_key = new NamespacedKey(plugin, LOCATION_KEY + "_world");
        if (data.has(world_loc_key, PersistentDataType.STRING)) {
            String worldName = data.get(world_loc_key, PersistentDataType.STRING);

            NamespacedKey x_loc_key = new NamespacedKey(plugin, LOCATION_KEY + "_x");
            NamespacedKey y_loc_key = new NamespacedKey(plugin, LOCATION_KEY + "_y");
            NamespacedKey z_loc_key = new NamespacedKey(plugin, LOCATION_KEY + "_z");
            NamespacedKey yaw_loc_key = new NamespacedKey(plugin, LOCATION_KEY + "_yaw");
            NamespacedKey pitch_loc_key = new NamespacedKey(plugin, LOCATION_KEY + "_pitch");

            double x = Objects.requireNonNull(data.get(x_loc_key, PersistentDataType.DOUBLE), "x coordinate in logport is null");
            double y = Objects.requireNonNull(data.get(y_loc_key, PersistentDataType.DOUBLE), "y coordinate in logport is null");
            double z = Objects.requireNonNull(data.get(z_loc_key, PersistentDataType.DOUBLE), "z coordinate in logport is null");
            float yaw = Objects.requireNonNull(data.get(yaw_loc_key, PersistentDataType.FLOAT), "yaw in logport is null");
            float pitch = Objects.requireNonNull(data.get(pitch_loc_key, PersistentDataType.FLOAT), "pitch in logport is null");

            //noinspection DataFlowIssue - checked above
            Location savedLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

            player.teleport(savedLocation);
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Zum gespeicherten Ort teleportiert!</green>");
        } else {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Keine Position im Logport gespeichert!</red>");
        }
    }

    public void startTeleportCountdown(Player player, ItemStack item) {
        UUID playerId = player.getUniqueId();

        if (teleportTasks.containsKey(playerId)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Ein Teleport ist bereits in Bearbeitung!</red>");
            return;
        }

        if (!hasSavedLocation(item)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Speichere zuerst eine Position im Logport!</red>");
            return;
        }

        if (getUsesLeft(item) <= 0) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Der Logport ist aufgebraucht und kann nicht verwendet werden!</red>");
            return;
        }

        Location initialLocation = player.getLocation();
        initialLocations.put(playerId, initialLocation);
        MiniMsg.msg(player, Messages.MINI_PREFIX + "<yellow>Teleport in 5 Sekunden... nicht bewegen oder Schaden erleiden!</yellow>");

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (teleportTasks.containsKey(playerId)) {
                    Location currentLocation = player.getLocation();
                    Location initialLoc = initialLocations.get(playerId);
                    if (hasMoved(initialLoc, currentLocation)) {
                        MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Teleport abgebrochen: Du hast dich bewegt!</red>");
                    } else {
                        teleportToSavedLocation(player, item);
                        decrementUses(item, player);
                    }
                    teleportTasks.remove(playerId);
                    initialLocations.remove(playerId);
                }
            }
        };

        teleportTasks.put(playerId, task);
        task.runTaskLater(plugin, 100L); // 100L = 5 Sekunden
    }

    private boolean hasSavedLocation(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.has(new NamespacedKey(plugin, LOCATION_KEY + "_world"), PersistentDataType.STRING);
    }

    private boolean hasMoved(Location initialLocation, Location currentLocation) {
        return initialLocation.getBlockX() != currentLocation.getBlockX() ||
                initialLocation.getBlockY() != currentLocation.getBlockY() ||
                initialLocation.getBlockZ() != currentLocation.getBlockZ();
    }

    public boolean hasTeleportTask(Player player) {
        return teleportTasks.containsKey(player.getUniqueId());
    }

    public void cancelTeleport(Player player, String message) {
        UUID playerId = player.getUniqueId();
        BukkitRunnable task = teleportTasks.remove(playerId);
        initialLocations.remove(playerId);

        if (task != null) {
            task.cancel();
            player.sendMessage(message);
        }
    }

    public void decrementUses(ItemStack item, Player player) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        int usesLeft = Objects.requireNonNull(data.get(new NamespacedKey(plugin, USES_LEFT_KEY), PersistentDataType.INTEGER), "uses_left in logport is null");
        usesLeft--;

        if (usesLeft <= 0) {
            data.set(new NamespacedKey(plugin, USES_LEFT_KEY), PersistentDataType.INTEGER, 0);
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Der Logport ist aufgebraucht!</red>");
        } else {
            data.set(new NamespacedKey(plugin, USES_LEFT_KEY), PersistentDataType.INTEGER, usesLeft);
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Verbleibende Verwendungen im Logport: " + usesLeft + "</green>");
        }

        updateLogportLore(meta);
        item.setItemMeta(meta);
    }

    public void reloadLogport(Player player, ItemStack item) {
        int emeralds = countEmeralds(player);
        if (emeralds <= 0) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Du hast keine Smaragde, um den Logport aufzuladen!</red>");
            return;
        }

        int usesLeft = getUsesLeft(item);
        int maxUses = getMaxUses(item);
        int usesToAdd = Math.min(maxUses - usesLeft, emeralds);

        if (usesToAdd > 0) {
            removeEmeralds(player, usesToAdd);
            usesLeft += usesToAdd;

            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(plugin, USES_LEFT_KEY), PersistentDataType.INTEGER, usesLeft);
            updateLogportLore(meta);
            item.setItemMeta(meta);

            MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Logport aufgeladen. Verbleibende Verwendungen: " + usesLeft + "</green>");
        } else {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Der Logport ist bereits voll aufgeladen.</red>");
        }
    }

    public int getUsesLeft(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        return data.getOrDefault(new NamespacedKey(plugin, USES_LEFT_KEY), PersistentDataType.INTEGER, 0);
    }


    public int getMaxUses(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        return data.getOrDefault(new NamespacedKey(plugin, MAX_USES_KEY), PersistentDataType.INTEGER, 0);
    }

    public int countEmeralds(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.EMERALD) {
                count += item.getAmount();
            }
        }
        return count;
    }

    public void removeEmeralds(Player player, int amount) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.EMERALD) {
                int count = item.getAmount();
                if (count > amount) {
                    item.setAmount(count - amount);
                    break;
                } else {
                    player.getInventory().remove(item);
                    amount -= count;
                    if (amount <= 0) {
                        break;
                    }
                }
            }
        }
    }

    public void updateLogportLore(ItemMeta meta) {
        PersistentDataContainer data = meta.getPersistentDataContainer();

        int usesLeft = Objects.requireNonNull(data.get(new NamespacedKey(plugin, USES_LEFT_KEY), PersistentDataType.INTEGER), "uses_left in logport is null");
        int maxUses = Objects.requireNonNull(data.get(new NamespacedKey(plugin, MAX_USES_KEY), PersistentDataType.INTEGER), "max_uses in logport is null");

        List<String> lore = new ArrayList<>();
        lore.add("§7Teleportiert dich zu einem vorher festgelegten Punkt");
        lore.add("§6" + usesLeft + "/" + maxUses + " Nutzungen verbleibend");

        meta.setLore(lore);
    }


    public void onDisableLogic() {
        teleportTasks.values().forEach(BukkitRunnable::cancel);
        teleportTasks.clear();
        initialLocations.clear();
    }

}

package de.ftscraft.ftsengine.logport;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LogportManager {
    private final Engine plugin;

    private static final String LOCATION_KEY = "logport_location";
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

        if (data.has(new NamespacedKey(plugin, LOCATION_KEY + "_world"), PersistentDataType.STRING)) {
            String worldName = data.get(new NamespacedKey(plugin, LOCATION_KEY + "_world"), PersistentDataType.STRING);
            double x = data.get(new NamespacedKey(plugin, LOCATION_KEY + "_x"), PersistentDataType.DOUBLE);
            double y = data.get(new NamespacedKey(plugin, LOCATION_KEY + "_y"), PersistentDataType.DOUBLE);
            double z = data.get(new NamespacedKey(plugin, LOCATION_KEY + "_z"), PersistentDataType.DOUBLE);
            float yaw = data.get(new NamespacedKey(plugin, LOCATION_KEY + "_yaw"), PersistentDataType.FLOAT);
            float pitch = data.get(new NamespacedKey(plugin, LOCATION_KEY + "_pitch"), PersistentDataType.FLOAT);

            Location savedLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

            player.teleport(savedLocation);
            player.sendMessage(Messages.PREFIX + ChatColor.GREEN + "Zum gespeicherten Ort teleportiert!");
        } else {
            player.sendMessage(Messages.PREFIX + ChatColor.RED + "Keine Position im Logport gespeichert!");
        }
    }

    public void startTeleportCountdown(Player player, ItemStack item) {
        UUID playerId = player.getUniqueId();
        if (teleportTasks.containsKey(playerId)) {
            player.sendMessage(Messages.PREFIX + ChatColor.RED + "Ein Teleport ist bereits in Bearbeitung!");
            return;
        }

        Location initialLocation = player.getLocation();
        initialLocations.put(playerId, initialLocation);
        player.sendMessage(Messages.PREFIX + ChatColor.YELLOW + "Teleport in 5 Sekunden... nicht bewegen oder Schaden erleiden!");

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (teleportTasks.containsKey(playerId)) {
                    Location currentLocation = player.getLocation();
                    Location initialLoc = initialLocations.get(playerId);
                    if (hasMoved(initialLoc, currentLocation)) {
                        player.sendMessage(Messages.PREFIX + ChatColor.RED + "Teleport abgebrochen: Du hast dich bewegt!");
                    } else {
                        teleportToSavedLocation(player, item);
                    }
                    teleportTasks.remove(playerId);
                    initialLocations.remove(playerId);
                }
            }
        };

        teleportTasks.put(playerId, task);
        task.runTaskLater(plugin, 100L); // 100L = 5 Sekunden
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

    public void onDisableLogic() {
        teleportTasks.values().forEach(BukkitRunnable::cancel);
        teleportTasks.clear();
        initialLocations.clear();
    }

}

package de.ftscraft.ftsengine.quivers;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Quiver {

    private static final Set<Material> VALID_ARROWS = new HashSet<>(Arrays.asList(
            Material.ARROW,
            Material.TIPPED_ARROW,
            Material.SPECTRAL_ARROW
    ));

    private final int id;
    private final QuiverType type;
    private final Inventory inventory;
    private final Engine plugin;

    public Quiver(Engine plugin, QuiverType type, int id, Inventory inventory) {
        this.plugin = plugin;
        this.type = type;
        this.id = id;
        if (plugin.biggestQuiverId < id) {
            plugin.biggestQuiverId = id;
        }
        this.inventory = inventory;
        plugin.quivers.put(this.id, this);
    }

    public Quiver(Engine plugin, QuiverType type, Player player, ItemStack quiver) {
        this.plugin = plugin;
        this.type = type;
        this.inventory = Bukkit.createInventory(null, type.getSize(), type.getName());
        this.id = plugin.biggestQuiverId + 1;
        plugin.biggestQuiverId = this.id;

        ItemMeta im = quiver.getItemMeta();
        im.setLore(Arrays.asList(type.getLore(), "ID: #" + this.id));
        quiver.setItemMeta(im);

        player.sendMessage(Messages.PREFIX + "Dein Köcher ist nun registriert. (ID: " + this.id + ")");

        plugin.quivers.put(this.id, this);
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

    public static boolean isValidArrow(ItemStack item) {
        return item != null && VALID_ARROWS.contains(item.getType());
    }

    public void save() {
        File file = new File(plugin.getDataFolder() + "//quivers//" + this.id + ".yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("id", this.id);
        ItemStack[] content = inventory.getContents();
        cfg.set("inventory", content);
        cfg.set("type", this.type.name());

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getId() {
        return id;
    }

    public static void returnArrowToQuiver(Player player, Engine plugin) {
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        ItemStack quiverItem = findQuiverInInventory(player);
        if (quiverItem == null) return;

        int quiverId = Var.getQuiverID(quiverItem);
        if (quiverId == -1) return;

        Quiver quiver = plugin.quivers.get(quiverId);
        if (quiver == null) return;

        if (!quiver.isValidArrow(offhandItem)) return;

        quiver.getInventory().addItem(offhandItem);
        player.getInventory().setItemInOffHand(null);
    }

    public ItemStack getArrowFromQuiver() {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && isValidArrow(item)) {
                ItemStack arrow = item.clone();
                arrow.setAmount(1);
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    inventory.setItem(i, null);
                }
                return arrow;

            }
        }
        return null;
    }

    public void consumeArrow(Player player) {
        ItemStack shotArrow = player.getInventory().getItemInOffHand();
        if (shotArrow == null || !isValidArrow(shotArrow)) {
            return;
        }

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && isValidArrow(item)) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    inventory.setItem(i, null);
                }
                break;
            }
        }

        ItemStack quiverItem = findQuiverInInventory(player);
        if (quiverItem != null) {
        }
    }

    public static boolean isQuiver(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        String sign = ItemReader.getSign(item);
        return sign != null && sign.equalsIgnoreCase(QuiverType.QUIVER.getSign());
    }

    public static void storeArrowInQuiver(Player player, Engine plugin) {
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        if (offhandItem == null || !VALID_ARROWS.contains(offhandItem.getType())) return;

        ItemStack quiverItem = findQuiverInInventory(player);
        if (quiverItem == null) return;

        Quiver quiver = getQuiverFromItem(plugin, quiverItem);
        if (quiver == null) return;

        quiver.getInventory().addItem(offhandItem);
        player.getInventory().setItemInOffHand(null);
    }

    public static ItemStack findQuiverInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && QuiverType.getQuiverByItem(item) != null) {
                return item;
            }
        }
        return null;
    }

    public static Quiver getQuiverFromItem(Engine plugin, ItemStack quiverItem) {
        int quiverId = Var.getQuiverID(quiverItem);
        return (quiverId != -1) ? plugin.quivers.get(quiverId) : null;
    }
}
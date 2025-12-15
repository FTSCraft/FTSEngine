package de.ftscraft.ftsengine.feature.items.backpacks;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class Backpack {

    private final int id;
    private final BackpackType type;
    private final Inventory inventory;

    private final Engine plugin;

    public Backpack(Engine plugin, BackpackType type, int id, Inventory inv) {
        this.plugin = plugin;
        this.type = type;
        this.id = id;
        if (plugin.biggestBpId < id) {
            plugin.biggestBpId = id;
        }
        this.inventory = inv;
        plugin.backpacks.put(this.id, this);
    }


    public Backpack(Engine plugin, BackpackType type, Player p, ItemStack backpack) {
        this.plugin = plugin;
        this.type = type;
        this.inventory = Bukkit.createInventory(null, type.getSize(), type.getName());
        this.id = plugin.biggestBpId + 1;
        plugin.biggestBpId = this.id;

        Var.setBackpackId(backpack, id);
        p.sendMessage(Messages.PREFIX + "Dein Rucksack ist nun Registriert. (ID: " + this.id + ")");

        plugin.backpacks.put(this.id, this);
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

    public void safe() {
        File file = new File(plugin.getDataFolder() + "//backpacks//" + this.id + ".yml");
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

    public BackpackType getType() {
        return type;
    }
}

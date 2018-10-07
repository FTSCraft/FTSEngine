package de.ftscraft.ftsengine.brett;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BrettGUI
{

    private Inventory inv;

    private Brett brett;
    private Engine plugin;

    public BrettGUI(Brett brett, Engine plugin) {
        this.brett = brett;
        this.plugin = plugin;
        inv = Bukkit.createInventory(null, 9 * 5, "§4Schwarzes-Brett " + brett.getName());
        setupGui();
    }

    private void setupGui() {
        ItemStack pinnwand = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta pinnwandMeta = pinnwand.getItemMeta();
        pinnwandMeta.setDisplayName("§7Pinnwand");
        pinnwand.setItemMeta(pinnwandMeta);

        ItemStack emptyNote = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta emptyNoteMeta = emptyNote.getItemMeta();
        emptyNoteMeta.setDisplayName("§8Leere Notitz");
        emptyNote.setItemMeta(emptyNoteMeta);

        ItemStack create = new ItemStack(Material.PAPER, 1);
        ItemMeta createMeta = create.getItemMeta();
        createMeta.setDisplayName("§cErstelle Notitz");
        List<String> createLore = new ArrayList<String>();
        createLore.add("§bErstelle eine Notitz!");
        createLore.add(" ");
        createLore.add("§8Preis: §c1 Taler");
        createMeta.setLore(createLore);
        create.setItemMeta(createMeta);

        for(int i = 0; i < 9 * 5; i++) {
            if(i < 9 || i > 35) {
                inv.setItem(i, pinnwand);
            } else inv.setItem(i, emptyNote);
        }
        inv.setItem(41, create);
    }

    public void addNote(ItemStack itemStack, BrettNote note) {
        for(int i = 0; i < 9 * 5; i++) {
            if(inv.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("§8Leere Notitz")) {
                inv.setItem(i, itemStack);
                note.invslot = i;
                return;
            }
        }
    }

    public void removeNote(int itemSlot) {
        ItemStack emptyNote = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta emptyNoteMeta = emptyNote.getItemMeta();
        emptyNoteMeta.setDisplayName("§8Leere Notitz");
        emptyNote.setItemMeta(emptyNoteMeta);

        inv.setItem(itemSlot, emptyNote);
    }

    public void open(Player player)
    {
        player.openInventory(inv);
    }

    public boolean isFull()
    {
        for(int i = 0; i < 9 * 5; i++) {
            if(inv.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("§8Leere Notitz")) {
                return false;
            }
        }
        return true;
    }

}

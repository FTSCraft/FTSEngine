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

public class BrettGUI {

    private final Inventory inv_page1;
    private final Inventory inv_page2;
    private final Inventory inv_page3;
    private final Inventory inv_page4;
    private final Inventory inv_page5;

    private final Brett brett;

    public BrettGUI(Brett brett, Engine plugin) {
        this.brett = brett;
        inv_page1 = Bukkit.createInventory(null, 9 * 5, "§4Schwarzes-Brett " + brett.getName());
        inv_page2 = Bukkit.createInventory(null, 9 * 5, "§4Schwarzes-Brett " + brett.getName());
        inv_page3 = Bukkit.createInventory(null, 9 * 5, "§4Schwarzes-Brett " + brett.getName());
        inv_page4 = Bukkit.createInventory(null, 9 * 5, "§4Schwarzes-Brett " + brett.getName());
        inv_page5 = Bukkit.createInventory(null, 9 * 5, "§4Schwarzes-Brett " + brett.getName());
        setupGui();
    }

    private void setupGui() {
        ItemStack pinnwand = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta pinnwandMeta = pinnwand.getItemMeta();
        pinnwandMeta.setDisplayName("§7Pinnwand");
        pinnwand.setItemMeta(pinnwandMeta);

        ItemStack emptyNote = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta emptyNoteMeta = emptyNote.getItemMeta();
        emptyNoteMeta.setDisplayName("§8Leere Notiz");
        emptyNote.setItemMeta(emptyNoteMeta);

        ItemStack create = new ItemStack(Material.PAPER, 1);
        ItemMeta createMeta = create.getItemMeta();
        createMeta.setDisplayName("§cErstelle Notiz");
        List<String> createLore = new ArrayList<>();
        createLore.add("§bErstelle eine Notiz!");
        createLore.add(" ");

        if (!brett.isAdmin())
            createLore.add("§8Preis: §c0 Taler");
        else
            createLore.add("§8Preis: §c0 Taler");

        createMeta.setLore(createLore);
        create.setItemMeta(createMeta);

        ItemStack page1Item = new ItemStack(Material.FLOWER_BANNER_PATTERN);
        ItemMeta page1Meta = page1Item.getItemMeta();
        page1Meta.setDisplayName("§cSeite 1");
        page1Item.setItemMeta(page1Meta);

        ItemStack page2Item = new ItemStack(Material.FLOWER_BANNER_PATTERN);
        ItemMeta page2Meta = page1Item.getItemMeta();
        page2Meta.setDisplayName("§cSeite 2");
        page2Item.setItemMeta(page2Meta);

        ItemStack page3Item = new ItemStack(Material.FLOWER_BANNER_PATTERN);
        ItemMeta page3Meta = page1Item.getItemMeta();
        page3Meta.setDisplayName("§cSeite 3");
        page3Item.setItemMeta(page3Meta);

        ItemStack page4Item = new ItemStack(Material.FLOWER_BANNER_PATTERN);
        ItemMeta page4Meta = page1Item.getItemMeta();
        page4Meta.setDisplayName("§cSeite 4");
        page4Item.setItemMeta(page4Meta);

        ItemStack page5Item = new ItemStack(Material.FLOWER_BANNER_PATTERN);
        ItemMeta page5Meta = page1Item.getItemMeta();
        page5Meta.setDisplayName("§cSeite 5");
        page5Item.setItemMeta(page5Meta);

        for (int i = 0; i < 9 * 5; i++) {
            if (i < 9 || i > 35) {
                inv_page1.setItem(i, pinnwand);
            } else inv_page1.setItem(i, emptyNote);
        }
        inv_page1.setItem(41, create);
        inv_page1.setItem(37, page2Item);
        inv_page1.setItem(38, page3Item);
        inv_page1.setItem(39, page4Item);
        inv_page1.setItem(40, page5Item);


        for (int i = 0; i < 9 * 5; i++) {
            if (i < 9 || i > 35) {
                inv_page2.setItem(i, pinnwand);
            } else inv_page2.setItem(i, emptyNote);
        }
        inv_page2.setItem(41, create);
        inv_page2.setItem(36, page1Item);
        inv_page2.setItem(38, page3Item);
        inv_page2.setItem(39, page4Item);
        inv_page2.setItem(40, page5Item);


        for (int i = 0; i < 9 * 5; i++) {
            if (i < 9 || i > 35) {
                inv_page3.setItem(i, pinnwand);
            } else inv_page3.setItem(i, emptyNote);
        }
        inv_page3.setItem(41, create);
        inv_page3.setItem(36, page1Item);
        inv_page3.setItem(37, page2Item);
        inv_page3.setItem(39, page4Item);
        inv_page3.setItem(40, page5Item);

        for (int i = 0; i < 9 * 5; i++) {
            if (i < 9 || i > 35) {
                inv_page4.setItem(i, pinnwand);
            } else inv_page4.setItem(i, emptyNote);
        }
        inv_page4.setItem(41, create);
        inv_page4.setItem(36, page1Item);
        inv_page4.setItem(37, page2Item);
        inv_page4.setItem(38, page3Item);
        inv_page4.setItem(40, page5Item);


        for (int i = 0; i < 9 * 5; i++) {
            if (i < 9 || i > 35) {
                inv_page5.setItem(i, pinnwand);
            } else inv_page5.setItem(i, emptyNote);
        }
        inv_page5.setItem(41, create);
        inv_page5.setItem(36, page1Item);
        inv_page5.setItem(37, page2Item);
        inv_page5.setItem(38, page3Item);
        inv_page5.setItem(39, page4Item);
    }

    public void addNote(ItemStack itemStack, BrettNote note) {

        for (int i = 0; i < 9 * 5; i++) {
            if (inv_page1.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("§8Leere Notiz")) {
                inv_page1.setItem(i, itemStack);
                note.invslot = i;
                note.page = 1;
                return;
            }
        }
        for (int i = 0; i < 9 * 5; i++) {
            if (inv_page2.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("§8Leere Notiz")) {
                inv_page2.setItem(i, itemStack);
                note.invslot = i;
                note.page = 2;
                return;
            }
        }
        for (int i = 0; i < 9 * 5; i++) {
            if (inv_page3.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("§8Leere Notiz")) {
                inv_page3.setItem(i, itemStack);
                note.invslot = i;
                note.page = 3;
                return;
            }
        }
        for (int i = 0; i < 9 * 5; i++) {
            if (inv_page4.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("§8Leere Notiz")) {
                inv_page4.setItem(i, itemStack);
                note.invslot = i;
                note.page = 4;
                return;
            }
        }
        for (int i = 0; i < 9 * 5; i++) {
            if (inv_page5.getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase("§8Leere Notiz")) {
                inv_page5.setItem(i, itemStack);
                note.invslot = i;
                note.page = 5;
                return;
            }
        }

    }

    public void removeNote(int itemSlot, int page) {
        ItemStack emptyNote = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta emptyNoteMeta = emptyNote.getItemMeta();
        emptyNoteMeta.setDisplayName("§8Leere Notiz");
        emptyNote.setItemMeta(emptyNoteMeta);

        if(page == 1)
            inv_page1.setItem(itemSlot, emptyNote);
        if(page == 2)
            inv_page2.setItem(itemSlot, emptyNote);
        if(page == 3)
            inv_page3.setItem(itemSlot, emptyNote);
        if(page == 4)
            inv_page4.setItem(itemSlot, emptyNote);
        if(page == 5)
            inv_page5.setItem(itemSlot, emptyNote);
    }

    public void open(Player player, int page) {
        switch (page) {
            case 1:
                player.openInventory(inv_page1);
                break;
            case 2:
                player.openInventory(inv_page2);
                break;
            case 3:
                player.openInventory(inv_page3);
                break;
            case 4:
                player.openInventory(inv_page4);
                break;
            case 5:
                player.openInventory(inv_page5);
                break;
        }
    }

    public boolean isFull() {
        return brett.getNotes().size() >= 134;
    }

}

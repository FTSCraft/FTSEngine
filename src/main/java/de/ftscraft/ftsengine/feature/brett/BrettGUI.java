package de.ftscraft.ftsengine.feature.brett;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.ScrollingGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrettGUI {

    private final ScrollingGui gui;
    private final Brett brett;
    private final Engine plugin;

    public BrettGUI(Brett brett) {
        this.brett = brett;
        this.plugin = Engine.getInstance();
        gui = Gui.scrolling()
                .title(MiniMsg.c("<dark_red>Schwarzes-Brett " + brett.getName()))
                .rows(5)
                .pageSize(27)
                .disableAllInteractions()
                .create();
        setupGui();
    }


    private void setupGui() {
        // Pinnwand (Filler für obere und untere Zeilen)
        ItemStack pinnwand = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemMeta pinnwandMeta = pinnwand.getItemMeta();
        pinnwandMeta.setDisplayName("§7Pinnwand");
        pinnwand.setItemMeta(pinnwandMeta);

        // Create Note Button
        ItemStack create = new ItemStack(Material.PAPER, 1);
        ItemMeta createMeta = create.getItemMeta();
        createMeta.setDisplayName("§cErstelle Notiz");
        List<String> createLore = new ArrayList<>(Arrays.asList(
                "§bErstelle eine Notiz!",
                "§cAchte auf einen RPlichen Schreibstil",
                " ",
                "§7Rechtsklick für Anonym"
        ));
        createMeta.setLore(createLore);
        create.setItemMeta(createMeta);

        // Navigation: Previous Page
        ItemStack previousPage = new ItemStack(Material.ARROW);
        ItemMeta previousMeta = previousPage.getItemMeta();
        previousMeta.setDisplayName("§cVorherige Seite");
        previousPage.setItemMeta(previousMeta);

        // Navigation: Next Page
        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextPage.getItemMeta();
        nextMeta.setDisplayName("§cNächste Seite");
        nextPage.setItemMeta(nextMeta);

        // Add navigation items
        GuiItem previousPageItem = new GuiItem(previousPage);
        previousPageItem.setAction(e -> gui.previous());
        gui.setItem(5, 3, previousPageItem);

        GuiItem nextPageItem = new GuiItem(nextPage);
        nextPageItem.setAction(e -> gui.next());
        gui.setItem(5, 7, nextPageItem);

        // Add create button
        GuiItem createItem = new GuiItem(create);
        createItem.setAction(e -> {
            if (!(e.getWhoClicked() instanceof Player p)) return;

            // Check if brett is full
            if (isFull()) {
                p.sendMessage("§7[§bSchwarzes Brett§7] Es gibt keine freien Plätze mehr!");
                p.sendMessage("§7[§bSchwarzes Brett§7] Warte bis ein Platz frei ist");
                return;
            }

            // Check if player has too many notes
            int noteCount = 0;
            for (BrettNote note : brett.getNotes()) {
                if (note.getCreator().equals(p.getName())) {
                    noteCount++;
                }
            }
            if (noteCount >= 4) {
                p.sendMessage("§7[§bSchwarzes Brett§7] Du hast bereits (mehr als) 4 Notizen geschriben. Es reicht!");
                if (p.hasPermission("ftsengine.brett.admin")) {
                    p.sendMessage("§7[§bSchwarzes Brett§7] Aber du hast die Rechte also darfst du das");
                } else {
                    return;
                }
            }

            // Check economy (price is 0, but keeping for consistency)
            int price = 0;
            if (!plugin.getEcon().has(p, price)) {
                p.sendMessage(Messages.PREFIX + "Du hast nicht genug Geld!");
                return;
            }

            p.closeInventory();
            p.sendMessage(Messages.PREFIX + "Bitte achte auf einen RPlichen Schreibstil \n §7[§bSchwarzes Brett§7] §bBitte gebe jetzt den Titel ein. §c(Max. 50 Zeichen)");
            p.sendMessage(Messages.PREFIX + "Um die Erstellung abzubrechen gebe 'exit' ein!");

            BrettNote brettNote = new BrettNote(brett, p.getName(), true, e.isRightClick());
            plugin.playerBrettNote.put(p, brettNote);
        });
        gui.setItem(5, 5, createItem);

        // Fill top and bottom rows with pinnwand
        GuiItem pinnwandItem = new GuiItem(pinnwand);
        gui.getFiller().fillTop(pinnwandItem);
        gui.getFiller().fillBottom(pinnwandItem);
    }


    public void addNote(ItemStack itemStack, BrettNote note) {
        GuiItem noteItem = new GuiItem(itemStack);
        noteItem.setAction(e -> {
            if (!(e.getWhoClicked() instanceof Player p)) return;

            String note_title = note.getTitle();
            String note_cont = note.getContent();
            String note_creator = note.getCreator();

            p.sendMessage("§7**********************************");
            p.sendMessage("§6" + note_title);
            p.sendMessage(note_cont);
            p.sendMessage(" ");
            if (note.isAnonym()) {
                p.sendMessage("§7§nNotiz von Anonym");
            } else {
                p.sendMessage("§7§nNotiz von " + note_creator);
            }
            if (p.hasPermission("ftsengine.brett.delete") || note_creator.equals(p.getName())) {
                Component deleteMessage = MiniMsg.c(
                        "<click:run_command:'/ftsengine brett delete " + note.getId() + " " + brett.getName().replace(" ", "_") + "'>" +
                        "<dark_red>Löschen</dark_red>" +
                        "</click>"
                );
                p.sendMessage(deleteMessage);
            }
            p.sendMessage("§7**********************************");
        });
        gui.addItem(noteItem);
        gui.update();
    }

    public void removeNote(BrettNote note) {
        // Rebuild the GUI with all notes except the removed one
        gui.clearPageItems();
        for (BrettNote n : brett.getNotes()) {
            if (n != note) {
                GuiItem noteItem = new GuiItem(n.getItem());
                noteItem.setAction(e -> {
                    if (!(e.getWhoClicked() instanceof Player p)) return;

                    String note_title = n.getTitle();
                    String note_cont = n.getContent();
                    String note_creator = n.getCreator();

                    p.sendMessage("§7**********************************");
                    p.sendMessage("§6" + note_title);
                    p.sendMessage(note_cont);
                    p.sendMessage(" ");
                    if (n.isAnonym()) {
                        p.sendMessage("§7§nNotiz von Anonym");
                    } else {
                        p.sendMessage("§7§nNotiz von " + note_creator);
                    }
                    if (p.hasPermission("ftsengine.brett.delete") || note_creator.equals(p.getName())) {
                        Component deleteMessage = MiniMsg.c(
                                "<click:run_command:'/ftsengine brett delete " + n.getId() + " " + brett.getName().replace(" ", "_") + "'>" +
                                "<dark_red>Löschen</dark_red>" +
                                "</click>"
                        );
                        p.sendMessage(deleteMessage);
                    }
                    p.sendMessage("§7**********************************");
                });
                gui.addItem(noteItem);
            }
        }
        gui.update();
    }

    public void open(Player player) {
        gui.open(player);
    }

    public void open(Player player, int page) {
        // Für Rückwärtskompatibilität - page wird ignoriert, da ScrollingGui eigene Pagination hat
        gui.open(player);
    }

    public boolean isFull() {
        return brett.getNotes().size() >= 135; // 27 items per page * 5 pages
    }

}

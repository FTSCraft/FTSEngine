package de.ftscraft.ftsengine.feature.instruments;

import de.ftscraft.ftsutils.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class SimpleInstrument implements Instrument {

    public final static float[] notes = {.529732f, .5f, 0.561231f, .594604f, .629961f, .66742f, .707107f, .707107f,
            .749154f, .793701f, .840896f, .890899f, .943874f, .943874f, 1, 1.059463f, 1.059463f, 1f, 1.122462f,
            1.189207f, 1.259921f, 1.33484f, 1.414214f, 1.414214f, 1.498307f, 1.587401f, 1.681793f, 1.781797f,
            1.887749f, 1.887749f, 2};
    public final Sound sound;
    private final Inventory inv;

    public SimpleInstrument(Sound sound, String name) {
        this.sound = sound;
        this.inv = Bukkit.createInventory(
                this, 9 * 5, Component.text(name).color(TextColor.color(166, 227, 87)));
        fillInventory();
    }

    private void fillInventory() {

        // Array der Notennamen, passend zu den Indizes in "notes"
        String[] noteNames = {"G", "Fis/Ges", "Gis/As", "A", "B/Ais", "H", "C", "C", "Cis/Des", "D", "Dis/Es", "E",
                "F", "F", "Fis/Ges", "G", "G", "Fis/Ges", "Gis/As", "A", "B/Ais", "H", "C", "C", "Cis/Des", "D",
                "Dis/Es", "E", "F", "F", "Fis/Ges"};

        // Die Slots, in die die Items eingefügt werden sollen
        int[] slots = {9, 0, 1, 10, 2, 11, 12, 3, 4, 13, 5, 14, 15, 6, 7, 16, 36, 27, 28, 37, 29, 38, 39,
                30, 31, 40, 32, 41, 42, 33, 34};

        // Materials für weiße und schwarze Glasscheiben
        Material whitePane = Material.WHITE_STAINED_GLASS_PANE;
        Material blackPane = Material.BLACK_STAINED_GLASS_PANE;

        for (int i = 0; i < slots.length; i++) {
            // Bestimmen, ob das Item weiß oder schwarz sein soll
            Material material = noteNames[i].contains("/") ? blackPane : whitePane;

            // Item mit PDC-Wert erstellen
            ItemStack item = new ItemBuilder(material)
                    .name("&fNote " + noteNames[i])
                    .addPDC("noteindex", i, PersistentDataType.INTEGER)
                    .build();

            // Item in das Inventar setzen
            inv.setItem(slots[i], item);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

}

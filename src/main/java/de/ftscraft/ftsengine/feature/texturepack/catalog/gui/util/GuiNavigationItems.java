package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util;

import de.ftscraft.ftsutils.items.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Color;
import org.bukkit.Material;

/**
 * Factory class for commonly used GUI navigation items
 */
public class GuiNavigationItems {

    /**
     * Creates a "Back" button
     */
    public static GuiItem createBackButton() {
        return new GuiItem(new ItemBuilder(Material.ARROW).name("Zurück").build());
    }

    /**
     * Creates a "Next Page" button
     */
    public static GuiItem createNextPageButton() {
        return new GuiItem(new ItemBuilder(Material.PAPER).name("Weiter scrollen").build());
    }

    /**
     * Creates a "Previous Page" button
     */
    public static GuiItem createPreviousPageButton() {
        return new GuiItem(new ItemBuilder(Material.PAPER).name("Zurück scrollen").build());
    }

    /**
     * Creates a filler item
     */
    public static GuiItem createFiller() {
        return new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name(" ").build());
    }

    /**
     * Creates an empty filler item
     */
    public static GuiItem createEmptyFiller() {
        return new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).name("").build());
    }

    public static GuiItem createItemChooseExplainer() {
        return new GuiItem(
                new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .addCMD("gui_fragezeichen")
                        .color(Color.RED)
                        .build()
        );
    }

}

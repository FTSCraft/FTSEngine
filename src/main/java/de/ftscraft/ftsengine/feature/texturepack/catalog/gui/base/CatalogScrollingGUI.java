package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.base;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.GuiNavigationItems;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.ScrollingGui;

/**
 * Base class for scrollable catalog GUIs
 * Handles standard navigation elements
 */
public abstract class CatalogScrollingGUI extends CatalogGUI {

    protected ScrollingGui gui;
    protected final BaseGui parentGui;

    public CatalogScrollingGUI(Catalog catalog, BaseGui parentGui) {
        super(catalog);
        this.parentGui = parentGui;
    }

    /**
     * Adds standard navigation items to the scrolling GUI
     */
    protected void addNavigationItems() {
        // Next page
        GuiItem nextPageItem = GuiNavigationItems.createNextPageButton();
        nextPageItem.setAction(e -> gui.next());
        gui.setItem(6, 7, nextPageItem);

        // Previous page
        GuiItem previousPageItem = GuiNavigationItems.createPreviousPageButton();
        previousPageItem.setAction(e -> gui.previous());
        gui.setItem(6, 3, previousPageItem);

        // Back
        GuiItem backItem = GuiNavigationItems.createBackButton();
        backItem.setAction(e -> parentGui.open(e.getWhoClicked()));
        gui.setItem(6, 1, backItem);

        // Filler for bottom row
        gui.getFiller().fillBottom(GuiNavigationItems.createEmptyFiller());
    }

    @Override
    public BaseGui getGui() {
        return gui;
    }
}

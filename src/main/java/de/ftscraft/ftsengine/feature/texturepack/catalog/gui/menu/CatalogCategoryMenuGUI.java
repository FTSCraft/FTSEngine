package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.menu;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.base.CatalogGUI;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.CatalogItemStackBuilder;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.GuiNavigationItems;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogCategory;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogSubCategory;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class CatalogCategoryMenuGUI extends CatalogGUI {

    private final CatalogCategory category;
    private final Material filter;
    private final BaseGui parentGui;

    public CatalogCategoryMenuGUI(Catalog catalog, CatalogCategory category, Material filter, BaseGui parentGui) {
        super(catalog);
        this.category = category;
        this.filter = filter;
        this.parentGui = parentGui;
        initGui();
    }

    @Override
    protected void initGui() {
        gui = Gui.gui()
                .title(Component.text(category.getName()))
                .rows(6)
                .disableAllInteractions()
                .create();

        fillGui(() -> {
            addSubCategoryItems();
            addBackButton();
            gui.getFiller().fill(GuiNavigationItems.createFiller());
        });
    }

    private void addSubCategoryItems() {
        for (CatalogSubCategory subCategory : category.getSubCategories()) {
            GuiItem guiItem = new GuiItem(CatalogItemStackBuilder.generateSubCategoryItemStack(subCategory, filter));
            guiItem.setAction(event -> new CatalogSubCategoryGUI(catalog, subCategory, filter, gui).getGui().open(event.getWhoClicked()));
            gui.addItem(guiItem);
        }
    }

    private void addBackButton() {
        GuiItem backItem = GuiNavigationItems.createBackButton();
        backItem.setAction(e -> parentGui.open(e.getWhoClicked()));
        gui.setItem(6, 1, backItem);
    }
}

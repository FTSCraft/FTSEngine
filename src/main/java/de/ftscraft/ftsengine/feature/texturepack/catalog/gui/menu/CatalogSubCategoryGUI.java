package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.menu;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.base.CatalogScrollingGUI;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.CatalogItemStackBuilder;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogItem;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogSubCategory;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class CatalogSubCategoryGUI extends CatalogScrollingGUI {

    private final CatalogSubCategory category;
    private final Material filter;

    public CatalogSubCategoryGUI(Catalog catalog, CatalogSubCategory category, Material filter, BaseGui parentGui) {
        super(catalog, parentGui);
        this.category = category;
        this.filter = filter;
        initGui();
    }

    @Override
    protected void initGui() {
        gui = Gui.scrolling()
                .title(Component.text("%s".formatted(category.getName())))
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        fillGui(() -> {
            for (CatalogItem item : category.getItems()) {
                if (filter != null && !item.getMaterials().contains(filter)) {
                    continue;
                }
                GuiItem guiItem = new GuiItem(CatalogItemStackBuilder.generateItemStack(item));
                guiItem.setAction(e -> new CatalogChooseItemGUI(catalog, item, gui).getGui().open(e.getWhoClicked()));
                gui.addItem(guiItem);
            }

            addNavigationItems();
        });
    }

}

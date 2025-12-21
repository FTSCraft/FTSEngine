package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.menu;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.base.CatalogScrollingGUI;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.CatalogItemStackBuilder;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogItem;
import de.ftscraft.ftsutils.misc.MiniMsg;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;

public class CatalogAllItemsGUI extends CatalogScrollingGUI {

    private final Material filter;

    public CatalogAllItemsGUI(Catalog catalog, Material filter, BaseGui parentGui) {
        super(catalog, parentGui);
        this.filter = filter;
        initGui();
    }

    @Override
    public void initGui() {
        String title = filter == null
                ? "Alle Items"
                : "<lang:" + filter.translationKey() + "> Items";

        gui = Gui.scrolling()
                .title(MiniMsg.c(title))
                .rows(6)
                .pageSize(45)
                .disableAllInteractions()
                .create();

        fillGui(() -> {
            for (CatalogItem item : catalog.getAllItems(filter)) {
                GuiItem guiItem = new GuiItem(CatalogItemStackBuilder.generateItemStack(item));
                guiItem.setAction(e -> new CatalogChooseItemGUI(catalog, item, gui).getGui().open(e.getWhoClicked()));
                gui.addItem(guiItem);
            }

            addNavigationItems();
        });
    }
}

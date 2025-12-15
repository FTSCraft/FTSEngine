package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.menu;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.base.CatalogGUI;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.CatalogItemStackBuilder;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.GuiNavigationItems;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.TextureResetService;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogCategory;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogSubCategory;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.items.ItemBuilder;
import de.ftscraft.ftsutils.misc.MiniMsg;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static de.ftscraft.ftsutils.misc.MiniMsg.c;

/**
 * Main menu of the catalog
 */
public class CatalogMainMenuGUI extends CatalogGUI {

    private final TextureResetService textureResetService = new TextureResetService();
    private Material filter;
    private boolean filterActive = false;
    private boolean resetActive = false;

    public CatalogMainMenuGUI(Catalog catalog) {
        super(catalog);
        initGui();
    }

    protected void initGui() {
        gui = Gui.gui()
                .title(c("<blue>Der Katalog</blue>"))
                .rows(6)
                .disableAllInteractions()
                .create();

        fillGui(() -> {
            addCategoryItems();
            addControlItems();
            setupPlayerInventoryInteraction();
            gui.getFiller().fill(GuiNavigationItems.createFiller());
            gui.update();
        });
    }

    private void addCategoryItems() {
        for (CatalogCategory category : catalog.getCategories()) {
            GuiItem guiItem = new GuiItem(CatalogItemStackBuilder.generateCategoryItemStack(category));
            guiItem.setAction(e -> handleCategoryClick(category, e.getWhoClicked()));
            gui.addItem(guiItem);
        }
    }

    private void handleCategoryClick(CatalogCategory category, org.bukkit.entity.HumanEntity player) {
        if (category.getSubCategories().size() == 1) {
            CatalogSubCategory subCategory = category.getSubCategories().getFirst();
            new CatalogSubCategoryGUI(catalog, subCategory, filter, gui).getGui().open(player);
        } else {
            new CatalogCategoryMenuGUI(catalog, category, filter, gui).getGui().open(player);
        }
    }

    private void addControlItems() {
        setAllItemsItem();
        setFilterItem();
        setRemoveFilterItem();
        setResetItem();
    }

    private void setAllItemsItem() {
        GuiItem allItemsItem = new GuiItem(new ItemBuilder(Material.BOOK).name("Alle Items anzeigen").build());
        allItemsItem.setAction(e -> {
            new CatalogAllItemsGUI(catalog, filter, gui).getGui().open(e.getWhoClicked());
        });
        gui.setItem(6, 5, allItemsItem);
    }

    private void setResetItem() {
        GuiItem resetItem = new GuiItem(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).name("Item-Textur entfernen").build());
        resetItem.setAction(e -> {
            resetActive = !resetActive;
            if (!resetActive) {
                e.getWhoClicked().sendMessage(c(Messages.MINI_PREFIX + "Vorgang abgebrochen."));
                return;
            }
            e.getWhoClicked().sendMessage(c(Messages.MINI_PREFIX + "Klicke jetzt auf ein Item im Inventar um die Textur zu entfernen oder nochmal auf diesen Knopf um den Vorgang abzubrechen."));
        });
        gui.setItem(6, 1, resetItem);
    }

    private void setFilterItem() {
        GuiItem filterItem = new GuiItem(new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).name("Filter hinzufügen").build());
        filterItem.setAction(e -> {
            filterActive = !filterActive;
            e.getWhoClicked().sendMessage(c(Messages.MINI_PREFIX + "Klicke jetzt auf ein Item in deinem Inventar um es als Filter zu nutzen oder nochmal auf diesen Knopf um den Vorgang abzubrechen."));
        });
        gui.setItem(6, 9, filterItem);
    }

    private void setRemoveFilterItem() {
        GuiItem removeFilterItem = new GuiItem(new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).name("Filter entfernen").build());
        removeFilterItem.setAction(e -> {
            this.filter = null;
            e.getWhoClicked().sendMessage(c(Messages.MINI_PREFIX + "Filter entfernt"));
        });
        gui.setItem(6, 8, removeFilterItem);
    }

    private void setupPlayerInventoryInteraction() {
        gui.setPlayerInventoryAction(e -> {
            if (e.getCurrentItem() == null) {
                return;
            }

            Material clickedMaterial = e.getCurrentItem().getType();
            if (clickedMaterial == Material.AIR) {
                return;
            }

            Player player = (Player) e.getWhoClicked();

            if (filterActive) {
                handleFilterSelection(clickedMaterial, player);
            } else if (resetActive) {
                handleTextureReset(e.getCurrentItem(), player);
            }
        });
    }

    private void handleFilterSelection(Material material, Player player) {
        this.filter = material;
        this.filterActive = false;
        player.sendMessage(c(Messages.MINI_PREFIX + "Filter gesetzt auf: " + material.name()));
    }

    private void handleTextureReset(org.bukkit.inventory.ItemStack item, Player player) {
        textureResetService.removeTexture(item);
        this.resetActive = false;
        player.sendMessage(c(Messages.MINI_PREFIX + "Textur vom Item entfernt."));
    }
}


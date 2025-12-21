package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.menu;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.base.CatalogGUI;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.CatalogItemStackBuilder;
import de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util.GuiNavigationItems;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogItem;
import de.ftscraft.ftsutils.items.ItemBuilder;
import de.ftscraft.ftsutils.misc.MiniMsg;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CatalogChooseItemGUI extends CatalogGUI {

    private final CatalogItem catalogItem;
    private final BaseGui parentGui;

    public CatalogChooseItemGUI(Catalog catalog, CatalogItem catalogItem, BaseGui parentGui) {
        super(catalog);
        this.catalogItem = catalogItem;
        this.parentGui = parentGui;
        initGui();
    }

    @Override
    public void initGui() {
        gui = Gui.gui()
                .title(Component.text("Bitte klick auf das Item in deinem Inventar auf das das Item angewendet werden soll"))
                .disableAllInteractions()
                .rows(3)
                .create();

        fillGui(() -> {
            displayChosenItem();
            addBackButton();
            setupPlayerInventoryInteraction();
            gui.getFiller().fill(GuiNavigationItems.createFiller());
        });
    }

    private void displayChosenItem() {
        GuiItem choosedItem = new GuiItem(CatalogItemStackBuilder.generateItemStack(catalogItem));
        gui.setItem(2, 5, choosedItem);
    }

    private void addBackButton() {
        GuiItem backItem = GuiNavigationItems.createBackButton();
        backItem.setAction(e -> parentGui.open(e.getWhoClicked()));
        gui.setItem(3, 1, backItem);
    }

    private void setupPlayerInventoryInteraction() {
        gui.setPlayerInventoryAction(event -> {
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null || currentItem.getType() == Material.AIR) {
                return;
            }

            if (!catalogItem.getMaterials().contains(currentItem.getType())) {
                Player player = (Player) event.getWhoClicked();
                MiniMsg.msg(player, "<red>Die Textur ist für diesen Gegenstand nicht anwendbar!</red>");
                return;
            }

            applyTexture(currentItem);
            Player player = (Player) event.getWhoClicked();
            MiniMsg.msg(player, "<green>Die Textur wurde erfolgreich auf das Item angewendet!</green>");
        });
    }

    private void applyTexture(ItemStack item) {
        new ItemBuilder(item).addCMD(catalogItem.getCmd()).build();
    }
}

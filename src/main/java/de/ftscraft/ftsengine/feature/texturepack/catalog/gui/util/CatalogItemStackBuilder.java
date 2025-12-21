package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util;

import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogCategory;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogItem;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogSubCategory;
import de.ftscraft.ftsutils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

/**
 * Builder class for catalog-specific ItemStacks
 */
public class CatalogItemStackBuilder {

    public static ItemStack generateItemStack(CatalogItem catalogItem) {
        var ib = new ItemBuilder(catalogItem.getMaterials().getFirst())
                .addCMD(catalogItem.getCmd())
                .mmLore("<gray>Für folgende Items anwendbar:</gray>")
                .name(catalogItem.getName());
        catalogItem.getMaterials().forEach(m -> ib.mmLore("<gray> - <lang:%s></gray>".formatted(m.translationKey())));
        return ib.build();
    }

    public static ItemStack generateCategoryItemStack(CatalogCategory category) {
        return new ItemBuilder(category.getMaterial())
                .name(category.getName())
                .sign("GUI_CATEGORY_ITEM")
                .mmLore(category.getDescription().split("<newline>"))
                .addPDC("category_name", category.getName(), PersistentDataType.STRING)
                .build();
    }

    public static ItemStack generateSubCategoryItemStack(CatalogSubCategory category, Material filter) {
        String filterText = filter != null ? "<lang:" + filter.translationKey() + ">" : "Kein Filter";
        long itemCount = category.getItems().stream()
                .filter(item -> filter == null || item.getMaterials().contains(filter))
                .count();

        return new ItemBuilder(category.getMaterial())
                .name(category.getName())
                .sign("GUI_SUBCATEGORY_ITEM")
                .mmLore("<gray>Filter: " + filterText)
                .mmLore("<gray>Verfügbare Items: " + itemCount)
                .mmLore(category.getDescription().split("<newline>"))
                .addPDC("subcategory_name", category.getName(), PersistentDataType.STRING)
                .build();
    }

}

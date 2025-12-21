package de.ftscraft.ftsengine.feature.texturepack.catalog.storage;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class CatalogSubCategory {

    private String name = "Einhandschwerter";
    private String description = "Einhandschwerter für den Kampf";
    private Material material = Material.DIAMOND_SWORD;
    private List<CatalogItem> items = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public List<CatalogItem> getItems() {
        return items;
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("CatalogSubCategory name must not be null or blank");
        }
        if (description == null) {
            throw new IllegalStateException("CatalogSubCategory %s description must not be null".formatted(name));
        }
        if (material == null) {
            throw new IllegalStateException("CatalogSubCategory %s material must not be null".formatted(name));
        }
        if (items == null) {
            throw new IllegalStateException("CatalogSubCategory %s items must not be null".formatted(name));
        }
        // validate items
        items.forEach(CatalogItem::validate);
    }
}

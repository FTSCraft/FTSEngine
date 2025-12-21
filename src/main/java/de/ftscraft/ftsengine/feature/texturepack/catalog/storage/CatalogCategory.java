package de.ftscraft.ftsengine.feature.texturepack.catalog.storage;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class CatalogCategory {

    private String name = "Schwerter";
    private String description = "Schwerter für den Kampf";
    private Material material = Material.DIAMOND_SWORD;
    private List<CatalogSubCategory> sub_categories = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<CatalogSubCategory> getSubCategories() {
        return sub_categories;
    }

    public Material getMaterial() {
        return material;
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("CatalogCategory name must not be null or blank");
        }
        if (description == null) {
            throw new IllegalStateException("CatalogCategory %s description must not be null".formatted(name));
        }
        if (material == null) {
            throw new IllegalStateException("CatalogCategory %s material must not be null".formatted(name));
        }
        if (sub_categories == null) {
            throw new IllegalStateException("CatalogCategory %s sub_categories must not be null".formatted(name));
        }
        // validate sub-categories
        sub_categories.forEach(CatalogSubCategory::validate);
    }
}

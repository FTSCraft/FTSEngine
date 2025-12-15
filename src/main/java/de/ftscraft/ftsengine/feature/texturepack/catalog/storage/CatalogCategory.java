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
}

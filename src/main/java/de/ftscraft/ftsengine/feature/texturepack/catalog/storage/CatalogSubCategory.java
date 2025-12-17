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
}

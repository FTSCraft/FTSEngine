package de.ftscraft.ftsengine.feature.texturepack.catalog.storage;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class CatalogItem {

    private String name = "Beispiel Item";
    private String cmd = "example";
    private List<Material> materials = new ArrayList<>();

    public String getName() {
        return name;
    }

    public String getCmd() {
        return cmd;
    }

    public List<Material> getMaterials() {
        return materials;
    }
}

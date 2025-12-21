package de.ftscraft.ftsengine.feature.texturepack.catalog.storage;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
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

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("CatalogItem name must not be null or blank");
        }
        if (cmd == null || cmd.isBlank()) {
            throw new IllegalStateException("CatalogItem %s cmd must not be null or blank".formatted(name));
        }
        if (materials == null || materials.isEmpty()) {
            throw new IllegalStateException("CatalogItem %s materials must not be null or empty".formatted(name));
        }
        // check if any material is null
        materials.forEach(m -> {
            if (m == null) {
                throw new IllegalStateException("CatalogItem %s materials must not contain null values".formatted(name));
            }
        });
    }
}

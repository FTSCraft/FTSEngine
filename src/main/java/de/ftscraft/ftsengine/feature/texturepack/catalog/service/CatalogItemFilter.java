package de.ftscraft.ftsengine.feature.texturepack.catalog.service;

import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogItem;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Responsible for filtering and paginating CatalogItems
 */
public class CatalogItemFilter {


    /**
     * Filters items by Material
     */
    public static List<CatalogItem> filterByMaterial(List<CatalogItem> items, Material material) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        if (material == null) {
            return items;
        }

        List<CatalogItem> filtered = items.stream()
                .filter(Objects::nonNull)
                .filter(item -> matchesMaterial(item, material))
                .toList();

        return filtered.isEmpty() ? Collections.emptyList() : filtered;
    }

    /**
     * Checks if a CatalogItem contains the given Material
     */
    public static boolean matchesMaterial(CatalogItem item, Material material) {
        if (item == null || material == null) {
            return false;
        }

        List<Material> materials;
        try {
            materials = item.getMaterials();
        } catch (RuntimeException e) {
            return false;
        }

        if (materials == null || materials.isEmpty()) {
            return false;
        }

        return materials.stream().anyMatch(m -> m != null && m == material);
    }
}

package de.ftscraft.ftsengine.feature.texturepack.catalog.service;

import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogCategory;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogStorage;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogSubCategory;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Responsible for searching categories and subcategories
 */
public class CatalogSearchService {

    private final CatalogStorage storage;

    public CatalogSearchService(CatalogStorage storage) {
        this.storage = storage;
    }

    /**
     * Finds a category by name (case-insensitive)
     */
    public Optional<CatalogCategory> findCategory(String categoryName) {
        if (categoryName == null) {
            return Optional.empty();
        }

        List<CatalogCategory> categories = storage.getCategories();
        if (categories == null) {
            return Optional.empty();
        }

        String normalizedName = categoryName.toLowerCase(Locale.ROOT);
        return categories.stream()
                .filter(c -> c != null && c.getName() != null)
                .filter(c -> c.getName().toLowerCase(Locale.ROOT).equals(normalizedName))
                .findFirst();
    }

    /**
     * Finds a subcategory inside a category (case-insensitive)
     */
    public Optional<CatalogSubCategory> findSubCategory(String categoryName, String subCategoryName) {
        if (categoryName == null || subCategoryName == null) {
            return Optional.empty();
        }

        Optional<CatalogCategory> category = findCategory(categoryName);
        if (category.isEmpty()) {
            return Optional.empty();
        }

        List<CatalogSubCategory> subCategories = category.get().getSubCategories();
        if (subCategories == null) {
            return Optional.empty();
        }

        String normalizedName = subCategoryName.toLowerCase(Locale.ROOT);
        return subCategories.stream()
                .filter(s -> s != null && s.getName() != null)
                .filter(s -> s.getName().toLowerCase(Locale.ROOT).equals(normalizedName))
                .findFirst();
    }
}

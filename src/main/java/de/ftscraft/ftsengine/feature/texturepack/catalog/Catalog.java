package de.ftscraft.ftsengine.feature.texturepack.catalog;

import de.ftscraft.ftsengine.feature.texturepack.catalog.service.CatalogItemFilter;
import de.ftscraft.ftsengine.feature.texturepack.catalog.service.CatalogSearchService;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogCategory;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogItem;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogStorage;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogSubCategory;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Catalog {

    private final CatalogStorage storage;
    private final CatalogSearchService searchService;

    // Caches
    private volatile List<CatalogCategory> cachedCategories = null;
    private final ConcurrentMap<String, List<CatalogSubCategory>> subCategoryCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, List<CatalogItem>> itemsCache = new ConcurrentHashMap<>();

    public Catalog(CatalogStorage storage) {
        this.storage = Objects.requireNonNull(storage, "storage must not be null");
        this.searchService = new CatalogSearchService(storage);
    }

    /**
     * Returns all categories from the storage as an unmodifiable list.
     */
    public List<CatalogCategory> getCategories() {
        List<CatalogCategory> cats = cachedCategories;
        if (cats == null) {
            synchronized (this) {
                cats = cachedCategories;
                if (cats == null) {
                    List<CatalogCategory> fromStorage = storage.getCategories();
                    cats = fromStorage == null ? Collections.emptyList() : Collections.unmodifiableList(fromStorage);
                    cachedCategories = cats;
                }
            }
        }
        return cats;
    }

    /**
     * Returns all sub-categories for the given category name.
     */
    public List<CatalogSubCategory> getSubCategories(String categoryName) {
        Objects.requireNonNull(categoryName, "categoryName must not be null");
        String key = categoryName.toLowerCase(Locale.ROOT);
        return subCategoryCache.computeIfAbsent(key, k -> {
            var cat = searchService.findCategory(categoryName);
            if (cat.isEmpty()) {
                return Collections.emptyList();
            }
            List<CatalogSubCategory> subs = cat.get().getSubCategories();
            return subs == null ? Collections.emptyList() : Collections.unmodifiableList(subs);
        });
    }

    /**
     * Returns items from the given category/subCategory as an unmodifiable list.
     */
    public List<CatalogItem> getItems(String categoryName, String subCategoryName) {
        Objects.requireNonNull(categoryName, "categoryName must not be null");
        Objects.requireNonNull(subCategoryName, "subCategoryName must not be null");

        String key = categoryName.toLowerCase(Locale.ROOT) + "::" + subCategoryName.toLowerCase(Locale.ROOT);
        return itemsCache.computeIfAbsent(key, k -> {
            var sub = searchService.findSubCategory(categoryName, subCategoryName);
            if (sub.isEmpty()) {
                return Collections.emptyList();
            }
            List<CatalogItem> items = sub.get().getItems();
            return items == null || items.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(items);
        });
    }

    /**
     * Returns all items for the given subCategory as an unmodifiable list.
     */
    public List<CatalogItem> getItems(CatalogSubCategory subCategory) {
        Objects.requireNonNull(subCategory, "subCategory must not be null");
        List<CatalogItem> items = subCategory.getItems();
        return items == null || items.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(items);
    }

    /**
     * Returns all items from all categories and subcategories in the catalog.
     */
    public List<CatalogItem> getAllItems() {
        return getAllItems(null);
    }

    /**
     * Returns all items from all categories and subcategories in the catalog,
     * optionally filtered by the provided Material.
     */
    public List<CatalogItem> getAllItems(Material material) {
        List<CatalogCategory> cats = getCategories();
        if (cats.isEmpty()) {
            return Collections.emptyList();
        }

        List<CatalogItem> allItems = cats.stream()
                .filter(Objects::nonNull)
                .flatMap(c -> getSubCategories(c.getName()).stream()
                        .filter(Objects::nonNull)
                        .flatMap(sc -> getItems(c.getName(), sc.getName()).stream()))
                .filter(Objects::nonNull)
                .toList();

        return CatalogItemFilter.filterByMaterial(allItems, material);
    }

    /**
     * Returns items in a specific subCategory that match the given Material.
     */
    public List<CatalogItem> getItemsByMaterial(String categoryName, String subCategoryName, Material material) {
        Objects.requireNonNull(material, "material must not be null");
        List<CatalogItem> items = getItems(categoryName, subCategoryName);
        return CatalogItemFilter.filterByMaterial(items, material);
    }

    /**
     * Returns items in a specific category (all subCategories) that match the given Material.
     */
    public List<CatalogItem> getItemsByMaterial(String categoryName, Material material) {
        Objects.requireNonNull(categoryName, "categoryName must not be null");
        Objects.requireNonNull(material, "material must not be null");

        List<CatalogSubCategory> subs = getSubCategories(categoryName);
        if (subs.isEmpty()) {
            return Collections.emptyList();
        }

        List<CatalogItem> allItems = subs.stream()
                .filter(Objects::nonNull)
                .flatMap(sc -> getItems(categoryName, sc.getName()).stream())
                .filter(Objects::nonNull)
                .toList();

        return CatalogItemFilter.filterByMaterial(allItems, material);
    }

    /**
     * Returns all items in the whole catalog that match the given Material.
     */
    public List<CatalogItem> getItemsByMaterial(Material material) {
        Objects.requireNonNull(material, "material must not be null");
        return getAllItems(material);
    }

    /**
     * Returns the number of items in the given category/subCategory.
     */
    public int countItems(String categoryName, String subCategoryName) {
        return getItems(categoryName, subCategoryName).size();
    }

    /**
     * Clears all caches. Call this if the underlying storage changed and you need fresh data.
     */
    public void clearCache() {
        cachedCategories = null;
        subCategoryCache.clear();
        itemsCache.clear();
    }

}

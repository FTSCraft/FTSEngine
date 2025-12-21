package de.ftscraft.ftsengine.feature.texturepack.catalog.storage;

import de.ftscraft.ftsutils.storage.Storage;
import de.ftscraft.ftsutils.storage.StorageType;

import java.util.ArrayList;
import java.util.List;

@Storage(type = StorageType.JSON, path = "texturepack", name = "catalog", config = true, editable = false)
public class CatalogStorage {

    private final List<CatalogCategory> categories = new ArrayList<>();

    public List<CatalogCategory> getCategories() {
        return categories;
    }

    public void validate() {
        for (CatalogCategory category : categories) {
            category.validate();
        }
    }
}

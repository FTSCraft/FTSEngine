package de.ftscraft.ftsengine.feature.texturepack.catalog.handler;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.storage.CatalogStorage;
import de.ftscraft.ftsutils.storage.DataHandler;

/**
 * Handler for initializing and managing the catalog
 */
public class CatalogHandler {

    private Catalog catalog;

    /**
     * Initializes and loads the Catalog storage
     *
     * @param dataHandler The DataHandler for storage operations
     */
    public void initAndLoadCatalogStorage(DataHandler dataHandler) {
        if (dataHandler == null) {
            throw new IllegalArgumentException("dataHandler must not be null");
        }

        dataHandler.registerClass(CatalogStorage.class);
        dataHandler.loadStorages(CatalogStorage.class);

        CatalogStorage catalogStore = dataHandler.get(CatalogStorage.class);
        if (catalogStore != null) {
            this.catalog = new Catalog(catalogStore);
        }
    }

    /**
     * Returns the Catalog
     *
     * @return The Catalog or null if not yet initialized
     */
    public Catalog getCatalog() {
        return catalog;
    }

}

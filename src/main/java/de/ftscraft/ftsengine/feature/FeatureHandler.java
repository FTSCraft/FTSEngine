package de.ftscraft.ftsengine.feature;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.feature.texturepack.catalog.handler.CatalogHandler;
import de.ftscraft.ftsengine.main.Engine;

import java.util.logging.Level;

public class FeatureHandler {

    private CatalogHandler catalogHandler;
    private final Engine engine;

    public FeatureHandler(Engine engine) {
        this.engine = engine;
        initFeatures();
    }

    private void initFeatures() {
        initCatalogFeature();
    }

    private void initCatalogFeature() {
        this.catalogHandler = new CatalogHandler();
        engine.getLogger().log(Level.INFO, "Catalog Feature initialized.");
        catalogHandler.initAndLoadCatalogStorage(Engine.getInstance().getStorage());
    }

    public Catalog getCatalog() {
        return catalogHandler.getCatalog();
    }

    public static FeatureHandler getInstance() {
        return Engine.getInstance().getFeatureHandler();
    }

}

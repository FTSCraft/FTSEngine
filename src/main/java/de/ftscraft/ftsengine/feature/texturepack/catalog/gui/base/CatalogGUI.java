package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.base;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import dev.triumphteam.gui.guis.BaseGui;

import java.util.concurrent.CompletableFuture;

public abstract class CatalogGUI {

    protected Catalog catalog;
    protected BaseGui gui;

    public CatalogGUI(Catalog catalog) {
        this.catalog = catalog;
    }

    protected abstract void initGui();

    public BaseGui getGui() {
        return gui;
    }

    protected void fillGui(Runnable runnable) {
        CompletableFuture.runAsync(runnable).thenRun(() -> getGui().update());
    }

}

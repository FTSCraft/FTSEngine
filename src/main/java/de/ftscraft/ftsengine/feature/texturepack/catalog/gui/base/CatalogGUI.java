package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.base;

import de.ftscraft.ftsengine.feature.texturepack.catalog.Catalog;
import de.ftscraft.ftsengine.main.Engine;
import dev.triumphteam.gui.guis.BaseGui;
import org.bukkit.Bukkit;

public abstract class CatalogGUI implements ICatalogGUI {

    protected Catalog catalog;
    protected BaseGui gui;

    public CatalogGUI(Catalog catalog) {
        this.catalog = catalog;
    }

    public abstract void initGui();

    public BaseGui getGui() {
        return gui;
    }

    public void fillGui(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Engine.getInstance(), () -> {
            runnable.run();
            getGui().update();
        });
    }

}

package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.feature.time.CalendarStorage;
import de.ftscraft.ftsengine.utils.storage.DatabaseAuthStorage;
import de.ftscraft.ftsutils.storage.Storage;
import de.ftscraft.ftsutils.storage.StorageType;

@Storage(name = "config", config = true, type = StorageType.JSON)
public class EngineConfig {

    public CalendarStorage calendar = new CalendarStorage();
    public DatabaseAuthStorage databaseAuth = new DatabaseAuthStorage();

    public String weatherCooldown;

}

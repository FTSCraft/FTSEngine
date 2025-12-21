package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.feature.time.CalendarStorage;
import de.ftscraft.ftsengine.utils.storage.DatabaseAuthStorage;
import de.ftscraft.ftsutils.storage.Storage;
import de.ftscraft.ftsutils.storage.StorageType;

@Storage(name = "config", config = true, type = StorageType.JSON)
public class EngineConfig {

    private CalendarStorage calendar = new CalendarStorage();
    private DatabaseAuthStorage databaseAuth = new DatabaseAuthStorage();

    private String weatherCooldown;

    public CalendarStorage getCalendar() {
        return calendar;
    }

    public DatabaseAuthStorage getDatabaseAuth() {
        return databaseAuth;
    }

    public String getWeatherCooldown() {
        return weatherCooldown;
    }

    public void setWeatherCooldown(String weatherCooldown) {
        this.weatherCooldown = weatherCooldown;
    }
}

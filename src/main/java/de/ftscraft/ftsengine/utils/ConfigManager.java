package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.feature.time.TimeManager;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    private static FileConfiguration config;
    private static final Map<String, Object> cache = new HashMap<>();

    public ConfigManager() {
        Engine.getInstance().saveResource("config.yml", false);
        config = Engine.getInstance().getConfig();
    }

    private static Object getObjectValue(String path) {
        if (cache.containsKey(path))
            return cache.get(path);
        if (!config.contains(path))
            throw new IllegalArgumentException("config does not contain " + path);
        Object val = config.get(path);
        cache.put(path, val);
        return val;
    }

    private void setValueAndSave(String path, Object obj) {
        setValue(path, obj);
        Engine.getInstance().saveConfig();
    }

    private void setValue(String path, Object obj) {
        config.set(path, obj);
        cache.put(path, obj);
    }

    public static <T> T getValue(String path) {
        return (T) getObjectValue(path);
    }

    public int getCalendarYear() {
        return (int) getObjectValue("cal.year");
    }

    public int getCalendarMonth() {
        return (int) getObjectValue("cal.month");
    }

    public int getCalendarDay() {
        return (int) getObjectValue("cal.day");
    }

    public int getCalendarHour() {
        return (int) getObjectValue("cal.hour");
    }

    public int getCalendarMinute() {
        return (int) getObjectValue("cal.minute");
    }

    public List<String> getTimeWorlds() {
        return (List<String>) getObjectValue("cal.worlds");
    }

    public void save() {
        saveCalendar();
    }

    public void invalidateCache() {
        cache.clear();
    }

    private void saveCalendar() {
        Calendar calendar = TimeManager.getCalendar();
        setCalendarYear(calendar.get(Calendar.YEAR));
        setCalendarMonth(calendar.get(Calendar.MONTH));
        setCalendarDay(calendar.get(Calendar.DAY_OF_MONTH));
        setCalendarHour(calendar.get(Calendar.HOUR_OF_DAY));
        setCalendarMinute(calendar.get(Calendar.MINUTE));
    }

    public void setCalendarYear(int year) {
        setValueAndSave("cal.year", year);
    }

    public void setCalendarMonth(int month) {
        setValueAndSave("cal.month", month);
    }

    public void setCalendarDay(int day) {
        setValueAndSave("cal.day", day);
    }

    public void setCalendarHour(int hour) {
        setValueAndSave("cal.hour", hour);
    }

    public void setCalendarMinute(int minute) {
        setValueAndSave("cal.minute", minute);
    }


}

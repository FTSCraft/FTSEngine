package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.time.TimeManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    FileConfiguration config;
    private final Map<String, Object> cache = new HashMap<>();

    public ConfigManager() {
        Engine.getInstance().saveResource("config.yml", false);
        config = Engine.getInstance().getConfig();
    }

    private Object getValue(String path) {
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

    public int getCalendarYear() {
        return (int) getValue("cal.year");
    }

    public int getCalendarMonth() {
        return (int) getValue("cal.month");
    }

    public int getCalendarDay() {
        return (int) getValue("cal.day");
    }

    public int getCalendarHour() {
        return (int) getValue("cal.hour");
    }

    public int getCalendarMinute() {
        return (int) getValue("cal.minute");
    }

    public void save() {
        saveCalendar();
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

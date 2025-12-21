package de.ftscraft.ftsengine.feature.time;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.EngineConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class TimeManager {

    private static GregorianCalendar calendar;
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM - HH:mm");

    public static void init() {
        EngineConfig config = Engine.getEngineConfig();
        CalendarStorage calendarStorage = config.getCalendar();
        //noinspection MagicConstant
        calendar = new GregorianCalendar(calendarStorage.year, calendarStorage.month, calendarStorage.day, calendarStorage.hour, calendarStorage.minute);

        Bukkit.getScheduler().runTaskTimer(Engine.getInstance(), TimeManager::incrementTime, 0, 40);
    }

    private static void incrementTime() {

        calendar.add(Calendar.SECOND, 8);

        Engine.getEngineConfig().getCalendar().worlds
                .stream()
                .map(Bukkit::getWorld)
                .filter(Objects::nonNull)
                .forEach(TimeManager::setTimeForWorld);

    }

    public static GregorianCalendar getCalendar() {
        return (GregorianCalendar) calendar.clone();
    }

    public static String getFormattedTime() {
        return format.format(getCalendar().getTime());
    }

    public static void setTimeForWorld(World world) {
        world.setTime(calcMinecraftTime());
    }

    public static int calcMinecraftTime() {
        return (int) (calendar.get(Calendar.HOUR_OF_DAY) * 1000 - 6000 + calendar.get(Calendar.MINUTE) * 16.6);
    }

}

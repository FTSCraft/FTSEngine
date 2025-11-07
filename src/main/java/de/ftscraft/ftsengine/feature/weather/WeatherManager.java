package de.ftscraft.ftsengine.feature.weather;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Date;

public class WeatherManager implements Listener {
    private static final WeatherManager instance = new WeatherManager();
    private Date latestWeatherChange;

    public static WeatherManager init() {
        Bukkit.getPluginManager().registerEvents(instance, Engine.getInstance());
        return instance;
    }


    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        String weatherCooldownSpec = Engine.getConfigManager().getWeatherCooldownSpec();
        if(latestWeatherChange == null || hasTimePassed(latestWeatherChange, weatherCooldownSpec)
            || !event.getCause().equals(WeatherChangeEvent.Cause.NATURAL) || !event.getCause().equals(WeatherChangeEvent.Cause.SLEEP)) {
            latestWeatherChange = new Date();
            return;
        }
        event.setCancelled(true);
    }


    private boolean hasTimePassed(Date baseTime, String timeSpec) {
        long durationMillis = parseDurationToMillis(timeSpec);
        long targetTime = baseTime.getTime() + durationMillis;
        return System.currentTimeMillis() >= targetTime;
    }

    private long parseDurationToMillis(String timeSpec) {
        if (timeSpec == null || timeSpec.length() < 2)
            throw new IllegalArgumentException("Invalid time format: " + timeSpec);

        char unit = Character.toLowerCase(timeSpec.charAt(timeSpec.length() - 1));
        String valueStr = timeSpec.substring(0, timeSpec.length() - 1);

        long value;
        try {
            value = Long.parseLong(valueStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number in time format: " + timeSpec);
        }

        return switch (unit) {
            case 's' -> value * 1000L;
            case 'm' -> value * 60_000L;
            case 'h' -> value * 3_600_000L;
            default -> throw new IllegalArgumentException("Unknown time unit: " + unit);
        };
    }



}

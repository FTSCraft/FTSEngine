package de.ftscraft.ftsengine.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UUIDFetcher
{

    public static String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public static UUID getUUID(String player) {
        return Bukkit.getOfflinePlayer(player).getUniqueId();
    }

    public static UUID getUUID(Player player) {
        return player.getUniqueId();
    }

}

package de.ftscraft.ftsengine.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UUIDFetcher
{

    public String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public UUID getUUID(String player) {
        return Bukkit.getOfflinePlayer(player).getUniqueId();
    }

    public UUID getUUID(Player player) {
        return player.getUniqueId();
    }

}

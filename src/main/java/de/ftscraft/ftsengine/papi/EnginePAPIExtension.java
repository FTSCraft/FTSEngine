package de.ftscraft.ftsengine.papi;

import de.ftscraft.ftsengine.main.Engine;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnginePAPIExtension extends PlaceholderExpansion {


    @Override
    public @NotNull String getIdentifier() {
        return "engine";
    }

    @Override
    public @NotNull String getAuthor() {
        return "FTS-Dev Team";
    }

    @Override
    public @NotNull String getVersion() {
        return Engine.getInstance().getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equals("ausweis_vorname")) {

        }
        return super.onRequest(player, params);
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return super.onPlaceholderRequest(player, params);
    }
}

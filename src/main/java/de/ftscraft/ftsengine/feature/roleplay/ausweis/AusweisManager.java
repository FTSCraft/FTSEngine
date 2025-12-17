package de.ftscraft.ftsengine.feature.roleplay.ausweis;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.EngineUser;
import de.ftscraft.ftsengine.utils.Ausweis;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AusweisManager {

    private final Engine plugin;

    public AusweisManager(Engine plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the active Ausweis of a player
     * @param player The player
     * @return The active Ausweis or null
     */
    public Ausweis getAusweis(Player player) {
        return getAusweis(player.getUniqueId());
    }

    /**
     * Returns the active Ausweis of a player by UUID
     * @param uuid The player's UUID
     * @return The active Ausweis or null
     */
    public Ausweis getAusweis(UUID uuid) {
        if (plugin.getDatabaseHandler() == null || !plugin.getDatabaseHandler().isInitialized()) {
            return null;
        }
        // Find the player and get the active Ausweis from EngineUser
        EngineUser user = plugin.getPlayer().get(uuid);
        if (user != null) {
            return user.getActiveAusweis();
        }
        // If player is not online, load from DB
        user = plugin.getDatabaseHandler().getUserStorageManager().getUser(uuid);
        if (user != null) {
            return user.getActiveAusweis();
        }
        return null;
    }

    /**
     * Checks whether a player has an active Ausweis
     * @param player The player
     * @return true if the player has an Ausweis
     */
    public boolean hasAusweis(Player player) {
        return getAusweis(player) != null;
    }

    /**
     * Checks whether a player (by UUID) has an active Ausweis
     * @param uuid The player's UUID
     * @return true if the player has an Ausweis
     */
    public boolean hasAusweis(UUID uuid) {
        return getAusweis(uuid) != null;
    }

    /**
     * Adds a new Ausweis and sets it as the active Ausweis
     * @param ausweis The new Ausweis
     */
    public void addAusweis(Ausweis ausweis) {
        // Save to database
        if (plugin.getDatabaseHandler() != null && plugin.getDatabaseHandler().isInitialized()) {
            plugin.getDatabaseHandler().getAusweisStorageManager().saveAusweis(ausweis);

            // Set as active Ausweis for the user
            EngineUser user = plugin.getPlayer().get(ausweis.getUuid());

            // If user is not online, load from DB
            if (user == null) {
                user = plugin.getDatabaseHandler().getUserStorageManager().getOrCreateUser(ausweis.getUuid());
            }

            if (user != null) {
                user.setActiveAusweis(ausweis);
                plugin.getDatabaseHandler().getUserStorageManager().saveUser(user);
            }
        }
    }

    /**
     * Saves an Ausweis to the database
     * @param ausweis The Ausweis to save
     */
    public void saveAusweis(Ausweis ausweis) {
        if (plugin.getDatabaseHandler() != null && plugin.getDatabaseHandler().isInitialized()) {
            plugin.getDatabaseHandler().getAusweisStorageManager().saveAusweis(ausweis);
        }
    }

}

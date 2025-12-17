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
     * Gibt den aktiven Ausweis eines Spielers zurück
     * @param player Der Spieler
     * @return Der aktive Ausweis oder null
     */
    public Ausweis getAusweis(Player player) {
        return getAusweis(player.getUniqueId());
    }

    /**
     * Gibt den aktiven Ausweis eines Spielers anhand der UUID zurück
     * @param uuid Die UUID des Spielers
     * @return Der aktive Ausweis oder null
     */
    public Ausweis getAusweis(UUID uuid) {
        if (plugin.getDatabaseHandler() == null || !plugin.getDatabaseHandler().isInitialized()) {
            return null;
        }
        // Suche den Player und hole den aktiven Ausweis aus EngineUser
        EngineUser user = plugin.getPlayer().get(uuid);
        if (user != null) {
            return user.getActiveAusweis();
        }
        // Falls Player nicht online ist, aus DB laden
        user = plugin.getDatabaseHandler().getUserStorageManager().getUser(uuid);
        if (user != null) {
            return user.getActiveAusweis();
        }
        return null;
    }

    /**
     * Prüft, ob ein Spieler einen aktiven Ausweis hat
     * @param player Der Spieler
     * @return true wenn der Spieler einen Ausweis hat
     */
    public boolean hasAusweis(Player player) {
        return getAusweis(player) != null;
    }

    /**
     * Prüft, ob ein Spieler anhand der UUID einen aktiven Ausweis hat
     * @param uuid Die UUID des Spielers
     * @return true wenn der Spieler einen Ausweis hat
     */
    public boolean hasAusweis(UUID uuid) {
        return getAusweis(uuid) != null;
    }

    /**
     * Fügt einen neuen Ausweis hinzu und setzt ihn als aktiven Ausweis
     * @param ausweis Der neue Ausweis
     */
    public void addAusweis(Ausweis ausweis) {
        // In Datenbank speichern
        if (plugin.getDatabaseHandler() != null && plugin.getDatabaseHandler().isInitialized()) {
            plugin.getDatabaseHandler().getAusweisStorageManager().saveAusweis(ausweis);

            // Als aktiven Ausweis für den User setzen
            EngineUser user = plugin.getPlayer().get(ausweis.getUuid());

            // Falls User nicht online ist, aus DB laden
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
     * Speichert einen Ausweis in der Datenbank
     * @param ausweis Der zu speichernde Ausweis
     */
    public void saveAusweis(Ausweis ausweis) {
        if (plugin.getDatabaseHandler() != null && plugin.getDatabaseHandler().isInitialized()) {
            plugin.getDatabaseHandler().getAusweisStorageManager().saveAusweis(ausweis);
        }
    }

}

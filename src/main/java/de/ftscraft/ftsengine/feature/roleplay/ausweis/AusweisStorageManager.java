package de.ftscraft.ftsengine.feature.roleplay.ausweis;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AusweisStorageManager {

    private final Dao<Ausweis, Integer> ausweisDao;
    private final HashMap<UUID, Ausweis> ausweisCache = new HashMap<>();

    public AusweisStorageManager(ConnectionSource connectionSource) {
        try {
            this.ausweisDao = DaoManager.createDao(connectionSource, Ausweis.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Speichert oder aktualisiert einen Ausweis in der Datenbank und im Cache
     */
    public void saveAusweis(Ausweis ausweis) {
        try {
            ausweisDao.createOrUpdate(ausweis);
            // Cache aktualisieren
            ausweisCache.put(ausweis.getUuid(), ausweis);
        } catch (SQLException e) {
            e.printStackTrace();
            Engine.getInstance().getLogger().severe("Could not save Ausweis for UUID: " + ausweis.getUuid());
        }
    }

    /**
     * Lädt alle Ausweise eines Spielers anhand der UUID
     */
    public List<Ausweis> getAusweiseByUUID(UUID uuid) {
        try {
            return ausweisDao.queryBuilder().where().eq("uuid", uuid).query();
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not retrieve Ausweise for UUID: " + uuid);
            return null;
        }
    }

    /**
     * Lädt einen Ausweis anhand der ID
     */
    public Ausweis getAusweisById(int id) {
        try {
            return ausweisDao.queryForId(id);
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not retrieve Ausweis for ID: " + id);
            return null;
        }
    }

    /**
     * Löscht einen Ausweis aus der Datenbank und dem Cache
     */
    public void deleteAusweis(Ausweis ausweis) {
        try {
            ausweisDao.delete(ausweis);
            // Aus Cache entfernen
            ausweisCache.remove(ausweis.getUuid());
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not delete Ausweis with ID: " + ausweis.getId());
        }
    }

    /**
     * Löscht einen Ausweis anhand der ID
     */
    public void deleteAusweisById(int id) {
        try {
            Ausweis ausweis = ausweisDao.queryForId(id);
            if (ausweis != null) {
                ausweisDao.deleteById(id);
                // Aus Cache entfernen
                ausweisCache.remove(ausweis.getUuid());
            }
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not delete Ausweis with ID: " + id);
        }
    }

    /**
     * Gibt den Ausweis-Cache zurück (für Kompatibilität mit altem Code)
     */
    public HashMap<UUID, Ausweis> getAusweisCache() {
        return ausweisCache;
    }

    /**
     * Lädt alle Ausweise aus der Datenbank in den Cache (beim Server-Start)
     */
    public void loadAllIntoCache() {
        try {
            List<Ausweis> allAusweise = ausweisDao.queryForAll();
            ausweisCache.clear();
            for (Ausweis ausweis : allAusweise) {
                ausweisCache.put(ausweis.getUuid(), ausweis);
            }
            Engine.getInstance().getLogger().info("Loaded " + allAusweise.size() + " Ausweise into cache.");
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not load Ausweise into cache: " + e.getMessage());
        }
    }
}

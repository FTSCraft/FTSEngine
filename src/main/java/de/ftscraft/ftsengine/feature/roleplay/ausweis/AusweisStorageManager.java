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
     * Saves or updates an Ausweis in the database and cache
     */
    public void saveAusweis(Ausweis ausweis) {
        try {
            ausweisDao.createOrUpdate(ausweis);
            // Update cache
            ausweisCache.put(ausweis.getUuid(), ausweis);
        } catch (SQLException e) {
            e.printStackTrace();
            Engine.getInstance().getLogger().severe("Could not save Ausweis for UUID: " + ausweis.getUuid());
        }
    }

    /**
     * Loads all Ausweise of a player by UUID
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
     * Loads an Ausweis by ID
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
     * Deletes an Ausweis from the database and cache
     */
    public void deleteAusweis(Ausweis ausweis) {
        try {
            ausweisDao.delete(ausweis);
            // Remove from cache
            ausweisCache.remove(ausweis.getUuid());
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not delete Ausweis with ID: " + ausweis.getId());
        }
    }

    /**
     * Deletes an Ausweis by ID
     */
    public void deleteAusweisById(int id) {
        try {
            Ausweis ausweis = ausweisDao.queryForId(id);
            if (ausweis != null) {
                ausweisDao.deleteById(id);
                // Remove from cache
                ausweisCache.remove(ausweis.getUuid());
            }
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not delete Ausweis with ID: " + id);
        }
    }

    /**
     * Returns the Ausweis cache (for compatibility with older code)
     */
    public HashMap<UUID, Ausweis> getAusweisCache() {
        return ausweisCache;
    }

    /**
     * Loads all Ausweise from the database into the cache (on server start)
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

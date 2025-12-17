package de.ftscraft.ftsengine.feature.roleplay.ausweis;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import de.ftscraft.ftsengine.main.Engine;

import java.sql.SQLException;

public class AusweisSkinStorageManager {

    private final Dao<AusweisSkin, Integer> skinDao;

    public AusweisSkinStorageManager(ConnectionSource connectionSource) {
        try {
            this.skinDao = DaoManager.createDao(connectionSource, AusweisSkin.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves or updates skin data for an Ausweis
     */
    public void saveSkin(AusweisSkin skin) {
        try {
            skinDao.createOrUpdate(skin);
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not save AusweisSkin for Ausweis ID: " + skin.getAusweisId());
            Engine.getInstance().getLogger().severe(e.getMessage());
        }
    }

    /**
     * Loads skin data for an Ausweis (lazy loading)
     */
    public AusweisSkin getSkinByAusweisId(int ausweisId) {
        try {
            return skinDao.queryBuilder()
                    .where()
                    .eq("ausweisId", ausweisId)
                    .queryForFirst();
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not retrieve AusweisSkin for Ausweis ID: " + ausweisId);
            return null;
        }
    }

    /**
     * Deletes skin data for an Ausweis
     */
    public void deleteSkin(int ausweisId) {
        try {
            AusweisSkin skin = getSkinByAusweisId(ausweisId);
            if (skin != null) {
                skinDao.delete(skin);
            }
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not delete AusweisSkin for Ausweis ID: " + ausweisId);
        }
    }
}

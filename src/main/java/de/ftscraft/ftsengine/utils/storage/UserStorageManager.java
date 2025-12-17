package de.ftscraft.ftsengine.utils.storage;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.EngineUser;

import java.sql.SQLException;
import java.util.UUID;

public class UserStorageManager {

    private final Dao<EngineUser, UUID> userDao;

    public UserStorageManager(ConnectionSource connectionSource) {
        try {
            this.userDao = DaoManager.createDao(connectionSource, EngineUser.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a user from the database or creates a new one if not present
     */
    public EngineUser getOrCreateUser(UUID uuid) {
        try {
            EngineUser user = userDao.queryForId(uuid);
            if (user == null) {
                user = new EngineUser(uuid);
                userDao.create(user);
            }
            return user;
        } catch (Exception e) {
            Engine.getInstance().getLogger().severe("Could not retrieve or create EngineUser for UUID: " + uuid);
            return null;
        }
    }

    /**
     * Saves or updates a user in the database
     */
    public void saveUser(EngineUser user) {
        try {
            userDao.createOrUpdate(user);
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not save EngineUser: " + user);
        }
    }

    /**
     * Loads a user from the database
     */
    public EngineUser getUser(UUID uuid) {
        try {
            return userDao.queryForId(uuid);
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not retrieve EngineUser for UUID: " + uuid);
            return null;
        }
    }

    /**
     * Deletes a user from the database
     */
    public void deleteUser(UUID uuid) {
        try {
            userDao.deleteById(uuid);
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not delete EngineUser for UUID: " + uuid);
        }
    }

    /**
     * Checks whether a user exists in the database
     */
    public boolean userExists(UUID uuid) {
        try {
            return userDao.idExists(uuid);
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not check if EngineUser exists for UUID: " + uuid);
            return false;
        }
    }
}

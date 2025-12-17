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
     * Lädt einen User aus der Datenbank oder erstellt einen neuen, falls nicht vorhanden
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
     * Speichert oder aktualisiert einen User in der Datenbank
     */
    public void saveUser(EngineUser user) {
        try {
            userDao.createOrUpdate(user);
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not save EngineUser: " + user);
        }
    }

    /**
     * Lädt einen User aus der Datenbank
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
     * Löscht einen User aus der Datenbank
     */
    public void deleteUser(UUID uuid) {
        try {
            userDao.deleteById(uuid);
        } catch (SQLException e) {
            Engine.getInstance().getLogger().severe("Could not delete EngineUser for UUID: " + uuid);
        }
    }

    /**
     * Überprüft, ob ein User in der Datenbank existiert
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

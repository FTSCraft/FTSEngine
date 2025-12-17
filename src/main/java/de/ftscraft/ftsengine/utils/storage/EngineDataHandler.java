package de.ftscraft.ftsengine.utils.storage;

import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisSkinStorageManager;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisStorageManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.EngineConfig;

import java.sql.SQLException;

/**
 * Central manager for all StorageManagers
 * Coordinates ConnectionHandler, UserStorageManager and AusweisStorageManager
 */
public class EngineDataHandler {

    private final Engine plugin;
    private final ConnectionHandler connectionHandler;

    private UserStorageManager userStorageManager;
    private AusweisStorageManager ausweisStorageManager;
    private AusweisSkinStorageManager ausweisSkinStorageManager;

    private boolean initialized = false;

    public EngineDataHandler(Engine plugin, EngineConfig engineConfig) {
        this.plugin = plugin;
        this.connectionHandler = new ConnectionHandler(plugin, engineConfig);
    }

    /**
     * Initializes the database connection and all StorageManagers
     */
    public void initialize() {
        try {
            plugin.getLogger().info("Initializing DataHandler...");

            // Connect to the database
            connectionHandler.connect();

            // Initialize StorageManagers
            userStorageManager = new UserStorageManager(connectionHandler.getConnectionSource());
            ausweisStorageManager = new AusweisStorageManager(connectionHandler.getConnectionSource());
            ausweisSkinStorageManager = new AusweisSkinStorageManager(connectionHandler.getConnectionSource());

            initialized = true;
            plugin.getLogger().info("DataHandler initialized successfully!");

        } catch (SQLException e) {
            plugin.getLogger().severe("Error while initializing DataHandler: " + e.getMessage());
            plugin.getLogger().severe("The plugin will be disabled!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Properly shuts down the database connection
     */
    public void shutdown() {
        plugin.getLogger().info("Shutting down DataHandler...");

        if (connectionHandler != null) {
            connectionHandler.disconnect();
        }

        initialized = false;
        plugin.getLogger().info("DataHandler has been shut down.");
    }

    /**
     * Returns the UserStorageManager
     */
    public UserStorageManager getUserStorageManager() {
        if (!initialized) {
            throw new IllegalStateException("DataHandler is not initialized!");
        }
        return userStorageManager;
    }

    /**
     * Returns the AusweisStorageManager
     */
    public AusweisStorageManager getAusweisStorageManager() {
        if (!initialized) {
            throw new IllegalStateException("DataHandler is not initialized!");
        }
        return ausweisStorageManager;
    }

    /**
     * Returns the AusweisSkinStorageManager
     */
    public AusweisSkinStorageManager getAusweisSkinStorageManager() {
        if (!initialized) {
            throw new IllegalStateException("DataHandler is not initialized!");
        }
        return ausweisSkinStorageManager;
    }

    /**
     * Returns the ConnectionHandler
     */
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    /**
     * Checks if the DataHandler is initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Checks the database connection and attempts to restore it if necessary
     */
    public void checkConnection() {
        if (!connectionHandler.isConnected()) {
            plugin.getLogger().warning("Database connection lost! Attempting to restore...");
            connectionHandler.reconnect();

            if (connectionHandler.isConnected()) {
                // Re-initialize StorageManagers
                try {
                    userStorageManager = new UserStorageManager(connectionHandler.getConnectionSource());
                    ausweisStorageManager = new AusweisStorageManager(connectionHandler.getConnectionSource());
                    plugin.getLogger().info("Database connection restored successfully!");
                } catch (Exception e) {
                    plugin.getLogger().severe("Error while restoring StorageManagers: " + e.getMessage());
                }
            }
        }
    }
}

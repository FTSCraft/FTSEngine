package de.ftscraft.ftsengine.utils.storage;

import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisSkinStorageManager;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisStorageManager;
import de.ftscraft.ftsengine.main.Engine;

import java.sql.SQLException;

/**
 * Zentrale Verwaltungsklasse für alle StorageManager
 * Koordiniert ConnectionHandler, UserStorageManager und AusweisStorageManager
 */
public class EngineDataHandler {

    private final Engine plugin;
    private final ConnectionHandler connectionHandler;

    private UserStorageManager userStorageManager;
    private AusweisStorageManager ausweisStorageManager;
    private AusweisSkinStorageManager ausweisSkinStorageManager;

    private boolean initialized = false;

    public EngineDataHandler(Engine plugin) {
        this.plugin = plugin;
        this.connectionHandler = new ConnectionHandler(plugin);
    }

    /**
     * Initialisiert die Datenbankverbindung und alle StorageManager
     */
    public void initialize() {
        try {
            plugin.getLogger().info("Initialisiere DataHandler...");

            // Verbindung zur Datenbank herstellen
            connectionHandler.connect();

            // StorageManager initialisieren
            userStorageManager = new UserStorageManager(connectionHandler.getConnectionSource());
            ausweisStorageManager = new AusweisStorageManager(connectionHandler.getConnectionSource());
            ausweisSkinStorageManager = new AusweisSkinStorageManager(connectionHandler.getConnectionSource());

            initialized = true;
            plugin.getLogger().info("DataHandler erfolgreich initialisiert!");

        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Initialisieren des DataHandlers: " + e.getMessage());
            plugin.getLogger().severe("Das Plugin wird deaktiviert!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * Beendet die Datenbankverbindung ordnungsgemäß
     */
    public void shutdown() {
        plugin.getLogger().info("Fahre DataHandler herunter...");

        if (connectionHandler != null) {
            connectionHandler.disconnect();
        }

        initialized = false;
        plugin.getLogger().info("DataHandler wurde heruntergefahren.");
    }

    /**
     * Gibt den UserStorageManager zurück
     */
    public UserStorageManager getUserStorageManager() {
        if (!initialized) {
            throw new IllegalStateException("DataHandler ist nicht initialisiert!");
        }
        return userStorageManager;
    }

    /**
     * Gibt den AusweisStorageManager zurück
     */
    public AusweisStorageManager getAusweisStorageManager() {
        if (!initialized) {
            throw new IllegalStateException("DataHandler ist nicht initialisiert!");
        }
        return ausweisStorageManager;
    }

    /**
     * Gibt den AusweisSkinStorageManager zurück
     */
    public AusweisSkinStorageManager getAusweisSkinStorageManager() {
        if (!initialized) {
            throw new IllegalStateException("DataHandler ist nicht initialisiert!");
        }
        return ausweisSkinStorageManager;
    }

    /**
     * Gibt den ConnectionHandler zurück
     */
    public ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    /**
     * Überprüft, ob der DataHandler initialisiert ist
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Überprüft die Datenbankverbindung und stellt sie bei Bedarf wieder her
     */
    public void checkConnection() {
        if (!connectionHandler.isConnected()) {
            plugin.getLogger().warning("Datenbankverbindung verloren! Versuche wiederherzustellen...");
            connectionHandler.reconnect();

            if (connectionHandler.isConnected()) {
                // StorageManager neu initialisieren
                try {
                    userStorageManager = new UserStorageManager(connectionHandler.getConnectionSource());
                    ausweisStorageManager = new AusweisStorageManager(connectionHandler.getConnectionSource());
                    plugin.getLogger().info("Datenbankverbindung erfolgreich wiederhergestellt!");
                } catch (Exception e) {
                    plugin.getLogger().severe("Fehler beim Wiederherstellen der StorageManager: " + e.getMessage());
                }
            }
        }
    }
}


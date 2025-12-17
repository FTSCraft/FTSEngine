package de.ftscraft.ftsengine.utils.storage;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.EngineUser;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisSkin;
import de.ftscraft.ftsengine.utils.EngineConfig;

import java.sql.SQLException;

/**
 * Verwaltet die Verbindung zur MySQL-Datenbank
 */
public class ConnectionHandler {

    private final Engine plugin;
    private final EngineConfig engineConfig;
    private ConnectionSource connectionSource;
    private String databaseUrl;

    public ConnectionHandler(Engine plugin, EngineConfig engineConfig) {
        this.plugin = plugin;
        this.engineConfig = engineConfig;
    }

    /**
     * Stellt die Verbindung zur Datenbank her
     */
    public void connect() throws SQLException {
        DatabaseAuthStorage dbAuth = engineConfig.databaseAuth;

        String host = dbAuth.host;
        int port = dbAuth.port;
        String database = dbAuth.database;
        String username = dbAuth.username;
        String password = dbAuth.password;

        // MySQL JDBC URL mit Zeitzone und SSL-Einstellungen
        databaseUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                host, port, database);

        plugin.getLogger().info("Verbinde zur Datenbank: " + host + ":" + port + "/" + database);

        try {
            connectionSource = new JdbcConnectionSource(databaseUrl, username, password);

            // Tabellen erstellen, falls sie nicht existieren
            createTables();

            plugin.getLogger().info("Datenbankverbindung erfolgreich hergestellt!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Verbinden zur Datenbank: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Erstellt die benötigten Tabellen in der Datenbank
     */
    private void createTables() throws SQLException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, EngineUser.class);
            TableUtils.createTableIfNotExists(connectionSource, Ausweis.class);
            TableUtils.createTableIfNotExists(connectionSource, AusweisSkin.class);
            plugin.getLogger().info("Datenbanktabellen wurden erfolgreich erstellt/überprüft.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Erstellen der Tabellen: " + e.getMessage());
            throw e;
        }
    }
    /**
     * Schließt die Datenbankverbindung
     */
    public void disconnect() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
                plugin.getLogger().info("Datenbankverbindung wurde geschlossen.");
            } catch (Exception e) {
                plugin.getLogger().severe("Fehler beim Schließen der Datenbankverbindung: " + e.getMessage());
            }
        }
    }

    /**
     * Gibt die ConnectionSource zurück
     */
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * Überprüft, ob die Verbindung aktiv ist
     */
    public boolean isConnected() {
        if (connectionSource == null) {
            return false;
        }
        try {
            return connectionSource.isOpen("");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Versucht die Verbindung wiederherzustellen
     */
    public void reconnect() {
        plugin.getLogger().info("Versuche Datenbankverbindung wiederherzustellen...");
        disconnect();
        try {
            connect();
        } catch (SQLException e) {
            plugin.getLogger().severe("Fehler beim Wiederherstellen der Datenbankverbindung: " + e.getMessage());
        }
    }
}


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
 * Manages the connection to the MySQL database
 */
public class ConnectionHandler {

    private final Engine plugin;
    private final EngineConfig engineConfig;
    private ConnectionSource connectionSource;

    public ConnectionHandler(Engine plugin, EngineConfig engineConfig) {
        this.plugin = plugin;
        this.engineConfig = engineConfig;
    }

    /**
     * Establishes the connection to the database
     */
    public void connect() throws SQLException {
        DatabaseAuthStorage dbAuth = engineConfig.databaseAuth;

        String host = dbAuth.host;
        int port = dbAuth.port;
        String database = dbAuth.database;
        String username = dbAuth.username;
        String password = dbAuth.password;

        // MySQL JDBC URL with timezone and SSL settings
        String databaseUrl = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                host, port, database);

        plugin.getLogger().info("Connecting to database: " + host + ":" + port + "/" + database);

        try {
            connectionSource = new JdbcConnectionSource(databaseUrl, username, password);

            // Create tables if they do not exist
            createTables();

            plugin.getLogger().info("Database connection established successfully!");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error while connecting to the database: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Creates the required tables in the database
     */
    private void createTables() throws SQLException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, EngineUser.class);
            TableUtils.createTableIfNotExists(connectionSource, Ausweis.class);
            TableUtils.createTableIfNotExists(connectionSource, AusweisSkin.class);
            plugin.getLogger().info("Database tables created/verified successfully.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error while creating the tables: " + e.getMessage());
            throw e;
        }
    }
    /**
     * Closes the database connection
     */
    public void disconnect() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
                plugin.getLogger().info("Database connection closed.");
            } catch (Exception e) {
                plugin.getLogger().severe("Error while closing the database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Returns the ConnectionSource
     */
    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * Checks if the connection is active
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
     * Attempts to restore the connection
     */
    public void reconnect() {
        plugin.getLogger().info("Attempting to restore database connection...");
        disconnect();
        try {
            connect();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error while restoring the database connection: " + e.getMessage());
        }
    }
}


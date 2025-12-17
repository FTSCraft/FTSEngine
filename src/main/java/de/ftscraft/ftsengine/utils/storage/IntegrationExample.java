package de.ftscraft.ftsengine.utils.storage;

/**
 * BEISPIEL-INTEGRATION FÜR DIE ENGINE.JAVA KLASSE
 *
 * Diese Datei zeigt, wie der DataHandler in der Engine-Klasse integriert werden sollte.
 *
 * 1. Füge in der Engine-Klasse ein Feld hinzu:
 *    private de.ftscraft.ftsengine.utils.storage.DataHandler databaseHandler;
 *
 * 2. In der onEnable() Methode, BEVOR andere Komponenten initialisiert werden:
 *    // Config laden (wichtig für Datenbankverbindung)
 *    saveDefaultConfig();
 *
 *    // DataHandler initialisieren
 *    databaseHandler = new de.ftscraft.ftsengine.utils.storage.DataHandler(this);
 *    databaseHandler.initialize();
 *
 * 3. In der onDisable() Methode:
 *    if (databaseHandler != null) {
 *        databaseHandler.shutdown();
 *    }
 *
 * 4. Verwendung der StorageManager:
 *
 *    // UserStorageManager verwenden
 *    UserStorageManager userStorage = databaseHandler.getUserStorageManager();
 *    EngineUser user = userStorage.getOrCreateUser(player.getUniqueId());
 *    userStorage.saveUser(user);
 *
 *    // AusweisStorageManager verwenden
 *    AusweisStorageManager ausweisStorage = databaseHandler.getAusweisStorageManager();
 *    List<Ausweis> ausweise = ausweisStorage.getAusweiseByUUID(player.getUniqueId());
 *    ausweisStorage.saveAusweis(ausweis);
 *
 * 5. Getter-Methode in Engine-Klasse hinzufügen:
 *    public de.ftscraft.ftsengine.utils.storage.DataHandler getDatabaseHandler() {
 *        return databaseHandler;
 *    }
 *
 * 6. Verwendung in anderen Klassen:
 *    Engine.getInstance().getDatabaseHandler().getUserStorageManager()...
 *
 * 7. Optionale Connection-Check (z.B. in einem Timer):
 *    Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
 *        databaseHandler.checkConnection();
 *    }, 20L * 60, 20L * 60); // Alle 60 Sekunden
 */
public class IntegrationExample {

    // Diese Klasse dient nur als Dokumentation und kann gelöscht werden

}


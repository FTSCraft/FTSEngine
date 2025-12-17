package de.ftscraft.ftsengine.main;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.ftscraft.ftsengine.feature.brett.Brett;
import de.ftscraft.ftsengine.utils.Ausweis;

import java.util.UUID;

@DatabaseTable(tableName = "user")
public class EngineUser {

    @DatabaseField(canBeNull = false, id = true)
    private UUID uuid;

    @DatabaseField(foreign = true,
            foreignAutoRefresh = true,
            canBeNull = true,
            columnName = "active_ausweis")
    private Ausweis activeAusweis;

    private Brett currentBrett = null;
    private int lanzenschlaege = 0;

    // Default constructor for ORM
    public EngineUser() {

    }

    public EngineUser(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getLanzenschlaege() {
        return lanzenschlaege;
    }

    public void setLanzenschlaege(int lanzenschlaege) {
        this.lanzenschlaege = lanzenschlaege;
    }

    public Brett getBrett() {
        return currentBrett;
    }

    public void setBrett(Brett brett) {
        this.currentBrett = brett;
    }

    public Ausweis getActiveAusweis() {
        return activeAusweis;
    }

    public void setActiveAusweis(Ausweis ausweis) {
        this.activeAusweis = ausweis;
    }
}

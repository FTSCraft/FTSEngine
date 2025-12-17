package de.ftscraft.ftsengine.utils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisSkin;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisSkinStorageManager;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.SkinService;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.entity.Player;

import java.util.UUID;

@DatabaseTable(tableName = "ausweis")
public class Ausweis {

    @DatabaseField(canBeNull = false)
    private UUID uuid;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String firstName;

    @DatabaseField
    private String lastName;

    @DatabaseField
    private String spitzname;

    @DatabaseField
    private Gender gender;

    @DatabaseField
    private String race;

    @DatabaseField
    private String desc;

    @DatabaseField
    private String forumLink;

    @DatabaseField
    private int height = 200;

    @DatabaseField
    private double lastHeightChange;


    public Ausweis() {

    }

    public Ausweis(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpitzname() {
        return spitzname;
    }

    public Gender getGender() {
        return gender;
    }

    public String getRace() {
        return race;
    }

    public String getDesc() {
        return desc;
    }

    public String getForumLink() {
        return forumLink;
    }

    public int getHeight() {
        return height;
    }

    public double getLastHeightChange() {
        return lastHeightChange;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSpitzname(String spitzname) {
        this.spitzname = spitzname;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setForumLink(String forumLink) {
        this.forumLink = forumLink;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLastHeightChange(double lastHeightChange) {
        this.lastHeightChange = lastHeightChange;
    }

    public enum Gender {
        MALE, FEMALE, DIVERS
    }

    /**
     * Lazily loads the skin data (only when needed)
     */
    public SkinService.SkinData getSkinData() {
        Engine plugin = Engine.getInstance();
        if (plugin == null || plugin.getDatabaseHandler() == null) {
            return null;
        }

        AusweisSkin skin = plugin.getDatabaseHandler().getAusweisSkinStorageManager().getSkinByAusweisId(this.id);
        return skin != null ? skin.toSkinData() : null;
    }

    /**
     * Stores the skin data in a separate table
     */
    public void setSkinData(SkinService.SkinData skinData) {
        Engine plugin = Engine.getInstance();
        if (plugin == null || plugin.getDatabaseHandler() == null) {
            return;
        }

        AusweisSkinStorageManager ausweisSkinStorageManager = plugin.getDatabaseHandler().getAusweisSkinStorageManager();
        AusweisSkin skin = ausweisSkinStorageManager.getSkinByAusweisId(this.id);

        if (skinData == null) {
            // Delete skin data
            if (skin != null) {
                ausweisSkinStorageManager.deleteSkin(this.id);
            }
        } else {
            // Save or update skin data
            if (skin == null) {
                skin = new AusweisSkin(this.id);
            }
            skin.fromSkinData(skinData);
            ausweisSkinStorageManager.saveSkin(skin);
        }
    }

    public void applySkinToPlayer(Player player) {
        SkinService.SkinData skinData = getSkinData();
        SkinService.applySkinToPlayer(player, skinData);
    }
}

package de.ftscraft.ftsengine.feature.roleplay.ausweis;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ausweis_skin")
public class AusweisSkin {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, unique = true)
    private int ausweisId;

    @DatabaseField(dataType = DataType.LONG_STRING)
    private String skinTexture;

    @DatabaseField(dataType = DataType.LONG_STRING)
    private String skinSignature;

    public AusweisSkin() {
        // ORMLite benötigt einen leeren Konstruktor
    }

    public AusweisSkin(int ausweisId) {
        this.ausweisId = ausweisId;
    }

    public int getId() {
        return id;
    }

    public int getAusweisId() {
        return ausweisId;
    }

    public String getSkinTexture() {
        return skinTexture;
    }

    public void setSkinTexture(String skinTexture) {
        this.skinTexture = skinTexture;
    }

    public String getSkinSignature() {
        return skinSignature;
    }

    public void setSkinSignature(String skinSignature) {
        this.skinSignature = skinSignature;
    }

    public SkinService.SkinData toSkinData() {
        if (skinTexture == null || skinSignature == null) {
            return null;
        }
        return new SkinService.SkinData(skinTexture, skinSignature);
    }

    public void fromSkinData(SkinService.SkinData skinData) {
        if (skinData == null) {
            this.skinTexture = null;
            this.skinSignature = null;
        } else {
            this.skinTexture = skinData.value();
            this.skinSignature = skinData.signature();
        }
    }
}


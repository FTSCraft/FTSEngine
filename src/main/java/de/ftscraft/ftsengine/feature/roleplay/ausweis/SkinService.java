package de.ftscraft.ftsengine.feature.roleplay.ausweis;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

public class SkinService {

    public record SkinData(String value, String signature) {}

    public static @Nullable SkinData getSkinData(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        try {
            URL profileUrl = new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false").toURL();
            InputStreamReader profileReader = new InputStreamReader(profileUrl.openStream());
            JsonObject properties = JsonParser.parseReader(profileReader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();

            String value = properties.get("value").getAsString();
            String signature = properties.get("signature").getAsString();
            if (value == null || signature == null) {
                return null;
            }
            return new SkinData(value, signature);
        } catch (URISyntaxException | IOException e) {
            return null;
        }

    }

    public static void applySkinToPlayer(Player player, SkinData skinData) {
        if (skinData == null)
            return;
        PlayerProfile profile = player.getPlayerProfile();
        profile.setProperty(new ProfileProperty("textures", skinData.value(), skinData.signature()));
        player.setPlayerProfile(profile);
    }

}

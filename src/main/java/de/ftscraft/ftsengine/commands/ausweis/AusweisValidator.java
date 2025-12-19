package de.ftscraft.ftsengine.commands.ausweis;

import de.ftscraft.ftsengine.utils.Ausweis;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;


public class AusweisValidator {

    private static final int MAX_HEIGHT = 300;
    private static final int MIN_HEIGHT = 90;
    private static final int MIN_APPEARANCE_WORDS = 4;
    private static final String FORUM_URL_PREFIX = "https://forum.ftscraft.de/";

    /**
     * Checks if the height is within valid range.
     */
    public boolean isValidHeight(int height) {
        return height >= MIN_HEIGHT && height <= MAX_HEIGHT;
    }

    /**
     * Returns the minimum allowed height.
     */
    public int getMinHeight() {
        return MIN_HEIGHT;
    }

    /**
     * Returns the maximum allowed height.
     */
    public int getMaxHeight() {
        return MAX_HEIGHT;
    }

    /**
     * Checks if the appearance description contains enough words (minimum 4).
     */
    public boolean isValidAppearance(String appearance) {
        if (appearance == null || appearance.trim().isEmpty()) {
            return false;
        }
        String[] words = appearance.split("\\s+");
        return words.length >= MIN_APPEARANCE_WORDS;
    }

    /**
     * Returns the minimum number of words required for appearance description.
     */
    public int getMinAppearanceWords() {
        return MIN_APPEARANCE_WORDS;
    }

    /**
     * Checks if a string is a valid URL.
     */
    public boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }

        if (url.contains(" ")) {
            return false;
        }

        try {
            URI.create(url).toURL();
            return true;
        } catch (MalformedURLException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks if the URL belongs to the forum.
     */
    public boolean isForumUrl(String url) {
        return url != null && url.startsWith(FORUM_URL_PREFIX);
    }

    /**
     * Returns the prefix for forum URLs.
     */
    public String getForumUrlPrefix() {
        return FORUM_URL_PREFIX;
    }

    /**
     * Checks if the gender is valid (m or f).
     */
    public boolean isValidGender(String gender) {
        return gender != null && (gender.equalsIgnoreCase("m") || gender.equalsIgnoreCase("f"));
    }

    /**
     * Checks if the race is valid.
     */
    public boolean isValidRace(String race) {
        if (race == null) return false;
        String normalized = race.substring(0, 1).toUpperCase() + race.substring(1).toLowerCase();
        return normalized.equals("Ork") || normalized.equals("Zwerg")
                || normalized.equals("Mensch") || normalized.equals("Elf");
    }

    /**
     * Normalizes the race (first letter uppercase, rest lowercase).
     */
    public String normalizeRace(String race) {
        if (race == null || race.isEmpty()) return null;
        return race.substring(0, 1).toUpperCase() + race.substring(1).toLowerCase();
    }

    /**
     * Checks if an Ausweis exists (is not null).
     */
    public boolean ausweisExists(Ausweis ausweis) {
        return ausweis != null;
    }

    /**
     * Checks if an Ausweis belongs to a specific player.
     */
    public boolean isAusweisOwner(Ausweis ausweis, UUID playerUUID) {
        if (ausweis == null || playerUUID == null) {
            return false;
        }
        return ausweis.getUuid().equals(playerUUID);
    }
}


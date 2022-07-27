package de.ftscraft.ftsengine.utils;

public class Race {

    private final int defaultNahkampf;
    private final int defaultFernkampf;
    private final int defaultAgilitaet;
    private final int defaultMagie;
    private final int defaultLeben;

    private final String name;
    private final String superKlasse;

    private final String textureValue;

    private final int id;

    public Race(int defaultNahkampf, int defaultFernkampf, int defaultAgilitaet, int defaultMagie, int defaultLeben, String name, String superKlasse, String textureValue, int id) {
        this.defaultNahkampf = defaultNahkampf;
        this.defaultFernkampf = defaultFernkampf;
        this.defaultAgilitaet = defaultAgilitaet;
        this.defaultMagie = defaultMagie;
        this.defaultLeben = defaultLeben;
        this.name = name;
        this.superKlasse = superKlasse;
        this.textureValue = textureValue;
        this.id = id;
    }

    public String getTextureValue() {
        return textureValue;
    }

    public int getDefaultNahkampf() {
        return defaultNahkampf;
    }

    public int getDefaultFernkampf() {
        return defaultFernkampf;
    }

    public int getDefaultAgilitaet() {
        return defaultAgilitaet;
    }

    public int getDefaultMagie() {
        return defaultMagie;
    }

    public int getDefaultLeben() {
        return defaultLeben;
    }

    public String getName() {
        return name;
    }

    public String getSuperKlasse() {
        return superKlasse;
    }

    public int getId() {
        return id;
    }

}

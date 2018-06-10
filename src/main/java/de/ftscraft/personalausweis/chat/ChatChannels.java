package de.ftscraft.personalausweis.chat;

public enum ChatChannels
{

    ROLEPLAY("§5RP"), HANDEL("§4Handel"), FLÜSTERN("§8Flüster"), RUFEN("§2Schreien");

    private String prefix;

    ChatChannels(String prefix)
    {
        this.prefix = prefix;
    }

}

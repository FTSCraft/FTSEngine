package de.ftscraft.personalausweis.backpacks;

public enum BackpackType
{

    TINY("§2Kleiner Rucksack"), LARGE("§4Großer Rucksack"), ENDER("");

    private String name;

    BackpackType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }


}

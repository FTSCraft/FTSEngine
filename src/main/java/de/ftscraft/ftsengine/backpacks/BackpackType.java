package de.ftscraft.ftsengine.backpacks;

public enum BackpackType
{

    TINY("§2Kleiner Rucksack", 3 * 9),
    LARGE("§4Großer Rucksack", 6 * 9),
    ENDER("", 0);

    private String name;
    private int size;

    BackpackType(String name, int size)
    {
        this.name = name;
        this.size = size;
    }

    public String getName()
    {
        return name;
    }

    public int getSize() {
        return size;
    }

    public static BackpackType getBackpackByName(String name) {
        for(BackpackType bt : BackpackType.values()) {
            if(bt.getName().equalsIgnoreCase(name))
                return bt;
        }
        return null;
    }


}

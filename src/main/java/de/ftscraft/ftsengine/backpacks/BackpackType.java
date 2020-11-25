package de.ftscraft.ftsengine.backpacks;

public enum BackpackType
{

    TINY("§2Kleiner Rucksack", 4 * 9, "§7In diesen Rucksack passen viele, weitere Dinge rein"),
    LARGE("§4Großer Rucksack", 6 * 9, "§7Dieser nützliche Rucksack hat Platz für viele Sachen"),
    ENDER("§6Ender Rucksack", -1, "§7Öffne mit diesem Rucksack deine Ender-Kiste");

    private String name;
    private int size;
    private String lore;

    BackpackType(String name, int size, String lore)
    {
        this.name = name;
        this.size = size;
        this.lore = lore;
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


    public String getLore()
    {
        return lore;
    }

}

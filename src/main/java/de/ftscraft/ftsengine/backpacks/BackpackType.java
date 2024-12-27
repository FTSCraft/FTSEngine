package de.ftscraft.ftsengine.backpacks;

import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.inventory.ItemStack;

public enum BackpackType {

    TINY("§2Kleiner Rucksack", 4 * 9, "§7In diesen Rucksack passen viele, weitere Dinge rein", "TINY_BACKPACK"),
    LARGE("§4Großer Rucksack", 6 * 9, "§7Dieser nützliche Rucksack hat Platz für viele Sachen", "LARGE_BACKPACK"),
    ENDER("§6Ender Rucksack", -1, "§7Öffne mit diesem Rucksack deine Ender-Kiste", "ENDER_BACKPACK");

    private final String name;
    private final int size;
    private final String lore;
    private final String sign;

    BackpackType(String name, int size, String lore, String sign) {
        this.name = name;
        this.size = size;
        this.lore = lore;
        this.sign = sign;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public static BackpackType getBackpackByName(String name) {
        for (BackpackType bt : BackpackType.values()) {
            if (bt.getName().equalsIgnoreCase(name))
                return bt;
        }
        return null;
    }

    public static BackpackType getBackpackByItem(ItemStack itemStack) {
        String sign = ItemReader.getSign(itemStack);
        if (sign == null)
            return null;
        for (BackpackType value : BackpackType.values()) {
            if (value.getSign().equalsIgnoreCase(sign))
                return value;
        }
        return null;
    }


    public String getLore() {
        return lore;
    }

    public String getSign() {
        return sign;
    }
}

package de.ftscraft.ftsengine.quivers;

import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public enum QuiverType {

    QUIVER("§6Köcher", 9, "§7Hier kannst du deine Pfeile verstauen", "QUIVER");

    private final String name;
    private final int size;
    private final String lore;
    private final String sign;

    QuiverType(String name, int size, String lore, String sign) {
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

    public static QuiverType getQuiverByName(String name) {
        return name.equalsIgnoreCase(QUIVER.getName()) ? QUIVER : null;
    }

    public static QuiverType getQuiverByItem(ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (!meta.hasLore()) {
            return null;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) {
            return null;
        }

        String sign = ItemReader.getSign(itemStack);
        if (sign == null || !sign.equalsIgnoreCase(QUIVER.getSign())) {
            return null;
        }

        return QUIVER;
    }

    public String getLore() {
        return lore;
    }

    public String getSign() {
        return sign;
    }
}

package de.ftscraft.ftsengine.feature.texturepack.catalog.gui.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;

/**
 * Service for removing textures from items
 */
@SuppressWarnings("UnstableApiUsage")
public class TextureResetService {

    /**
     * Removes the texture from an ItemStack
     */
    public void removeTexture(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        CustomModelDataComponent customModelDataComponent = itemMeta.getCustomModelDataComponent();

        customModelDataComponent.setStrings(new ArrayList<>());
        itemMeta.setCustomModelDataComponent(customModelDataComponent);
        item.setItemMeta(itemMeta);
    }
}

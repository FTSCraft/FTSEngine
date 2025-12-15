package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.feature.items.signs.TeachingBoardManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsutils.items.ItemBuilder;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    public final Engine plugin;

    public BlockPlaceListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        handleTeachingBoards(event.getPlayer(), event.getItemInHand(), event.getBlockPlaced());
    }

    private void handleTeachingBoards(Player player, ItemStack itemStack, Block placedBlock) {
        if (!"teaching-board".equals(ItemReader.getSign(itemStack))) {
            return;
        }
        if (!(placedBlock.getBlockData() instanceof WallSign) && !(placedBlock.getBlockData() instanceof org.bukkit.block.data.type.Sign)) {
            return;
        }
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) placedBlock.getState();
        sign.getPersistentDataContainer().set(ItemBuilder.getSignKey(), PersistentDataType.STRING, "teaching-board");

        if (!TeachingBoardManager.copyPDCToSign(player, itemStack, sign)) {
            TeachingBoardManager.create(player, sign);
        }

        player.sendMessage("§aLehrtafel erfolgreich erstellt. §fMit Shift-Rechtsklick auf das Schild, kannst du den Inhalt bearbeiten.");
        sign.update(true, false);
    }
}



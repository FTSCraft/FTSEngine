package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.signs.TeachingBoard;
import de.ftscraft.ftsengine.signs.TeachingBoardManager;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.items.ItemBuilder;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.*;

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
        if(!"teaching-board".equals(ItemReader.getSign(itemStack))) {
            return;
        }
        if(!(placedBlock.getBlockData() instanceof WallSign) && !(placedBlock.getBlockData() instanceof org.bukkit.block.data.type.Sign)) {
            return;
        }
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) placedBlock.getState();
        sign.getPersistentDataContainer().set(ItemBuilder.getSignKey(), PersistentDataType.STRING, "teaching-board");
        sign.update(true, false);
    }
}



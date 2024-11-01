package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplodeListener implements Listener {

    public BlockExplodeListener() {
        Engine.getInstance().getServer().getPluginManager().registerEvents(this, Engine.getInstance());
    }

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent event) {
        Block block = event.getBlock();
        if (block.getLocation().getWorld().getEnvironment() != World.Environment.NETHER)
            return;
        event.blockList().removeIf(eBlock -> eBlock.getType() == Material.GOLD_BLOCK);
    }

}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockType;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class BlockBreakListener implements Listener {

    public Engine plugin;

    public BlockBreakListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (event.isCancelled()) {
            return;
        }

        if (event.getBlock().getType() == Material.GOLD_BLOCK && event.getBlock().getWorld().getEnvironment() == World.Environment.NETHER) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
        }

        //Schwarzes Brett und Briefkasten
        if (event.getBlock().getBlockData() instanceof WallSign || event.getBlock().getBlockData() instanceof org.bukkit.block.data.type.Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase("§4Schwarzes Brett")) {
                if (!(event.getPlayer().hasPermission("blackboard.remove")) && !plugin.bretter.get(sign.getLocation()).getCreator().toString().equals(event.getPlayer().getUniqueId().toString())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du darfst das nicht kaputt machen!");
                } else {
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du hast erfolgreich das Schwarze Brett entfernt");
                    Brett brett = plugin.bretter.get(event.getBlock().getLocation());
                    brett.remove();
                }
            }

            //Briefkasten

            if (sign.getLine(0).equalsIgnoreCase("§7[§2Briefkasten§7]")) {

                String tName = sign.getLine(1);

                OfflinePlayer op = Bukkit.getOfflinePlayer(tName);

                if (op == null) {

                    event.setCancelled(true);

                    event.getPlayer().sendMessage(Messages.PREFIX + "Bitte kontaktiere einen Admin falls das dein Briefkasten ist. Stichwort: UUID nicht vorhanden");
                    return;
                }

                if (op.getName().equals(event.getPlayer().getName())) {

                    if (!plugin.briefkasten.containsKey(op.getUniqueId())) {
                        event.setCancelled(false);
                        return;
                    }

                    plugin.briefkasten.remove(op.getUniqueId());

                    File file = new File(plugin.getDataFolder() + "//briefkasten//" + event.getPlayer().getUniqueId() + ".yml");

                    file.getName();

                    file.delete();

                    event.getPlayer().sendMessage(Messages.PREFIX + "Du hast deinen Briefkasten erfolgreich entfernt!");

                } else {
                    event.getPlayer().sendMessage(Messages.PREFIX + "Das ist nicht dein Briefkasten!");
                }


            }

        } else if (event.getBlock().getType() == Material.CHEST) {

            Block block = event.getBlock();
            BlockData blockData = block.getBlockData();
            Directional directional = (Directional) blockData;

            boolean briefkasten = false;

            for (BlockFace face : directional.getFaces()) {
                Block a = block.getRelative(face.getOppositeFace());

                if (a.getState() instanceof Sign) {

                    Sign sign = (Sign) a.getState();

                    if (sign.getLine(0).equalsIgnoreCase("§7[§2Briefkasten§7]")) {

                        briefkasten = true;
                        break;
                    }

                }

            }

            if (briefkasten) {

                event.getPlayer().sendMessage(Messages.PREFIX + "Falls das dein Briefkasten ist, zerstöre erst das Schild");

                event.setCancelled(true);

            }

        }

        //Emeraldpickaxe
        if (event.getPlayer().getInventory().getItemInMainHand().isEmpty() || (event.getPlayer().getInventory().getItemInMainHand() == null))
            return;

        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE) {
            String sign = ItemReader.getSign(event.getPlayer().getInventory().getItemInMainHand());
            if (sign != null && sign.equals("EMERALDPICKAXE")) {
                excavateArea(event.getBlock(), event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer().getTargetBlockFace(10));
            }
        }
    }

    public void excavateArea(Block centerBlock, ItemStack tool, BlockFace direction) {
        if (direction == BlockFace.UP || direction == BlockFace.DOWN) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    Block targetBlock = centerBlock.getRelative(xOffset, 0, zOffset);
                    breakBlockWithTool(targetBlock, tool);
                }
            }
        } else {
            if (direction == BlockFace.EAST || direction == BlockFace.WEST) {
                for (int yOffset = -1; yOffset <= 1; yOffset++) {
                    for (int zOffset = -1; zOffset <= 1; zOffset++) {
                        Block targetBlock = centerBlock.getRelative(0, yOffset, zOffset);
                        breakBlockWithTool(targetBlock, tool);
                    }
                }
            } else if (direction == BlockFace.NORTH || direction == BlockFace.SOUTH) {
                for (int yOffset = -1; yOffset <= 1; yOffset++) {
                    for (int xOffset = -1; xOffset <= 1; xOffset++) {
                        Block targetBlock = centerBlock.getRelative(xOffset, yOffset, 0);
                        if(targetBlock.getType() != Material.AIR)
                            breakBlockWithTool(targetBlock, tool);
                    }
                }
            }
        }
    }

    private boolean breakBlockWithTool(Block block, ItemStack tool) {
        block.breakNaturally(tool);
        if (tool.getType().getMaxDurability() > 0) {
            tool.setDurability((short) (tool.getDurability() + 1));

            if (tool.getDurability() >= tool.getType().getMaxDurability()) {
                tool.setAmount(0);
                return true;
            }
        }
        return false;
    }
}


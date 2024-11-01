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
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class BlockBreakListener implements Listener {

    public Engine plugin;
    private final Set<Player> emeraldPickaxeUsers = Collections.synchronizedSet(new HashSet<>());
    private final Collection<Material> seeds = new HashSet<>(Arrays.asList(
            Material.WHEAT_SEEDS,
            Material.BEETROOT_SEEDS,
            Material.CARROT,
            Material.POTATO,
            Material.MELON_SEEDS,
            Material.PUMPKIN_SEEDS,
            Material.TORCHFLOWER_SEEDS,
            Material.COCOA_BEANS
    ));

    public BlockBreakListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (event.isCancelled()) {
            return;
        }

        handleNetherGoldBlock(event);
        handleSchwarzesBrett(event);
        handleBriefkasten(event);
        handleEmeraldPickaxe(event);
    }

    @EventHandler
    public void onBlockDropItem(BlockDropItemEvent event) {
        handleSense(event);
    }

    private void handleSense(BlockDropItemEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        String sign = ItemReader.getSign(item);
        if (!"SENSE".equals(sign)) {
            return;
        }
        if (!(event.getBlockState().getBlockData() instanceof Ageable)) {
            return;
        }
        event.getBlock().setType(event.getBlockState().getType());
        event.getItems().stream()
                .map(Item::getItemStack)
                .filter(drop -> seeds.contains(drop.getType()))
                .forEach(drop -> drop.setAmount(drop.getAmount() - 1));
    }

    private void handleNetherGoldBlock(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.GOLD_BLOCK && event.getBlock().getWorld().getEnvironment() == World.Environment.NETHER) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT));
        }
    }

    private void handleSchwarzesBrett(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getBlockData() instanceof WallSign || block.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
            Sign sign = (Sign) block.getState();
            if (sign.getLine(0).equalsIgnoreCase("§4Schwarzes Brett")) {
                if (!event.getPlayer().hasPermission("blackboard.remove") && !plugin.bretter.get(sign.getLocation()).getCreator().toString().equals(event.getPlayer().getUniqueId().toString())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du darfst das nicht kaputt machen!");
                } else {
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du hast erfolgreich das Schwarze Brett entfernt");
                    Brett brett = plugin.bretter.get(event.getBlock().getLocation());
                    brett.remove();
                }
            }
        }
    }

    private void handleBriefkasten(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getBlockData() instanceof WallSign || block.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
            Sign sign = (Sign) block.getState();
            if (sign.getLine(0).equalsIgnoreCase("§7[§2Briefkasten§7]")) {
                handleBriefkastenSign(event, sign);
            }
        } else if (block.getType() == Material.CHEST) {
            handleBriefkastenChest(event, block);
        }
    }

    private void handleBriefkastenSign(BlockBreakEvent event, Sign sign) {
        String tName = sign.getLine(1);
        OfflinePlayer op = Bukkit.getOfflinePlayer(tName);

        if (op.getName() != null && op.getName().equals(event.getPlayer().getName())) {
            if (!plugin.briefkasten.containsKey(op.getUniqueId())) {
                event.setCancelled(false);
                return;
            }
            plugin.briefkasten.remove(op.getUniqueId());
            File file = new File(plugin.getDataFolder() + "//briefkasten//" + event.getPlayer().getUniqueId() + ".yml");
            file.delete();
            event.getPlayer().sendMessage(Messages.PREFIX + "Du hast deinen Briefkasten erfolgreich entfernt!");
        } else {
            event.getPlayer().sendMessage(Messages.PREFIX + "Das ist nicht dein Briefkasten!");
        }
    }

    private void handleBriefkastenChest(BlockBreakEvent event, Block block) {
        Directional directional = (Directional) block.getBlockData();
        boolean briefkasten = false;
        for (BlockFace face : directional.getFaces()) {
            Block adjacentBlock = block.getRelative(face.getOppositeFace());
            if (adjacentBlock.getState() instanceof Sign) {
                Sign sign = (Sign) adjacentBlock.getState();
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

    private void handleEmeraldPickaxe(BlockBreakEvent event) {
        if (emeraldPickaxeUsers.contains(event.getPlayer())) return;
        ItemStack itemInHand = event.getPlayer().getInventory().getItemInMainHand();
        if (itemInHand.isEmpty() || itemInHand == null) return;

        if (itemInHand.getType() == Material.DIAMOND_PICKAXE) {
            String sign = ItemReader.getSign(itemInHand);
            if (sign != null && sign.equals("EMERALDPICKAXE")) {
                excavateArea(event.getBlock(), event.getPlayer(), event.getPlayer().getTargetBlockFace(10));
            }
        }
    }

    public void excavateArea(Block centerBlock, Player p, BlockFace direction) {
        emeraldPickaxeUsers.add(p);
        Bukkit.getScheduler().runTask(plugin, () -> {
            if (direction == BlockFace.UP || direction == BlockFace.DOWN) {
                for (int xOffset = -1; xOffset <= 1; xOffset++) {
                    for (int zOffset = -1; zOffset <= 1; zOffset++) {
                        Block targetBlock = centerBlock.getRelative(xOffset, 0, zOffset);
                        p.breakBlock(targetBlock);
                    }
                }
            } else {
                if (direction == BlockFace.EAST || direction == BlockFace.WEST) {
                    for (int yOffset = -1; yOffset <= 1; yOffset++) {
                        for (int zOffset = -1; zOffset <= 1; zOffset++) {
                            Block targetBlock = centerBlock.getRelative(0, yOffset, zOffset);
                            p.breakBlock(targetBlock);
                        }
                    }
                } else if (direction == BlockFace.NORTH || direction == BlockFace.SOUTH) {
                    for (int yOffset = -1; yOffset <= 1; yOffset++) {
                        for (int xOffset = -1; xOffset <= 1; xOffset++) {
                            Block targetBlock = centerBlock.getRelative(xOffset, yOffset, 0);
                            if (targetBlock.getType() != Material.AIR) p.breakBlock(targetBlock);
                        }
                    }
                }
            }
            emeraldPickaxeUsers.remove(p);
        });
    }

}


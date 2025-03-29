package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.feature.instruments.InstrumentManager;
import de.ftscraft.ftsengine.logport.LogportManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.quivers.Quiver;
import de.ftscraft.ftsengine.quivers.QuiverType;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import de.ftscraft.ftsutils.items.ItemReader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class PlayerInteractListener implements Listener {

    private final Engine plugin;
    private final LogportManager logportManager;
    private final ArrayList<Player> hornCooldown = new ArrayList<>();

    public PlayerInteractListener(Engine plugin) {
        this.plugin = plugin;
        logportManager = plugin.getLogportManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack itemInHand = e.getItem();
        Block clickedBlock = e.getClickedBlock();

        if (e.getAction() == Action.LEFT_CLICK_AIR) {
            removePassengers(player);
        }

        if (itemInHand != null) {
            if (itemInHand.getType() == Material.BOW || itemInHand.getType() == Material.CROSSBOW) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    handleBowOrCrossbowDraw(player);
                }
            }

            handleHorn(player, itemInHand);
            handleMeissel(player, itemInHand);
            handleLogport(e, player, itemInHand);
            handleWeihrauchlaterne(player, itemInHand);
            handleBackpack(player, itemInHand);
            handleQuiver(e, player, itemInHand);
            handleFertilizer(e, clickedBlock);
            handleInstrument(e, itemInHand);
        }

        if (clickedBlock != null) {
            handleSchwarzesBrett(player, clickedBlock, e.getAction());
        }
    }

    private void handleBowOrCrossbowDraw(Player player) {
        PlayerInventory inventory = player.getInventory();

        if (inventory.getItemInOffHand().getType() != Material.AIR) return;

        ItemStack itemInHand = inventory.getItemInMainHand();
        if (itemInHand.getType() == Material.CROSSBOW && isCrossbowLoaded(itemInHand)) return;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && Quiver.isValidArrow(item)) return;
        }

        Quiver quiver = Quiver.getQuiverFromItem(plugin, Quiver.findQuiverInInventory(player));
        if (quiver == null) return;

        ItemStack arrow = quiver.getArrowFromQuiver();
        if (arrow != null) {
            inventory.setItemInOffHand(arrow);
        } else {
            player.sendMessage(Messages.PREFIX + "Dein Köcher ist leer!");
        }
    }

    @EventHandler
    public void onBowOrCrossbowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand != null && itemInHand.getType() == Material.CROSSBOW) return;

        Quiver quiver = Quiver.getQuiverFromItem(plugin, Quiver.findQuiverInInventory(player));
        if (quiver != null) {
            quiver.consumeArrow(player);
        }
    }

    private boolean isCrossbowLoaded(ItemStack itemInHand) {
        if (itemInHand == null || !(itemInHand.getItemMeta() instanceof CrossbowMeta crossbowMeta)) {
            return false;
        }
        return crossbowMeta.hasChargedProjectiles();
    }

    // Entfernt Passagiere bei Links-Klick in die Luft
    private void removePassengers(Player player) {
        Object[] passengers = player.getPassengers().toArray();
        for (Object passenger : passengers) {
            player.removePassenger((Entity) passenger);
        }
    }

    // Horn Handlers
    private void handleHorn(Player player, ItemStack item) {
        if (item.getType() == Material.NAUTILUS_SHELL) {
            String sign = ItemReader.getSign(item);
            if ("HORN".equals(sign)) {
                playHornSound(player);
            }
        }
    }

    private void playHornSound(Player player) {
        if (hornCooldown.contains(player)) return;

        hornCooldown.add(player);
        Random random = new Random();
        int r = random.nextInt(50) + 10;

        player.playSound(Sound.sound(Key.key("event.raid.horn"), Sound.Source.VOICE, 100, r), Sound.Emitter.self());

        for (Entity entity : player.getNearbyEntities(70, 70, 70)) {
            if (entity instanceof Player nearbyPlayer) {
                nearbyPlayer.playSound(Sound.sound(Key.key("event.raid.horn"), Sound.Source.VOICE, 100, r), player);
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> hornCooldown.remove(player), 20 * 2);
    }

    private void handleMeissel(Player player, ItemStack item) {
        if ("MEISSEL".equals(ItemReader.getSign(item))) {
            player.openStonecutter(null, true);
        }
    }

    private void handleLogport(PlayerInteractEvent event, Player player, ItemStack item) {
        if (item.getType() == Material.RECOVERY_COMPASS && "LOGPORT".equals(ItemReader.getSign(item))) {
            if (player.getOpenInventory().getType() != InventoryType.CRAFTING) {
                event.setCancelled(true);
                return;
            }

            if (event.getAction().toString().contains("RIGHT_CLICK")) {
                if (player.isSneaking()) {
                    logportManager.reloadLogport(player, item);
                } else {
                    logportManager.startTeleportCountdown(player, item);
                }
            } else if (event.getAction().toString().contains("LEFT_CLICK")) {
                if (player.isSneaking()) {
                    handleSneakLeftClickLogport(player, item);
                }
            }

            if (event.getHand() == EquipmentSlot.HAND) {
                event.setCancelled(true);
            }
        }
    }

    private void handleSneakLeftClickLogport(Player player, ItemStack item) {
        if (!player.isOnGround()) {
            player.sendMessage(Messages.PREFIX + ChatColor.RED + "Du kannst keinen Teleportpunkt in der Luft setzen!");
            return;
        }

        if (player.isInsideVehicle()) {
            player.sendMessage(Messages.PREFIX + ChatColor.RED + "Du kannst keinen Teleportpunkt setzen, während du auf einem Reittier sitzt!");
            return;
        }

        logportManager.saveLocationToItem(player, item);
    }

    private void handleWeihrauchlaterne(Player player, ItemStack item) {
        if (item.getType() == Material.LANTERN && "§cWeihrauchlaterne".equals(item.getItemMeta().getDisplayName())) {
            player.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, player.getLocation().add(0, 1.5, 0), 8, 0.0D, 0, 0.01D, 0.01D);
        }
    }

    private void handleQuiver(PlayerInteractEvent event, Player player, ItemStack item) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        QuiverType type = QuiverType.getQuiverByItem(item);
        if (type != null) {
            event.setCancelled(true);
            openOrRegisterQuiver(player, item, type);
        }
    }

    private void openOrRegisterQuiver(Player player, ItemStack quiver, QuiverType type) {
        int id = Var.getQuiverID(quiver);

        if (id == -1) {
            new Quiver(plugin, type, player, quiver);
        } else {
            Quiver quiverInst = plugin.quivers.get(id);

            if (quiverInst == null) {
                player.sendMessage(Messages.PREFIX + "Dieser Köcher ist nicht registriert?");
                return;
            }

            quiverInst.open(player);
        }
    }

    private void handleBackpack(Player player, ItemStack item) {
        if (player.getInventory().getChestplate() != null && item != null && "BACKPACK_KEY".equals(ItemReader.getSign(item))) {
            BackpackType type = BackpackType.getBackpackByItem(player.getInventory().getChestplate());
            if (type != null) {
                if (type == BackpackType.ENDER) {
                    player.openInventory(player.getEnderChest());
                    return;
                }
                openOrRegisterBackpack(player, type);
            }
        }
    }

    private void openOrRegisterBackpack(Player player, BackpackType type) {
        ItemStack chest = player.getInventory().getChestplate();
        int id = Var.getBackpackID(chest);

        if (id == -1) {
            new Backpack(plugin, type, player);
        } else {
            Backpack bp = plugin.backpacks.get(id);

            if (bp == null) {
                player.sendMessage(Messages.PREFIX + "Dieser Rucksack ist (warum auch immer) nicht registriert?");
                return;
            }

            bp.open(player);
        }
    }

    private void handleFertilizer(PlayerInteractEvent event, Block clickedBlock) {
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.BONE_MEAL && "FERTILIZER".equals(ItemReader.getSign(item))) {
            applyBonemealToArea(clickedBlock);
        }
    }

    private void applyBonemealToArea(Block centerBlock) {
        for (int yOffset = 0; yOffset >= -1; yOffset--) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    Block targetBlock = centerBlock.getRelative(xOffset, yOffset, zOffset);
                    applyBonemealToBlock(targetBlock);
                }
            }
        }
    }

    private void applyBonemealToBlock(Block block) {
        block.applyBoneMeal(BlockFace.UP);
    }

    private void handleSchwarzesBrett(Player player, Block block, Action action) {
        // If trying to break block, skip
        if (action == Action.LEFT_CLICK_BLOCK)
            return;

        if (block.getBlockData() instanceof WallSign || block.getBlockData() instanceof Sign) {
            org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
            if ("§4Schwarzes Brett".equalsIgnoreCase(sign.getLine(0))) {
                Brett brett = plugin.bretter.get(block.getLocation());
                if (brett != null) {
                    Engine.getInstance().getPlayer().get(player).setBrett(brett);
                    brett.getGui().open(player, 1);
                }
            }
        }
    }

    private void handleInstrument(@NotNull PlayerInteractEvent event, @NotNull ItemStack item) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        if (!"INSTRUMENT".equals(ItemReader.getSign(item)))
            return;
        Integer type = ItemReader.getPDC(item, "type", PersistentDataType.INTEGER);
        if (type == null)
            return;
        event.getPlayer().openInventory(InstrumentManager.instruments[type].getInventory());
    }
}
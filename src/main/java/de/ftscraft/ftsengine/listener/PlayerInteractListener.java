package de.ftscraft.ftsengine.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import de.ftscraft.ftsutils.items.ItemReader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class PlayerInteractListener implements Listener {

    private final Engine plugin;
    private final ProtocolManager protocolManager;

    private final ArrayList<Player> hornCooldown = new ArrayList<>();

    public PlayerInteractListener(Engine plugin) {
        this.plugin = plugin;
        this.protocolManager = plugin.getProtocolManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        if (e.getAction() == Action.LEFT_CLICK_AIR) {
            Object[] passengers = e.getPlayer().getPassengers().toArray();
            for (Object passenger : passengers) {
                e.getPlayer().removePassenger((Entity) passenger);
            }
        }

        if (e.getItem() != null) {

            if (e.getItem().getType() == Material.BOW) {
                ItemStack is = e.getItem();
                if (is.hasItemMeta()) {
                    //TODO HÄÄ??
                    if (is.getItemMeta().getDisplayName().equalsIgnoreCase("§6Harfe")) {

                        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Client.BLOCK_DIG);
                        packet.getModifier().writeDefaults();
                        //Index 4 = The action the player is taking against the block
                        packet.getIntegers().write(0, 5);
                        //Index 0-3 = Standard values which have to be set on 0
                        packet.getIntegers().write(0, 0);
                        packet.getIntegers().write(0, 0);
                        try {
                            protocolManager.sendServerPacket(e.getPlayer(), packet);
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.NAUTILUS_SHELL) {

                ItemStack item = e.getPlayer().getInventory().getItemInMainHand();

                if (item.hasItemMeta()) {
                    String sign = ItemReader.getSign(item);
                    if (sign != null && sign.equals("HORN")) {

                        final Player p = e.getPlayer();

                        if (hornCooldown.contains(p)) {
                            return;
                        }


                        hornCooldown.add(p);

                        Random random = new Random();
                        int r = random.nextInt(50) + 10;

                        //p.playSound(p.getLocation(), Sound.EVENT_RAID_HORN, 100, r);
                        p.playSound(Sound.sound(Key.key("event.raid.horn"), Sound.Source.VOICE, 100, r), Sound.Emitter.self());

                        for (Entity n : p.getNearbyEntities(70, 70, 70)) {
                            if (n instanceof Player) {
                                Player playerInRadius = (Player) n;

                                n.playSound(Sound.sound(Key.key("event.raid.horn"), Sound.Source.VOICE, 100, r), p);
                                //playerInRadius.playSound(p.getLocation(), Sound.EVENT_RAID_HORN, SoundCategory.VOICE, 300, r);
                            }
                        }

                        Bukkit.getScheduler().runTaskLater(plugin, () -> hornCooldown.remove(p), 20 * 2);

                    }

                }

            } else if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LIGHTNING_ROD) {
                String sign = ItemReader.getSign(e.getPlayer().getInventory().getItemInMainHand());
                if (sign != null && sign.equals("MEISSEL")) {
                    e.getPlayer().openStonecutter(null, true);
                    return;
                }
            }

        }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getBlockData() instanceof WallSign || e.getClickedBlock().getBlockData() instanceof Sign) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) e.getClickedBlock().getState();
                if (sign.getLine(0).equalsIgnoreCase("§4Schwarzes Brett")) {
                    if (sign.getLine(1).equalsIgnoreCase("§bGlobal")) {
                        for (Brett value : plugin.bretter.values()) {
                            if (value.getName().equalsIgnoreCase("Global")) {
                                plugin.getPlayer().get(e.getPlayer()).setBrett(value);
                                value.getGui().open(e.getPlayer(), 1);
                                value.checkForRunOut();
                            }
                        }
                    }
                }
                if (plugin.bretter.containsKey(e.getClickedBlock().getLocation())) {
                    plugin.bretter.get(e.getClickedBlock().getLocation()).getGui().open(e.getPlayer(), 1);
                    Brett brett = plugin.bretter.get(e.getClickedBlock().getLocation());
                    plugin.getPlayer().get(e.getPlayer()).setBrett(brett);
                    brett.checkForRunOut();
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {

            if (e.getPlayer().getInventory().getChestplate() != null && e.getPlayer().getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
                    ItemStack chest = e.getPlayer().getInventory().getChestplate();
                    String chestplateSign = ItemReader.getSign(chest);
                    String handSign = ItemReader.getSign(e.getPlayer().getInventory().getItemInMainHand());
                    if (handSign != null && handSign.equals("BACKPACK_KEY")) {
                        Player p = e.getPlayer();
                        BackpackType type = BackpackType.getBackpackByItem(e.getPlayer().getInventory().getChestplate());
                        if (type != null && type != BackpackType.ENDER) {
                            int id = Var.getBackpackID(chest);

                            if (id == -1) {
                                new Backpack(plugin, type, p);
                            } else {
                                Backpack bp = plugin.backpacks.get(id);

                                if (bp == null) {
                                    p.sendMessage(Messages.PREFIX + "Dieser Rucksack ist (warum auch immer) nicht regestriert?");
                                    return;
                                }

                                bp.open(p);

                            }
                        } else if (type == BackpackType.ENDER) {
                            p.openInventory(p.getEnderChest());
                        }
                    }
                }
            } else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LANTERN) {

                ItemStack itemInHand = e.getPlayer().getInventory().getItemInMainHand();
                if (itemInHand.getItemMeta().hasDisplayName()) {
                    if (itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase("§cWeihrauchlaterne")) {

                        Player p = e.getPlayer();
                        p.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation().add(0, 1.5, 0), 8, 0.0D, 0, 0.01D, 0.01D);

                    }
                }

            }


        }
    }
}

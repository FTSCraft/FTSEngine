package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import net.sf.cglib.asm.$ByteVector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class PlayerInteractListener implements Listener {

    private Engine plugin;

    private ArrayList<Player> hornCooldown = new ArrayList<>();

    public PlayerInteractListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (plugin.mats.contains(e.getClickedBlock().getType()) || (e.getClickedBlock().getBlockData() instanceof Stairs)) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
                    plugin.getPlayer().get(e.getPlayer()).setSitting(e.getClickedBlock());
            }
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock().getBlockData() instanceof WallSign || e.getClickedBlock().getBlockData() instanceof Sign) {
                if (plugin.bretter.containsKey(e.getClickedBlock().getLocation())) {
                    plugin.bretter.get(e.getClickedBlock().getLocation()).getGui().open(e.getPlayer());
                    Brett brett = plugin.bretter.get(e.getClickedBlock().getLocation());
                    brett.checkForRunOut();
                }
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {

            if (e.getPlayer().getInventory().getChestplate() != null && e.getPlayer().getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE) {
                if (e.getPlayer().getInventory().getItemInMainHand() != null)
                    if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null) {
                        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§5Rucksack Schlüssel")) {
                            Player p = e.getPlayer();
                            ItemStack chest = e.getPlayer().getInventory().getChestplate();
                            if (BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()) != null && BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()) != BackpackType.ENDER) {
                                int id = plugin.getVar().getBackpackID(chest);

                                if (id == -1) {
                                    new Backpack(plugin, BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()), p);
                                } else {
                                    Backpack bp = plugin.backpacks.get(id);

                                    if (bp == null) {
                                        p.sendMessage("§eDieser Rucksack ist (warum auch immer) nicht regestriert?");
                                        return;
                                    }

                                    bp.open(p);

                                }
                            } else if (BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()) == BackpackType.ENDER) {
                                p.openInventory(p.getEnderChest());
                            }
                        }
                    }
            }

            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.NAUTILUS_SHELL) {

                ItemStack item = e.getPlayer().getInventory().getItemInMainHand();

                if (item.hasItemMeta()) {

                    if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Horn")) {

                        Player p = e.getPlayer();

                        if(hornCooldown.contains(p)) {
                            return;
                        }


                        hornCooldown.add(p);

                        Random random = new Random();
                        int r = random.nextInt(50) + 10;

                        p.playSound(p.getLocation(), Sound.EVENT_RAID_HORN, 100, r);

                        for (Entity n : p.getNearbyEntities(70, 70, 70)) {
                            if (n instanceof Player) {
                                Player playerInRadius = (Player) n;

                                playerInRadius.playSound(p.getLocation(), Sound.EVENT_RAID_HORN, 300, r);
                            }
                        }

                        Bukkit.getScheduler().runTaskLater(plugin, () -> {

                            hornCooldown.remove(p);

                        }, 20 * 2);

                    }

                }

            }


        }
    }
}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.logport.LogportManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class DamageListener implements Listener {

    private final Engine plugin;
    private final LogportManager logportManager;

    public DamageListener(Engine plugin) {
        this.plugin = plugin;
        logportManager = plugin.getLogportManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamageDo(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (((Player) e.getDamager()).getInventory().getItemInMainHand().getType() == Material.STICK) {
                if (((Player) e.getDamager()).getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§6Lanze")) {

                    if (plugin.getPlayer().get(e.getDamager()).getLanzenschlaege() > 2) {

                        Random random = new Random();
                        int r = random.nextInt(2) + 1;

                        if (r == 2) {

                            if (e.getEntity() instanceof Player) {
                                Player t = (Player) e.getEntity();
                                if (t.getVehicle() != null) {
                                    if (t.getVehicle().getPassengers().contains(t))
                                        t.getVehicle().removePassenger(t);
                                }

                                plugin.getPlayer().get(e.getDamager()).setLanzenschlaege(0);

                            }

                        } else
                            plugin.getPlayer().get(e.getDamager()).setLanzenschlaege(plugin.getPlayer().get(e.getDamager()).getLanzenschlaege() + 1);
                    } else
                        plugin.getPlayer().get(e.getDamager()).setLanzenschlaege(plugin.getPlayer().get(e.getDamager()).getLanzenschlaege() + 1);
                }
            }

        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (logportManager.hasTeleportTask(player)) {
                logportManager.cancelTeleport(player, Messages.PREFIX + ChatColor.RED + "Teleport abgebrochen: Du hast Schaden erlitten!");
            }
        }
    }
}

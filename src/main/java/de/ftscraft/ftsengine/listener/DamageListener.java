package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class DamageListener implements Listener {

    private Engine plugin;

    public DamageListener(Engine plugin) {
        this.plugin = plugin;
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
                            }
                        } else
                            plugin.getPlayer().get(e.getDamager()).setLanzenschlaege(plugin.getPlayer().get(e.getDamager()).getLanzenschlaege() + 1);
                    } else
                        plugin.getPlayer().get(e.getDamager()).setLanzenschlaege(plugin.getPlayer().get(e.getDamager()).getLanzenschlaege() + 1);
                }
            }
        }

        //PFERD DAMAGE - GESICHERT?

        if (e.getEntity() instanceof Horse) {
            Horse h = (Horse) e.getEntity();
            if (h.hasMetadata("FTSEngine.Horse"))
                if (plugin.getPferde().containsKey(h.getMetadata("FTSEngine.Horse").get(0).asInt())) {
                    if (plugin.getPferde().get(h.getMetadata("FTSEngine.Horse").get(0).asInt()).isLocked()) {
                        e.setCancelled(true);
                        e.getDamager().sendMessage("§eDieses Pferd ist gesichert!");
                    }
                }
        }

    }
}

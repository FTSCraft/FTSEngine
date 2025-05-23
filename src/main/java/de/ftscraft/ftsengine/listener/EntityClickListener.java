package de.ftscraft.ftsengine.listener;

import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityClickListener implements Listener {

    private final Engine plugin;

    public EntityClickListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e) {
        if (plugin.getReiter().contains(e.getPlayer())) {
            if (e.getRightClicked().getType() == EntityType.PHANTOM || e.getRightClicked().getType() == EntityType.WOLF) {
                e.getPlayer().sendMessage("§cne ne ne.");
                return;
            }
            e.getRightClicked().addPassenger(e.getPlayer());
            plugin.getReiter().remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();

        if (!plugin.getStreicheln().isActivePetter(p.getUniqueId())) return;

        Entity target = event.getRightClicked();

        if (!plugin.getStreicheln().validateTarget(p, target)) return;

        Location interactLocation = plugin.getStreicheln().extractInteractLocation(event, target);
        
        if (target instanceof Player) {
            plugin.getStreicheln().handlePlayerPet(p, (Player) target, interactLocation);
        } else if (target instanceof Animals) {
            plugin.getStreicheln().handleAnimalPet(p, (Animals) target, interactLocation);
        }

        event.setCancelled(true);
        plugin.getStreicheln().removeActivePetter(p.getUniqueId());
    }

    private final double SPEED_MODIFIER = 0.5;

    @EventHandler
    public void onMount(EntityMountEvent event) {

        if (event.getEntity() instanceof Player) {

            if (event.getMount().getType() == EntityType.PIG) {

                Pig pig = (Pig) event.getMount();
                AttributeInstance ms = pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                //noinspection DataFlowIssue
                ms.setBaseValue(ms.getBaseValue() + SPEED_MODIFIER);

            }

        }

    }

    // If a player sits on a pig, don't let the pig wander around
    @EventHandler
    public void onPigPath(EntityPathfindEvent event) {
        if (event.getEntity().getType() == EntityType.PIG) {

            Pig pig = (Pig) event.getEntity();
            if (!pig.getPassengers().isEmpty())
                event.setCancelled(true);

        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {

        if (event.getEntity() instanceof Player) {

            if (event.getDismounted().getType() == EntityType.PIG) {


                Pig pig = (Pig) event.getDismounted();

                AttributeInstance ms = pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                //noinspection DataFlowIssue
                ms.setBaseValue(ms.getBaseValue() - SPEED_MODIFIER);

            }

        }

    }

}

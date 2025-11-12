package de.ftscraft.ftsengine.listener;

import com.destroystokyo.paper.event.entity.EntityPathfindEvent;
import de.ftscraft.ftsengine.commands.emotes.CMDstreicheln;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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
        Player player = e.getPlayer();
        if (plugin.getReiter().contains(player)) {
            if(e.getRightClicked().hasMetadata("NPC")) {
                player.sendMessage(Messages.PREFIX + "§cDu kannst nicht auf einem NPC reiten.");
                return;
            }
            if (e.getRightClicked().getType() == EntityType.PHANTOM || e.getRightClicked().getType() == EntityType.WOLF || e.getRightClicked().getType() == EntityType.BEE) {
                player.sendMessage(Messages.PREFIX + "§cDu kannst hierauf nicht reichten.");
                return;
            }
            e.getRightClicked().addPassenger(player);
            plugin.getReiter().remove(player);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();
        Entity target = event.getRightClicked();
        CMDstreicheln.petEntity(p, target);
    }

    private final double SPEED_MODIFIER = 0.5;

    @EventHandler
    public void onMount(EntityMountEvent event) {

        if (event.getEntity() instanceof Player) {

            if (event.getMount().getType() == EntityType.PIG) {

                Pig pig = (Pig) event.getMount();
                AttributeInstance ms = pig.getAttribute(Attribute.MOVEMENT_SPEED);

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

                AttributeInstance ms = pig.getAttribute(Attribute.MOVEMENT_SPEED);

                //noinspection DataFlowIssue
                ms.setBaseValue(ms.getBaseValue() - SPEED_MODIFIER);

            }

        }

    }

}

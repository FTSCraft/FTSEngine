package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class EntityClickListener implements Listener
{

    private Engine plugin;

    public EntityClickListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent e)
    {
        if (plugin.getReiter().contains(e.getPlayer()))
        {
            if(e.getRightClicked().getType() == EntityType.PHANTOM || e.getRightClicked().getType() == EntityType.WOLF) {
                e.getPlayer().sendMessage("§cne ne ne.");
                return;
            }
            e.getRightClicked().setPassenger(e.getPlayer());
            plugin.getReiter().remove(e.getPlayer());
        }

        for (Player reiter : plugin.getReiter())
        {
            if (e.getPlayer().getPassengers().contains(reiter))
            {
                e.getPlayer().removePassenger(reiter);
            }
        }

    }

    private final double SPEED_MODIFIER = 0.5;

    @EventHandler
    public void onMount(EntityMountEvent event) {

        if(event.getEntity() instanceof Player) {

            Player p = (Player) event.getEntity();

            if(event.getMount().getType() == EntityType.PIG) {

                Pig pig = (Pig) event.getMount();

                AttributeInstance ms = pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                ms.setBaseValue(ms.getBaseValue() + SPEED_MODIFIER);

            }

        }

    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {

        if(event.getEntity() instanceof Player) {

            Player p = (Player) event.getEntity();

            if(event.getDismounted().getType() == EntityType.PIG) {


                Pig pig = (Pig) event.getDismounted();

                AttributeInstance ms = pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

                ms.setBaseValue(ms.getBaseValue() - SPEED_MODIFIER);

            }

        }

    }

}

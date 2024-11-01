package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    public ProjectileHitListener() {
        Bukkit.getPluginManager().registerEvents(this, Engine.getInstance());
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {

        if(event.getHitEntity() == null)
            return;

        if(event.getHitEntity() instanceof Player) {

            if(event.getEntity() instanceof Snowball) {

                if(!Bukkit.getOnlinePlayers().contains(event.getHitEntity()))
                    return;

                event.getHitEntity().setVelocity(event.getEntity().getVelocity().multiply(0.75).setY(0.5));

            }

        }

    }

}

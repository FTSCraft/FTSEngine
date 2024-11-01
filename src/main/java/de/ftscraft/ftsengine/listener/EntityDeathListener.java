package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    public EntityDeathListener() {
        Bukkit.getPluginManager().registerEvents(this, Engine.getInstance());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.getEntity().getType() == EntityType.ZOMBIE) {
            event.getDrops().removeIf(item -> item.getType() == Material.IRON_INGOT);
        }

    }

}

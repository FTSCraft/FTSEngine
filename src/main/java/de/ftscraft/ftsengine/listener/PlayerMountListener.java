package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.commands.CMDgehen;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;

public class PlayerMountListener implements Listener {

    private final Engine plugin;

    public PlayerMountListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onMount(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        Ausweis ausweis = plugin.getAusweis(player);
        if (ausweis == null) return;
        EntityType mountType = event.getMount().getType();
        if (mountType != EntityType.HORSE && mountType != EntityType.SKELETON_HORSE && mountType != EntityType.ZOMBIE_HORSE && mountType != EntityType.DONKEY) {
            return;
        }

        AbstractHorse horse = (AbstractHorse) event.getMount();
        var scaleAttribute = horse.getAttribute(Attribute.SCALE);
        if (scaleAttribute == null) {
            plugin.getLogger().warning("Horse from %s does not have scale attribute".formatted(player.getName()));
            return;
        }

        scaleAttribute.setBaseValue(ausweis.getHeight() / 200d);

    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDismount(EntityDismountEvent event) {
        EntityType mountType = event.getDismounted().getType();
        if (mountType != EntityType.HORSE && mountType != EntityType.SKELETON_HORSE && mountType != EntityType.ZOMBIE_HORSE && mountType != EntityType.DONKEY) {
            return;
        }

        AbstractHorse horse = (AbstractHorse) event.getDismounted();
        if(CMDgehen.speed.contains(horse)){
            CMDgehen.setHorseSpeed(horse, CMDgehen.getHorseDefaultSpeed(horse));
            CMDgehen.speed.remove(horse);
            event.getEntity().sendMessage(Messages.PREFIX + " Dein Pferd geht jetzt wieder normal");
        }
    }


}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.feature.courier.Brief;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.EngineUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final Engine plugin;

    public PlayerJoinListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        UUID uniqueId = p.getUniqueId();
        EngineUser user = plugin.getDatabaseHandler().getUserStorageManager().getOrCreateUser(uniqueId);
        plugin.getPlayer().put(uniqueId, user);

        if (plugin.getAusweisManager().hasAusweis(p)) {
            plugin.getAusweisManager().getAusweis(p).applySkinToPlayer(p);
        }

        if (plugin.getProtocolManager() != null)
            Engine.getInstance().sendTablistHeaderAndFooter(p, " §cHeutiger Tipp: \nGeht voten!", "");

        //Map
        p.getInventory().getItemInMainHand();
        if (p.getInventory().getItemInMainHand().getType() == Material.FILLED_MAP) {
            ItemStack itemMap = p.getInventory().getItemInMainHand();
            Brief brief = plugin.briefe.get((int) itemMap.getDurability());
            if (brief != null) {
                brief.loadMap(itemMap);
            }
        }
    }

}

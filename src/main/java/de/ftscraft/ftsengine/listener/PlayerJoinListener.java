package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {

    private final Engine plugin;

    public PlayerJoinListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        FTSUser user = new FTSUser(plugin, p);
        plugin.getPlayer().put(e.getPlayer(), user);

        plugin.sendTablistHeaderAndFooter(p, " §cHeutiger Tipp: \nGeht voten!", "\n" +
                "§7[RP] §r- RP-Modus");

        //Map
        if (p.getInventory().getItemInMainHand() != null) {
            if (p.getInventory().getItemInMainHand().getType() == Material.FILLED_MAP) {
                ItemStack itemMap = p.getInventory().getItemInMainHand();
                Brief brief = plugin.briefe.get((int) itemMap.getDurability());
                if (brief != null) {
                    brief.loadMap(itemMap);
                }
            }
        }
    }

}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class ItemSwitchListener implements Listener {

    private Engine plugin;

    public ItemSwitchListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent e)
    {
        Inventory inv = e.getPlayer().getInventory();
        if (inv.getItem(e.getNewSlot()) == null)
            return;
        if (inv.getItem(e.getNewSlot()).getType() == Material.FILLED_MAP) {
            ItemStack itemMap = inv.getItem(e.getNewSlot());
            Brief brief = plugin.briefe.get(((MapMeta) itemMap.getItemMeta()).getMapId());
            if (brief != null) {
                brief.loadMap(itemMap);
            }
        }

    }

}

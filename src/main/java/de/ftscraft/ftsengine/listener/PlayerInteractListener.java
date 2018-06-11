package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener
{

    private Engine plugin;

    public PlayerInteractListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        if (e.getPlayer().getInventory().getChestplate() != null && e.getPlayer().getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE)
        {
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null && e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR)
            {
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§5Rucksack Schlüssel"))
                {
                    Player p = e.getPlayer();
                    ItemStack chest = e.getPlayer().getInventory().getChestplate();
                    if(BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()) != null)
                    {
                        int id = plugin.getVar().getBackpackID(chest);

                        if (id == -1)
                        {
                            new Backpack(plugin, BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()), p);
                        } else {
                            Backpack bp = plugin.backpacks.get(id);

                            if(bp == null) {
                                p.sendMessage("§eDieser Rucksack ist (warum auch immer) nicht regestriert?");
                                return;
                            }

                            bp.open(p);

                        }
                    }
                }
            }
        }
    }

}

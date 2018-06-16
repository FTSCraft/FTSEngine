package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {

            if (plugin.mats.contains(e.getClickedBlock().getType()))
            {
                if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
                    plugin.getPlayer().get(e.getPlayer()).setSitting(e.getClickedBlock());
            }
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {
            if (e.getClickedBlock().getType().equals(Material.SIGN) || e.getClickedBlock().getType().equals(Material.SIGN_POST) || e.getClickedBlock().getType().equals(Material.WALL_SIGN))
            {
                if (plugin.bretter.containsKey(e.getClickedBlock().getLocation()))
                {
                    plugin.bretter.get(e.getClickedBlock().getLocation()).getGui().open(e.getPlayer());
                    Brett brett = plugin.bretter.get(e.getClickedBlock().getLocation());
                    brett.checkForRunOut();
                }
            }
        }

        if (e.getPlayer().getInventory().getChestplate() != null && e.getPlayer().getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE)
        {
            if (e.getPlayer().getInventory().getItemInMainHand() != null)
                if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null)
                {
                    if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§5Rucksack Schlüssel"))
                    {
                        Player p = e.getPlayer();
                        ItemStack chest = e.getPlayer().getInventory().getChestplate();
                        if (BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()) != null && BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()) != BackpackType.ENDER)
                        {
                            int id = plugin.getVar().getBackpackID(chest);

                            if (id == -1)
                            {
                                new Backpack(plugin, BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()), p);
                            } else
                            {
                                Backpack bp = plugin.backpacks.get(id);

                                if (bp == null)
                                {
                                    p.sendMessage("§eDieser Rucksack ist (warum auch immer) nicht regestriert?");
                                    return;
                                }

                                bp.open(p);

                            }
                        } else if (BackpackType.getBackpackByName(chest.getItemMeta().getDisplayName()) == BackpackType.ENDER)
                        {
                            p.openInventory(p.getEnderChest());
                        }
                    }
                }
        }
    }

}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;


// Drops all contents of a backpack when it breaks
public class BackpackBreakListener implements Listener {

    private final Engine plugin;

    public BackpackBreakListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemBreak(PlayerItemBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack brokenItem = event.getBrokenItem();
        
        BackpackType backpackType = BackpackType.getBackpackByItem(brokenItem);
        if (backpackType == null) {
            return;
        }
        
        if (!brokenItem.hasItemMeta() || !brokenItem.getItemMeta().hasLore()) {
            return;
        }
        
        int backpackId = Var.getBackpackID(brokenItem);
        if (backpackId == -1) {
            return;
        }

        Backpack backpack = plugin.backpacks.get(backpackId);
        if (backpack == null) {
            player.sendMessage(Messages.PREFIX + "Dein Rucksack ist kaputt gegangen, aber der Inhalt konnte nicht gefunden werden?");
            return;
        }

        Location dropLocation = player.getLocation();
        ItemStack[] contents = backpack.getInventory().getContents();
        
        for (ItemStack item : contents) {
            if (item != null) {
                dropLocation.getWorld().dropItemNaturally(dropLocation, item);
            }
        }

        backpack.getInventory().clear();
        plugin.backpacks.remove(backpackId);
        
        player.sendMessage(Messages.PREFIX + "Dein Rucksack ist kaputt gegangen! Der gesamte Inhalt wurde fallen gelassen.");
    }
} 
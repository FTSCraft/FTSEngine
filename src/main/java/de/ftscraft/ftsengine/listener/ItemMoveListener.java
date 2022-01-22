package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class ItemMoveListener implements Listener
{

    private final Engine plugin;

    public ItemMoveListener(Engine plugin)
    {
        this.plugin = plugin;
    }


}

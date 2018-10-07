package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.pferd.Pferd;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadListener implements Listener {


    private Engine plugin;

    public ChunkUnloadListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        try {
            for (Pferd a : plugin.pferde.values()) {
                if (a.location.getChunk().equals(e.getChunk())) {
                    a.removeHorse();
                }
            }
        }catch (Exception ignored) {

        }

    }

}

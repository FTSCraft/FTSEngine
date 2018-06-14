package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.FTSMapRenderer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

public class MapLoadListener implements Listener
{

    private Engine plugin;

    public MapLoadListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onMap(MapInitializeEvent e)
    {
        MapView map = e.getMap();
        for (MapRenderer mr : map.getRenderers())
            map.removeRenderer(mr);
        map.setScale(MapView.Scale.FARTHEST);
        String msg = plugin.briefMsg.get(0).replaceAll("ä", "ae").replaceAll("ö", "oe").
                replaceAll("ü", "ue").
                replaceAll("ß", "ss");

        map.addRenderer(new FTSMapRenderer(msg));
        plugin.briefMsg.remove(0);

    }

}

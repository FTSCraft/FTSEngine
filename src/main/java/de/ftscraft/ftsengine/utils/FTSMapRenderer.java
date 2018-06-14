package de.ftscraft.ftsengine.utils;

import org.bukkit.entity.Player;
import org.bukkit.map.*;

public class FTSMapRenderer extends org.bukkit.map.MapRenderer
{

    private String msg;

    public FTSMapRenderer(String msg)
    {
        this.msg = msg;
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player)
    {
        String test = "";
        int i2 = 0;
        int il = 0;
        int zeilen = 0;
        for(int i = 0; i < msg.length(); i++) {
            test += msg.charAt(i);
            i2++;
            il = i;
            if(i2 == 23) {
                il -= i2;
                zeilen++;
                i2 = 0;
                mapCanvas.drawText(1,zeilen*8, MinecraftFont.Font, test);
                test = "";
            }
        }
        zeilen++;
        if(il < 23)
            mapCanvas.drawText(1,zeilen * 8,MinecraftFont.Font, test);

        mapCanvas.setCursors(new MapCursorCollection());
    }

}

package de.ftscraft.ftsengine.feature.courier;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class FTSMapRenderer extends org.bukkit.map.MapRenderer {

    private final String msg;
    private final String date;
    private final boolean debug = true;

    final Brief brief;

    public FTSMapRenderer(int id, Engine plugin) {
        brief = plugin.briefe.get(id);
        this.msg = brief.msg;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(brief.creation);
        date = cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH) + 1);
    }

    @Override
    public void render(@NotNull MapView mapView, @NotNull MapCanvas mapCanvas, @NotNull Player player) {

        for (int x = 0; x < 150; x++) {
            for (int y = 0; y < 150; y++) {
                mapCanvas.setPixel(x, y, (byte) 145);
                if (y == 10)
                    mapCanvas.setPixel(x, y, (byte) 120);
            }
        }

        mapCanvas.drawText(100, 2, MinecraftFont.Font, date);

        final int SPACE = 12;

        String[] woerter = msg.split(" ");
        String text = "";
        int zeilen = 0;

        for (int i = 0; i < woerter.length; i++) {
            if (woerter[i].length() + text.length() < 20) {
                text = text + " " + woerter[i];
                if (i + 1 == woerter.length)
                    try {
                        mapCanvas.drawText(0, zeilen * 10 + SPACE, MinecraftFont.Font, text);
                    } catch (IllegalArgumentException ignored) {
                        player.sendMessage(Messages.PREFIX + "Da sind Zeichen im Brief die nicht gehen!");
                        brief.error();
                        return;
                    }
            } else {
                text = text + " " + woerter[i];
                mapCanvas.drawText(0, zeilen * 10 + SPACE, MinecraftFont.Font, text);
                zeilen++;
                text = " ";
            }
        }

        mapCanvas.setCursors(new MapCursorCollection());
    }

}

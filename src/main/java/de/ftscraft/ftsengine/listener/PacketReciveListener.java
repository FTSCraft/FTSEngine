package de.ftscraft.ftsengine.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketReciveListener implements Listener {

    private final Engine plugin;

    public PacketReciveListener(Engine plugin) {
        this.plugin = plugin;
        onPacketRecive();
    }

    public void onPacketRecive() {

        /*plugin.getProtocolManager().addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.BLOCK_DIG) {

            @Override
            public void onPacketSending(PacketEvent event) {
                if(event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BOW){
                    event.setCancelled(true);
                }
            }

        });*/

    }

}

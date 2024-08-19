package de.ftscraft.ftsengine.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class PlayerJoinListener implements Listener {

    private final Engine plugin;

    public PlayerJoinListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        FTSUser user = new FTSUser(plugin, p);
        plugin.getPlayer().put(e.getPlayer(), user);

        if (plugin.getProtocolManager() != null)
            sendTablistHeaderAndFooter(p, " §cHeutiger Tipp: \nGeht voten!", "");

        //Map
        p.getInventory().getItemInMainHand();
        if (p.getInventory().getItemInMainHand().getType() == Material.FILLED_MAP) {
            ItemStack itemMap = p.getInventory().getItemInMainHand();
            Brief brief = plugin.briefe.get((int) itemMap.getDurability());
            if (brief != null) {
                brief.loadMap(itemMap);
            }
        }
    }

    public void sendTablistHeaderAndFooter(Player p, String header, String footer) {
        PacketContainer pc = plugin.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        pc.getChatComponents().write(0, WrappedChatComponent.fromText(header)).write(1, WrappedChatComponent.fromText(footer));

        try {
            plugin.getProtocolManager().sendServerPacket(p, pc);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.WARNING, "Was not able to send header and footer package to player.");
        }
    }

}

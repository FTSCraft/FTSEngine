package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignWriteListener implements Listener {

    private final Engine plugin;

    public SignWriteListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onWirte(SignChangeEvent event) {

        //If player wants to create a briefkasten
        if (event.getLine(0).equalsIgnoreCase("[Briefkasten]")) {

            Player p = event.getPlayer();

            Block block = event.getBlock();

            if (block.getState() instanceof Sign) {
                BlockData data = block.getBlockData();
                if (data instanceof Directional directional) {
                    Block blockBehind = block.getRelative(directional.getFacing().getOppositeFace());

                    if (blockBehind.getState() instanceof Container) {

                        if (plugin.briefkasten.containsKey(p.getUniqueId())) {

                            Briefkasten briefkasten = plugin.briefkasten.get(p.getUniqueId());

                            p.sendMessage("§cDu hast bereits einen Briefkasten, zerstöre diesen, bevor du einen neuen erstellst");
                            p.sendMessage("§cFalls du vergessen hast wo er ist, hier die Koordinaten:");
                            p.sendMessage("§cX: " + briefkasten.getLocation().getX() + " Y: " + briefkasten.getLocation().getY() + " Z: " + briefkasten.getLocation().getZ());
                            p.sendMessage("§cFalls da keiner ist, kannst du auch /brief removekasten eingeben");
                            event.setCancelled(true);

                            return;

                        }

                        new Briefkasten(plugin, blockBehind.getLocation(), p.getUniqueId());

                        event.setLine(0, "§7[§2Briefkasten§7]");
                        event.setLine(1, p.getName());

                        p.sendMessage(Messages.PREFIX + "Du hast deinen Briefkasten erfolgreich erstellt!");

                    }

                }
            } else p.sendMessage("§cHallo");


        }

        //Schwarzes Brett
        if (event.getLine(0).equalsIgnoreCase("Schwarzes Brett"))
            if (event.getLine(1).length() > 3) {
                String name = event.getLine(1);
                boolean admin = false;
                boolean global = false;
                if (event.getLine(3).equalsIgnoreCase("Admin")) {
                    if (event.getPlayer().hasPermission("ftsengine.brett.admin")) {
                        admin = true;
                    } else {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(Messages.PREFIX + "Du darfst keine Admin-Schilder erstellen!");
                        return;
                    }
                }
                if (name.equalsIgnoreCase("Global")) {
                    if (event.getPlayer().hasPermission("ftsengine.brett.admin")) {
                        global = true;
                    } else {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(Messages.PREFIX + "Du darfst keine Admin-Schilder erstellen!");
                        return;
                    }
                }
                if (event.getLine(2).equalsIgnoreCase("")) {


                    org.bukkit.block.Sign sign = (org.bukkit.block.Sign) event.getBlock().getState();
                    event.setLine(0, "§4Schwarzes Brett");
                    event.setLine(2, "");

                    if (admin)
                        event.setLine(3, "§7[Admin]");
                    else event.setLine(3, "");

                    if (global)
                        event.setLine(1, "§bGlobal");

                    boolean firstGlobal = true;

                    for (Brett all : plugin.bretter.values()) {
                        if (all.getName().equals(name)) {
                            if (global) {
                                firstGlobal = false;
                                break;
                            }
                            event.getPlayer().sendMessage(Messages.PREFIX + "Dieser Name ist schon vorhanden. Probier ein anderen");
                            event.setCancelled(true);
                            return;
                        }
                    }

                    if (firstGlobal)
                        plugin.bretter.put(event.getBlock().getLocation(), new Brett(sign, event.getBlock().getLocation(), event.getPlayer().getUniqueId(), name, admin, plugin));
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du hast das Schwarze Brett erfolgreich erstellt");

                }
            } else
                event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Der Name (2. Zeile) muss mind. 4 Zeichen haben!");
        if (!(event.getPlayer().hasPermission("blackboard.create"))) {
            if (event.getLine(0).equalsIgnoreCase("&4Schwarzes Brett")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Mach sowas nicht! Das könnte Fehler verursachen!");
            }
        }
    }

}

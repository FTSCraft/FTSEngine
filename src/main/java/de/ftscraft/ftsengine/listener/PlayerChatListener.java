package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import de.ftscraft.ftsengine.utils.Ausweis;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;

public class PlayerChatListener implements Listener {

    private final Engine plugin;

    public PlayerChatListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {


        if (plugin.playerBrettNote.containsKey(e.getPlayer())) {
            Player p = e.getPlayer();

            if (plugin.playerBrettNote.get(p) != null) {
                if (!(plugin.playerBrettNote.get(p).isTitle())) {
                    e.setCancelled(true);
                    if (e.getMessage().length() > 4) {
                        BrettNote bn = plugin.playerBrettNote.get(p);
                        bn.setTitle(e.getMessage());
                        p.sendMessage("§7[§bSchwarzes Brett§7] Perfekt! Jetzt bitte die Beschreibung (mind. 10 Zeichen)");
                    } else
                        p.sendMessage("§7[§bSchwarzes Brett§7] Der Titel muss mind. 5 Zeichen haben.");
                } else if (!(plugin.playerBrettNote.get(p).isContent())) {
                    e.setCancelled(true);
                    if (e.getMessage().length() > 9) {
                        BrettNote bn = plugin.playerBrettNote.get(p);
                        bn.setContent(e.getMessage());
                        bn.addToBrett();
                        //Geld
                        OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());
                        int price;
                        if (bn.getBrett().isAdmin())
                            price = 0;
                        else price = 0;
                        if (plugin.getEcon().has(op, price)) {
                            plugin.getEcon().withdrawPlayer(op, price);
                            OfflinePlayer c = Bukkit.getOfflinePlayer(bn.getBrett().getCreator());
                            plugin.getEcon().depositPlayer(c, price);
                            p.sendMessage("§7[§bSchwarzes Brett§7] Ok! Die Notiz wurde erstellt");

                            if (bn.getBrett().isAdmin()) {
                                Ausweis ausweis = plugin.getAusweis(p);
                                plugin.getServer().broadcastMessage("§7[§bMarktschreier§7] Es wurde etwas neues am Schwarzen Brett in Lohengrin von §c" + ausweis.getFirstName() + " " + ausweis.getLastName() + " §7mit dem Titel §c" + bn.getTitle() + " §7angeheftet");
                            }

                        } else p.sendMessage("§7Du hast nicht genug Geld!");
                    } else
                        p.sendMessage("§7Bitte mind. 10 Zeichen in der Beschreibung!");
                }
            }
        }

        return;
    }
        /*
        Player p = e.getPlayer();

        FTSUser user = plugin.getPlayer().get(p);
        user.getChatChannel().sendMessage(p, e.getMessage());

        e.setCancelled(true);
        */


}

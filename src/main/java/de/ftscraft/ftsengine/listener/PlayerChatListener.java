package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.feature.brett.BrettNote;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final Engine plugin;

    final String BRETT_PREFIX = "§7[§bSchwarzes Brett§7] ";

    public PlayerChatListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        handleSchwarzesBrett(e);
    }

    private void handleSchwarzesBrett(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        if (!plugin.playerBrettNote.containsKey(p)) {
            return;
        }

        BrettNote brettNote = plugin.playerBrettNote.get(p);
        if (brettNote == null) {
            return;
        }

        if (e.getMessage().equalsIgnoreCase("exit")) {
            p.sendMessage(BRETT_PREFIX + "Du hast erfolgreich abgebrochen!");
            plugin.playerBrettNote.remove(p);
            return;
        }

        e.setCancelled(true);

        if (!(brettNote.hasTitle())) {

            if (e.getMessage().length() > 4) {
                brettNote.setTitle(e.getMessage());
                p.sendMessage(BRETT_PREFIX + "Perfekt! Jetzt bitte die Beschreibung (mind. 10 Zeichen)");
            } else {
                p.sendMessage(BRETT_PREFIX + "Der Titel muss mind. 5 Zeichen haben.");
            }

        } else if (!(brettNote.hasContent())) {

            if (e.getMessage().length() > 9) {
                brettNote.setContent(e.getMessage());
                brettNote.addToBrett();

                p.sendMessage(BRETT_PREFIX + "Ok! Die Notiz wurde erstellt");

                if (brettNote.getBrett().isAdmin()) {
                    Ausweis ausweis = plugin.getAusweis(p);
                    plugin.getServer().broadcastMessage("§7[§bMarktschreier§7] Es wurde etwas neues am Schwarzen Brett in Xantia von §c" + ausweis.getFirstName() + " " + ausweis.getLastName() + " §7mit dem Titel §c" + brettNote.getTitle() + " §7angeheftet");
                }

                plugin.playerBrettNote.remove(p);

            } else {
                p.sendMessage("§7Bitte mind. 10 Zeichen in der Beschreibung!");
            }

        }
    }


}

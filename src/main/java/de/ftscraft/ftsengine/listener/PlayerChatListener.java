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

public class PlayerChatListener implements Listener
{

    private Engine plugin;

    public PlayerChatListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {


        if (plugin.playerBrettNote.containsKey(e.getPlayer()))
        {
            Player p = e.getPlayer();

            if (plugin.playerBrettNote.get(p) != null)
            {
                if (!(plugin.playerBrettNote.get(p).isTitle()))
                {
                    e.setCancelled(true);
                    if (e.getMessage().length() < 51 && e.getMessage().length() > 4)
                    {
                        BrettNote bn = plugin.playerBrettNote.get(p);
                        bn.setTitle(e.getMessage());
                        p.sendMessage("§7[§bSchwarzes Brett§7] Perfekt! Jetzt bitte die Beschreibung (max. 150, mind. 10 Zeichen)");
                    } else
                        p.sendMessage("§7[§bSchwarzes Brett§7] Der Titel muss mind. 5 Zeichen haben und darf max. 50 Zeichen haben");
                } else if (!(plugin.playerBrettNote.get(p).isContent()))
                {
                    e.setCancelled(true);
                    if (e.getMessage().length() < 151 && e.getMessage().length() > 9)
                    {
                        BrettNote bn = plugin.playerBrettNote.get(p);
                        bn.setContent(e.getMessage());
                        p.sendMessage("§7[§bSchwarzes Brett§7] Ok! Die Notitz wurde erstellt");
                        bn.addToBrett();
                        //Geld
                        OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());
                        if(plugin.getEcon().has(op, 1))
                        {
                            plugin.getEcon().withdrawPlayer(op, 1);
                            OfflinePlayer c = Bukkit.getOfflinePlayer(bn.getBrett().getCreator());
                            plugin.getEcon().depositPlayer(c, 1);
                            p.sendMessage("§eDu hast die Notitz erfolgreich erstellt!");
                        } else p.sendMessage("§7Du hast nicht genug Geld!");
                    }
                }
            }
        }

        if(e.getMessage().startsWith(String.valueOf('*'))) {

            String[] msgs = e.getMessage().split(" ");

            for(int i = 0; i < msgs.length; i++) {
                for(Player a : Bukkit.getOnlinePlayers())
                {
                    if (plugin.hasAusweis(a))
                    {
                        if (a.getName().equalsIgnoreCase(msgs[i]))
                        {
                            msgs[i] = plugin.getAusweis(a).getFirstName() + " " + plugin.getAusweis(a).getLastName();
                        }
                    }
                }
            }

            String msg;

            Ausweis a = plugin.getAusweis(e.getPlayer());

            if(a == null) {
                e.getPlayer().sendMessage("§cBitte erstell dir erst einen Ausweis");
                return;
            }

            msgs[0] = msgs[0].substring(1);

            msg = "§e"+a.getFirstName()+" " +a.getLastName() + " ";


            //msgs[0].replace("*", "");

            for(String m : msgs) {
                msg += m + " ";
            }

            e.setCancelled(true);

            for(Entity en : e.getPlayer().getNearbyEntities(15,15,15)) {
                if(en instanceof Player) {
                    en.sendMessage(msg);
                }
            }

            e.getPlayer().sendMessage(msg);
            return;
        }
        /*
        Player p = e.getPlayer();

        FTSUser user = plugin.getPlayer().get(p);
        user.getChatChannel().sendMessage(p, e.getMessage());

        e.setCancelled(true);
        */

    }

}

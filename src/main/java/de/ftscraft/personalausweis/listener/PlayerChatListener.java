package de.ftscraft.personalausweis.listener;

import de.ftscraft.personalausweis.chat.ChatChannels;
import de.ftscraft.personalausweis.main.Engine;
import de.ftscraft.personalausweis.utils.Ausweis;
import org.bukkit.Bukkit;
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

        Player p = e.getPlayer();
        ChatChannels ch = plugin.getChats().get(p);
        if(ch == ChatChannels.FLÜSTERN) {
            plugin.getVar().ChatInRoleplay(p, e.getMessage());
        } else if(ch == ChatChannels.HANDEL) {
            plugin.getVar().ChatInHandel(p, e.getMessage());
        } else if(ch == ChatChannels.RUFEN) {
            plugin.getVar().ChatInRufen(p, e.getMessage());
        } else if(ch == ChatChannels.ROLEPLAY) {
            plugin.getVar().ChatInRoleplay(p, e.getMessage());
        }



    }

}

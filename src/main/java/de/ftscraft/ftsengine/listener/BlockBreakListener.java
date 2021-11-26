package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;

public class BlockBreakListener implements Listener
{

    public Engine plugin;

    public BlockBreakListener(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {

        //Schwarzes Brett und Briefkasten
        if (event.getBlock().getBlockData() instanceof WallSign || event.getBlock().getBlockData() instanceof org.bukkit.block.data.type.Sign)
        {
            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase("§4Schwarzes Brett"))
            {
                if (!(event.getPlayer().hasPermission("blackboard.remove")) && !plugin.bretter.get(sign.getLocation()).getCreator().toString().equals(event.getPlayer().getUniqueId().toString()))
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du darfst das nicht kaputt machen!");
                } else
                {
                    event.getPlayer().sendMessage("§7[§bSchwarzes Brett§7] Du hast erfolgreich das Schwarze Brett entfernt");
                    Brett brett = plugin.bretter.get(event.getBlock().getLocation());
                    brett.remove();
                }
            }

            //Briefkasten

            if(sign.getLine(0).equalsIgnoreCase("§7[§2Briefkasten§7]")) {

                String tName = sign.getLine(1);

                OfflinePlayer op = Bukkit.getOfflinePlayer(tName);

                if(op == null) {

                    event.setCancelled(true);

                    event.getPlayer().sendMessage("§cBitte kontaktiere einen Admin falls das dein Briefkasten ist. Stichwort: UUID nicht vorhanden");
                    return;
                }

                if(op.getName().equals(event.getPlayer().getName())) {

                    if(!plugin.briefkasten.containsKey(op.getUniqueId())) {
                        event.setCancelled(false);
                        return;
                    }

                    plugin.briefkasten.remove(op.getUniqueId());

                    File file = new File(plugin.getDataFolder() + "//briefkasten//" + event.getPlayer().getUniqueId().toString()+ ".yml");

                    file.getName();

                    file.delete();

                    event.getPlayer().sendMessage("§cDu hast deinen Briefkasten erfolgreich entfernt!");

                } else {
                    event.getPlayer().sendMessage("§cDas ist nicht dein Briefkasten!");
                }


            }

        } else if(event.getBlock().getType() == Material.CHEST) {

            Block block = event.getBlock();
            BlockData blockData = block.getBlockData();
            Directional directional = (Directional)blockData;

            boolean briefkasten = false;

            for (BlockFace face : directional.getFaces()) {
                Block a = block.getRelative(face.getOppositeFace());

                if(a.getState() instanceof Sign) {

                    Sign sign = (Sign) a.getState();

                    if(sign.getLine(0).equalsIgnoreCase("§7[§2Briefkasten§7]")) {

                        briefkasten = true;
                        break;
                    }

                }

            }

            if(briefkasten) {

                event.getPlayer().sendMessage("§cFalls das dein Briefkasten ist, zerstöre erst das Schild");

                event.setCancelled(true);

            }

        }
    }

}

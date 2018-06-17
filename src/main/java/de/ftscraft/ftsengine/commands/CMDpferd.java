package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import de.ftscraft.ftsengine.pferd.Pferd;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

public class CMDpferd implements CommandExecutor
{

    private Engine plugin;

    public CMDpferd(Engine engine)
    {
        this.plugin = engine;
        plugin.getCommand("pferd").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if (!(cs instanceof Player))
        {
            cs.sendMessage(plugin.msgs.PREFIX);
            return true;
        }

        Player p = (Player) cs;

        if (args.length > 0)
        {
            String sub = args[0];

            if (sub.equalsIgnoreCase("beanspruchen"))
            {
                if (args.length == 2)
                {
                    if (p.getVehicle() != null)
                    {
                        if (p.getVehicle() instanceof Horse)
                        {
                            if (!plugin.isRegistered((Horse) p.getVehicle()))
                            {
                                FTSUser user = plugin.getPlayer().get(p);
                                if (user.getPferde().size() < 4)
                                {
                                    Horse horse = (Horse) p.getVehicle();
                                    horse.setCustomName(args[1]);
                                    Pferd pf = new Pferd(plugin, horse.getUniqueId(), p.getWorld(), p.getUniqueId(), false, -1, 0, args[1], false);
                                    p.sendMessage(plugin.msgs.NOW_YOUR_HORSE);
                                    user.addPferd(pf);
                                } else p.sendMessage("§eDu hast schon zu viele Pferde!");
                            } else p.sendMessage("§eDieses Pferd ist schon regestriert!");
                        } else p.sendMessage("§eDas ist kein Pferd!");
                    } else p.sendMessage("§eDu musst auf ein Pferd sitzen");
                } else plugin.getVar().sendPferdHelpMsg(p);
            } else if (sub.equalsIgnoreCase("teleportieren"))
            {
                if (p.getVehicle() == null)
                {
                    FTSUser user = plugin.getPlayer().get(p);
                    Pferd pferd = user.getChosedPferd();
                    if (pferd != null)
                        pferd.teleport(p);
                    else p.sendMessage("§eDu hast kein Pferd ausgewählt");
                } else p.sendMessage("§eDu bist schon auf einem Gefährt");
            } else if (sub.equalsIgnoreCase("verkaufen"))
            {
                if (p.getVehicle() != null)
                {
                    if (args.length == 2)
                    {
                        int price;
                        try
                        {
                            price = Integer.valueOf(args[1]);
                        } catch (NumberFormatException e)
                        {
                            p.sendMessage("§eBitte verwende eine Gültige Zahl");
                            return true;
                        }
                        if (price <= 0)
                        {
                            p.sendMessage("§eDer Betrag darf nicht negativ sein!");
                            return true;
                        }
                        Pferd pferd = plugin.getPferde().get(p.getVehicle().getUniqueId());
                        if (pferd == null)
                        {
                            p.sendMessage("§eDas Pferd muss regestriert sein!");
                            return true;
                        }
                        if (!pferd.getOwner().toString().equalsIgnoreCase(p.getUniqueId().toString()))
                        {
                            p.sendMessage("§eDir muss das Pferd gehören!");
                            return true;
                        }

                        pferd.setPrice(price);
                        p.sendMessage("§cDu hast es erfolgreich zu verkauf gestellt");

                    } else p.sendMessage("§eDu musst ein Preis angeben");
                } else p.sendMessage("§eDu musst auf ein Pferd sitzen");
            } else if (sub.equalsIgnoreCase("kaufen"))
            {
                if (p.getVehicle() != null)
                {
                    Pferd pferd = plugin.getPferde().get(p.getVehicle().getUniqueId());
                    FTSUser user = plugin.getPlayer().get(p);
                    if (user.getPferde().size() <= 3)
                    {
                        if (pferd.getPrice() != -1)
                        {
                            int price = pferd.getPrice();
                            if (plugin.getEcon().has(p, price))
                            {
                                plugin.getEcon().withdrawPlayer(p, price);
                                pferd.setOwner(p.getUniqueId(), p);
                            } else p.sendMessage("§eDu hast nicht genug Geld!");
                        } else p.sendMessage("§eDas Pferd steht nicht zum verkauf");
                    } else p.sendMessage("§eDu hast zu viele Pferde");
                } else p.sendMessage("§eDu musst auf ein Pferd sitzen");
            } else if (sub.equalsIgnoreCase("abstellen"))
            {
                if (p.getVehicle() != null)
                {
                    if (p.getVehicle() instanceof Horse)
                    {
                        FTSUser user = plugin.getPlayer().get(p);
                        if (user.ownsHorse((Horse) p.getVehicle()))
                        {
                            Pferd pferd = plugin.getPferde().get(p.getVehicle().getUniqueId());
                            pferd.lock(p);
                            p.getVehicle().removePassenger(p);
                        } else p.sendMessage("§eDieses Pferd gehört dir nicht");
                    } else p.sendMessage("§eDu musst auf ein Pferd von die sitzen");
                } else p.sendMessage("§eDu musst auf ein Pferd sitzen");
            } else if (sub.equalsIgnoreCase("auswählen"))
            {
                if (args.length == 2)
                {
                    FTSUser user = plugin.getPlayer().get(p);
                    user.setChosedPferd(args[1]);
                } else plugin.getVar().sendPferdHelpMsg(p);
            } else if (sub.equalsIgnoreCase("list"))
            {
                FTSUser user = plugin.getPlayer().get(p);
                p.sendMessage("§eDu hast folgende Pferde:");
                for (Pferd pferd : user.getPferde())
                {
                    p.sendMessage("§e- " + pferd.getName());
                }
            } else plugin.getVar().sendPferdHelpMsg(p);

        } else plugin.getVar().sendPferdHelpMsg(p);
        return false;
    }

}

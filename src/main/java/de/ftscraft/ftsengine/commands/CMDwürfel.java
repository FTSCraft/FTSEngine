package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Var;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;


public class CMDwürfel implements CommandExecutor
{

    private Engine plugin;

    public CMDwürfel(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("würfel").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        Player p = (Player)cs;

        if(args.length == 0)
        {
            Random random = new Random();
            int n = random.nextInt(6) + 1;
            p.sendMessage("§7Du hat ne' §c"+n+" gewürfelt");
            for(Entity e : p.getNearbyEntities(15,15,15))
                if(e instanceof Player)
                    e.sendMessage("§7Der Spieler §c"+p.getName()+" §7hat eine §c"+n+" §7gewürfelt");


        } else if(args.length == 1) {
            int z;
            try {
                z = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                p.sendMessage(plugin.msgs.USAGE_NUMBER);
                return true;
            }

            Random random = new Random();
            int r = random.nextInt(4) + 1;

            if(r == 1 || r == 2) {
                p.sendMessage("§7Du hast ne' §c"+z+" §7gewürfelt!");
                for(Entity e : p.getNearbyEntities(15,15,15))
                    if(e instanceof Player)
                        e.sendMessage("§7Der Spieler §c"+p.getName()+" §7hat eine §c"+z+" §7gewürfelt");
            } else if(r == 3) {
                int n = random.nextInt(6) + 1;
                while(n == z) {
                    n = random.nextInt(6) + 1;
                }
                p.sendMessage("§7Das zinken ist dir nicht gelungen. Du hat ne' §c"+n+" gewürfelt");
                for(Entity e : p.getNearbyEntities(15,15,15))
                    if(e instanceof Player)
                        e.sendMessage("§7Der Spieler §c"+p.getName()+" §7hat eine §c"+n+" §7gewürfelt");
            } else if(r == 4) {
                int n = random.nextInt(6) + 1;
                while(n == z) {
                    n = random.nextInt(6) + 1;
                }
                p.sendMessage("§7Das zinken ist dir nicht gelungen und die anderen haben es mitbekommen! Du hast ne' §c"+n+"§7 gewürfelt");
                for(Entity e : p.getNearbyEntities(15,15,15))
                    if(e instanceof Player)
                        e.sendMessage("§7Du bemerkst wie "+p.getName() +" versucht hat zu zinken. Der Spieler §c"+p.getName()+" §7hat eine §c"+n+" §7gewürfelt");
            }

        } else {
            cs.sendMessage(Var.millisToWeek(p.getStatistic(Statistic.PLAY_ONE_MINUTE)));
        }

        return false;
    }
}

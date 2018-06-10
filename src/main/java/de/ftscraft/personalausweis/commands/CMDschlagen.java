package de.ftscraft.personalausweis.commands;

import de.ftscraft.personalausweis.main.Engine;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CMDschlagen implements CommandExecutor
{

    private Engine plugin;

    public CMDschlagen(Engine plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("schlagen").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        Player p = (Player)cs;

        if(args.length == 1) {
            boolean isNear = false;
            Player t = null;
            for(Entity e : p.getNearbyEntities(5,5,5)) {
                if(e.getName().equalsIgnoreCase(args[0])) {
                    t = (Player)e;
                    Location loc = t.getLocation();
                    loc.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, loc.clone().add(0,1.5D, 0), 4, 0.3,0,0.3);
                    t.sendMessage("§e"+p.getName()+" hat dich geschlagen");
                    isNear = true;
                }
            }

            if(!isNear) {
                p.sendMessage("§eDer Spieler ist nicht in Schlagreichweite");
                return true;
            }

            for(Entity e : p.getNearbyEntities(5,5,5)) {
                if(e instanceof Player) {
                    e.sendMessage("§e"+p.getName()+" §7hat §e"+t.getName()+" §7geschlagen!");
                }
            }

            p.sendMessage("§eDu hast erfolgreich §c"+t.getName()+" §7geschlagen!");

        }

        return false;
    }
}

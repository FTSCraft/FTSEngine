package de.ftscraft.ftsengine.commands.emotes;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDohrfeige implements CommandExecutor {

    private final Engine plugin;
    private final Messages messages = new Messages();

    public CMDohrfeige(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("ohrfeige").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(cs instanceof Player)){
            return true;
        }

        Player p = (Player) cs;

        if (plugin.getAusweis(p) == null){
            p.sendMessage("§cDu hast noch kein Ausweis. Mach dir einen mit /ausweis!");
            return true;
        }

        if(args.length == 1){

            String tName = args[0];
            Player target = Bukkit.getPlayer(tName);

            if(target != null){
                if (plugin.getAusweis(target) != null){
                    if(target.getLocation().distance(p.getLocation()) < 4){
                        String message = "§c" + plugin.getAusweis(p).getFirstName() + " " + plugin.getAusweis(p).getLastName() + "§e gibt §c" + plugin.getAusweis(target).getFirstName() + " " + plugin.getAusweis(target).getLastName() + "§e eine Ohrfeige.";
                        p.sendMessage(message);
                        for(Entity n : p.getNearbyEntities(15, 15, 15)) {
                            if (n instanceof Player) {
                                n.sendMessage(message);
                            }
                        }
                        //for (int = 0; i < 4; i++){
                        p.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, p.getLocation().add(0,2.2,0), 4, 0.2, 0.3, 0.2);
                        target.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, p.getLocation().add(0,2.2,0), 4, 0.2, 0.3, 0.2);
                        //}

                    }else
                        p.sendMessage(Messages.PLAYER_TOO_FAR_AWAY);
                }else
                    p.sendMessage(Messages.TARGET_NO_AUSWEIS);
            }else
                p.sendMessage("Der Spieler ist derzeit nicht online");
        }

        return false;
    }
}

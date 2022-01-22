package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CMDkussen implements CommandExecutor {

    private final Engine plugin;
    private final Messages messages = new Messages();

    public CMDkussen(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("küssen").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if (!(cs instanceof Player)) {
            return true;
        }

        Player p = (Player) cs;

        if (plugin.getAusweis(p) == null) {
            p.sendMessage("§cDu hast noch kein Ausweis. Mach dir einen mit /ausweis!");
            return true;
        }

        if (args.length == 1) {

            String tName = args[0];
            Player target = Bukkit.getPlayer(tName);

            if (target != null) {
                if (plugin.getAusweis(target) != null) {
                    if (target.getLocation().distance(p.getLocation()) < 3) {
                        String message = "§c" + plugin.getAusweis(p).getFirstName() + " " + plugin.getAusweis(p).getLastName() + "§e küsst §c" + plugin.getAusweis(target).getFirstName() + " " + plugin.getAusweis(target).getLastName();
                        p.sendMessage(message);
                        p.playSound(p.getLocation(), "minecraft:custom.effect_kiss", 1, 1);
                        for (Entity n : p.getNearbyEntities(10, 10, 10)) {
                            if (n instanceof Player) {
                                n.sendMessage(message);
                                ((Player) n).playSound(n.getLocation(), "minecraft:custom.effect_kiss", 1, 1);
                            }
                        }

                        //for (int i = 0; i < 4; i++) {
                            p.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0,2.2,0), 4, 0.2, 0.3, 0.2);
                            target.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0,2.2,0), 4, 0.2, 0.3, 0.2);
                        //}

                    } else
                        p.sendMessage(messages.PREFIX + "Der Spieler ist zu weit entfernt");
                } else
                    p.sendMessage(messages.PREFIX + "Der Spieler hat keinen Ausweis");
            }

        }

        return false;
    }
}

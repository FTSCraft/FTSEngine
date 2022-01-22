package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CMDspucken implements CommandExecutor {

    private final Engine plugin;

    public CMDspucken(Engine plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("spucken").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(new Messages().ONLY_PLAYER);
            return true;
        }

        Player p = (Player) sender;

        if(args.length == 1) {

            String tString = args[0];

            Player t = Bukkit.getPlayer(tString);

            if(t == null) {
                p.sendMessage(ChatColor.RED + "Dieser Spieler ist nicht online!");
                return true;
            }

            if(t.getLocation().distance(p.getLocation()) > 4) {
                p.sendMessage(ChatColor.RED + "Dieser Spieler ist zu weit weg!");
                return true;
            }

            Location pLoc = p.getLocation();

            LlamaSpit llamaSpit = p.launchProjectile(LlamaSpit.class,pLoc.getDirection().multiply(0.5));
            llamaSpit.setShooter(p);

            p.sendMessage(ChatColor.GRAY + "Du hast den Spieler " + ChatColor.RED + t.getName() + ChatColor.GRAY + " angespuckt");
            t.sendMessage(ChatColor.GRAY + "Der Spieler " + ChatColor.RED + p.getName() + ChatColor.GRAY + " hat dich angespuckt!");


        }


        return false;
    }


}

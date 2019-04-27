package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDfasten implements CommandExecutor {

    private Engine plugin;

    public CMDfasten(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("fasten").setExecutor(this::onCommand);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if(!(cs instanceof Player)) {
            cs.sendMessage("§cDieser Befehl ist nur für Spieler");
            return true;
        }

        Player p = (Player) cs;

        ((Player) cs).setFoodLevel(10);
        cs.sendMessage("§7[§6FTS-Engine§7] Du hast erfolgreich gefastet!");

        return false;
    }
}

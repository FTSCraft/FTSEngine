package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CMDgehen implements CommandExecutor {

    public CMDgehen(Engine plugin) {
        plugin.getCommand("gehen").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if(cs instanceof Player) {
            Player p = (Player)cs;

            int armorNulls = 0;

            for (ItemStack armorContent : p.getInventory().getArmorContents()) {
                if(armorContent == null)
                    armorNulls++;
            }

            if(armorNulls == 4) {

                if(p.getWalkSpeed() == 0.1f) {
                    p.setWalkSpeed(0.2f);
                    p.sendMessage("§cDu gehst jetzt wieder normal");
                } else {
                    p.setWalkSpeed(0.1f);
                    p.sendMessage("§cDu gehst jetzt langsam");
                }

            } else p.sendMessage("§cDu kannst diesen Befehl nicht benutzen wenn du eine Rüstung trägst!");


        }
        return false;
    }
}

package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Material;
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

    final float SLOW_SPEED = 0.1f;

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cs instanceof Player p) {

            int armorNulls = 0;

            for (ItemStack armorContent : p.getInventory().getArmorContents()) {
                if (armorContent == null)
                    armorNulls++;
                else if (armorContent.getType() == Material.CARVED_PUMPKIN)
                    armorNulls++;
            }

            if (armorNulls == 4) {

                if (p.getWalkSpeed() == SLOW_SPEED) {
                    p.setWalkSpeed(0.2f);
                    p.sendMessage(Messages.PREFIX + "Du gehst jetzt wieder normal");
                } else {
                    p.setWalkSpeed(SLOW_SPEED);
                    p.sendMessage(Messages.PREFIX + "Du gehst jetzt langsam");
                }

            } else
                p.sendMessage(Messages.PREFIX + "Du kannst diesen Befehl nicht benutzen wenn du eine Rüstung trägst!");


        }
        return false;
    }
}

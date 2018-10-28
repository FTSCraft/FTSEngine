package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.courier.Kurier;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CMDkurier implements CommandExecutor
{

    private Engine pluign;

    public CMDkurier(Engine pluign)
    {
        this.pluign = pluign;
        pluign.getCommand("kurier").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args)
    {
        if(!(cs instanceof Player)) {
            cs.sendMessage(pluign.msgs.ONLY_PLAYER);
            return true;
        }

        Player p = (Player)cs;

        PlayerInventory inv = p.getInventory();
        ItemStack hand = inv.getItemInMainHand();
        if(hand.getType() != Material.FILLED_MAP) {
            return true;
        }

        new Kurier(p, Bukkit.getPlayer(args[0]), hand, pluign);

        return false;
    }


}


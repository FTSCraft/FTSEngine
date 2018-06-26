package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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

        Inventory inv = p.getInventory();

        

        return false;
    }


}


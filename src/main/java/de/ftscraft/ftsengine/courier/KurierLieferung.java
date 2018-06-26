package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class KurierLieferung
{

    private ItemStack itemStack;
    private Player sender;
    private Player reciver;
    private int seconds;
    int id;
    private Engine plugin;

    public KurierLieferung(ItemStack itemStack, Player sender, Player reciver, int seconds, Engine plugin)
    {
        this.itemStack = itemStack;
        this.sender = sender;
        this.reciver = reciver;
        this.seconds = seconds;
        this.plugin = plugin;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public void setSeconds(int seconds)
    {
        this.seconds = seconds;
    }

    public void send() {
        int anzahl;
        anzahl = itemStack.getAmount();
        anzahl = (int) (anzahl * 0.95);
        if(reciver != null) {
            Parrot parrot = reciver.getWorld().spawn(reciver.getLocation().clone().add(5,0,5), Parrot.class);
            parrot.setMetadata("Kurier." + id, new FixedMetadataValue(plugin, true));
            if(sender != null)
                sender.sendMessage("§cDein Brief ist angekommen");
        } else {
            sender.getInventory().addItem(itemStack);
            sender.sendMessage("§cDer Spieler ist Offline gegangen. Du hast das Item wieder bekommen");
        }
    }
}

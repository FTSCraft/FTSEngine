package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class BriefLieferung
{

    private Engine plugin;

    private Briefkasten sender;
    private Briefkasten reciver;
    private int seconds;
    private ArrayList<ItemStack> lieferung;

    public BriefLieferung(Engine plugin, String sender, String reciver)
    {
        this.plugin = plugin;
        this.sender = plugin.briefkasten.get(sender);
        this.reciver = plugin.briefkasten.get(reciver);
        Player p = Bukkit.getPlayer(UUID.fromString(sender));

        if(this.sender == null)
        {
            p.sendMessage(plugin.msgs.PREFIX+"Du hast kein Briefkasten!");
            return;
        }
        if(this.reciver == null)
        {
            p.sendMessage(plugin.msgs.PREFIX+"Der Spieler hat kein Briefkasten!");
            return;
        }

        p.sendMessage(plugin.msgs.PREFIX+"Du hast erfolgreich Post abgeschickt!");

        seconds = 0;

        seconds = (int) this.sender.getChest().getLocation().distance(this.reciver.getChest().getLocation()) / 5;

        if(seconds == 0)
            seconds = 10;

        lieferung = new ArrayList<>();

        for(ItemStack is : this.sender.getChest().getBlockInventory().getContents()) {
            if(is != null)
            if(is.getType() == Material.MAP || is.getType() == Material.WRITTEN_BOOK || is.getType() == Material.BOOK_AND_QUILL) {
                lieferung.add(is);
                this.sender.getChest().getBlockInventory().remove(is);
            } else p.sendMessage("§7Die anderen Sachen da drin sind viel zu schwer für den alten Postmann! Er hat sie drin gelassen.");
        }
        plugin.lieferungen.add(this);

    }

    public Briefkasten getSender()
    {
        return sender;
    }

    public Briefkasten getReciver()
    {
        return reciver;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public void setSeconds(int seconds)
    {
        this.seconds = seconds;
        if(seconds == 0)
            send();
    }

    private void send() {
        for(ItemStack is : lieferung) {
            if(reciver.getChest().getBlockInventory().firstEmpty() != -1)
                reciver.getChest().getBlockInventory().addItem(is);
            else sender.getChest().getBlockInventory().addItem(is);
        }

        plugin.lieferungen.remove(this);

    }
}

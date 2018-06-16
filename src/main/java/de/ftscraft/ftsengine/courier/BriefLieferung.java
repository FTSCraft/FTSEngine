package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;
import org.bukkit.Material;
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

    public BriefLieferung(Engine plugin, UUID sender, UUID reciver)
    {
        this.plugin = plugin;
        this.sender = plugin.briefkasten.get(sender);
        this.reciver = plugin.briefkasten.get(reciver);

        if(this.sender == null)
            return;
        if(this.reciver == null)
            return;

        if(!this.sender.getChest().getBlockInventory().contains(Material.MAP))
            return;

        this.lieferung = new ArrayList<>(Arrays.asList(this.sender.getChest().getBlockInventory().getContents()));



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
    }

    public void send() {
        for(ItemStack is : lieferung) {
            if(reciver.getChest().getBlockInventory().firstEmpty() != -1)
                reciver.getChest().getBlockInventory().addItem(is);
            else sender.getChest().getBlockInventory().addItem(is);
        }
    }
}

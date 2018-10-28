package de.ftscraft.ftsengine.courier;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
//import org.bukkit.craftbukkit.v1_13_R2.entity.CraftCreature;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kurier {

    private Parrot parrot;
    private Player sender, receiver;
    private ItemStack itemStack;
    private Engine plugin;

    public Kurier(Player sender, Player receiver, ItemStack itemStack, Engine plugin)
    {
        this.sender = sender;
        this.receiver = receiver;
        this.itemStack = itemStack;
        this.plugin = plugin;
        start();
        Bukkit.getScheduler().runTaskLater(plugin, this::end, 20 * 5);
    }

    public void start()
    {
        Location sloc = sender.getLocation().clone();
        this.parrot = sloc.getWorld().spawn(sloc.clone(), Parrot.class);
        sloc.add(20, 0, 0);
        ArmorStand a = sloc.getWorld().spawn(sloc.clone(), ArmorStand.class);
        a.setGravity(false);
        moveTo(parrot, a.getLocation(), 1);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            a.remove();
            this.parrot.remove();
        }, 20 * 3);
    }

    public void end()
    {
        Location rloc = receiver.getLocation().clone().add(5, 0, 3);
        this.parrot = rloc.getWorld().spawn(rloc.clone(), Parrot.class);
        moveTo(parrot, receiver.getLocation(), 1);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            this.parrot.remove();
            receiver.getInventory().addItem(itemStack);
        }, 20 * 3);
    }

    private void moveTo(LivingEntity entity, Location moveTo, float speed)
    {
        //((CraftCreature)entity).getHandle().getNavigation().a(moveTo.getX(), moveTo.getY(), moveTo.getZ(), speed);
    }

}

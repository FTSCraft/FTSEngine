package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CMDgehen implements CommandExecutor {

    public CMDgehen(Engine plugin) {
        plugin.getCommand("gehen").setExecutor(this);
    }

    final float SLOW_SPEED = 0.1f;
    public static List<AbstractHorse> speed = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cs instanceof Player p) {

            if(p.getVehicle()!=null){
                EntityType mountType = p.getVehicle().getType();
                System.out.println(mountType);
                if (mountType != EntityType.HORSE && mountType != EntityType.SKELETON_HORSE && mountType != EntityType.ZOMBIE_HORSE && mountType != EntityType.DONKEY) {
                    return true;
                }
                AbstractHorse horse = (AbstractHorse) p.getVehicle();
                if (getHorseSpeed(horse) > 0.1) {
                    speed.add(horse);
                    setHorseSpeed(horse, 0.08);
                    p.sendMessage(Messages.PREFIX + " Dein Pferd geht jetzt langsam");
                } else {
                    setHorseSpeed(horse, getHorseDefaultSpeed(horse));
                    speed.remove(horse);
                    p.sendMessage(Messages.PREFIX + " Dein Pferd geht jetzt wieder normal");
                }
            }else {

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

        }
        return false;
    }

    public static void setHorseSpeed(AbstractHorse horse, double speed) {
        AttributeInstance attribute = horse.getAttribute(Attribute.MOVEMENT_SPEED);
        if (attribute != null) {
            horse.getPersistentDataContainer().set(new NamespacedKey(Engine.getInstance(), "horse_speed"), PersistentDataType.DOUBLE, attribute.getBaseValue());
            attribute.setBaseValue(speed);
        }
    }

    public static double getHorseSpeed(AbstractHorse horse) {
        AttributeInstance attribute = horse.getAttribute(Attribute.MOVEMENT_SPEED);
        if (attribute != null) {
            return attribute.getBaseValue();
        }
        return -1;
    }

    public static double getHorseDefaultSpeed(AbstractHorse horse) {
        NamespacedKey namespacedKey = new NamespacedKey(Engine.getInstance(), "horse_speed");
        if(horse.getPersistentDataContainer().has(namespacedKey, PersistentDataType.DOUBLE)){
            return horse.getPersistentDataContainer().get(namespacedKey, PersistentDataType.DOUBLE);
        }
        return -1;
    }

}

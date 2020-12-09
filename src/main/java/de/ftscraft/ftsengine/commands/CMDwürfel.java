package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Gender;
import de.ftscraft.ftsengine.utils.Var;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class CMDwürfel implements CommandExecutor {

    private Engine plugin;

    private final int DICES = 3;

    public CMDwürfel(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("würfel").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(plugin.msgs.ONLY_PLAYER);
            return true;
        }

        Player p = (Player) cs;

        Ausweis ausweis = plugin.getAusweis(p);

        if (ausweis == null || ausweis.getGender() == null || ausweis.getRace() == null) {
            p.sendMessage("§6Bitte lege dir zuerst einen Ausweis an (/ausweis). Für das Würfeln ist folgendes wichtig: §cName, Rasse, Geschlecht");
            return true;
        }

        int[] numbers = new int[DICES];

        Gender gender = ausweis.getGender();
        String name = ausweis.getFirstName() + " " + ausweis.getLastName();

        DiceType dice = DiceType.getDiceTypeByName(ausweis.getRace());

        if(args.length == 1 && dice == null) {
            dice = DiceType.getDiceTypeByName(args[0]);
        }

        if (dice != null) {

            int total = 0;

            StringBuilder sb;

            if (gender == Gender.FEMALE) {
                if (dice.getMName().equalsIgnoreCase(dice.getFName())) {
                    sb = new StringBuilder("§6Der §o" + dice.getMName() + " §r§e" + name + " §6würfelt: §e");
                } else
                    sb = new StringBuilder("§6Die §o" + dice.getFName() + " §r§e" + name + " §6würfelt: §e");
            } else
                sb = new StringBuilder("§6Der §o" + dice.getMName() + " §r§e" + name + " §6würfelt: §e");

            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = ThreadLocalRandom.current().nextInt(1, dice.pips + 1);
                total += numbers[i];
                if (i == numbers.length - 1) {
                    sb.append(numbers[i] + " = ");
                } else {
                    sb.append(numbers[i] + " + ");
                }
            }

            sb.append(((total >= dice.needs) ? "§2" : "§c") + total);

            for (Entity nearbyEntity : p.getLocation().getNearbyEntities(20, 20, 20)) {
                if (nearbyEntity instanceof Player) {
                    nearbyEntity.sendMessage(sb.toString());
                }
            }


        } else {
            cs.sendMessage("§cBitte würfel so: §e/würfel [Mensch/Zwerg/Elf/Ork]");
        }

        return false;
    }

    public enum DiceType {

        ORK("Ork", "Orkin", 24, 50),
        ELF("Elf", "Elfin", 20, 30),
        ZWERG("Zwerg", "Zwergin", 18, 30),
        MENSCH("Mensch", "Mensch", 16, 30);

        private String mName;
        private String fName;
        private int pips;
        private int needs;

        DiceType(String mName, String fName, int pips, int needs) {
            this.mName = mName;
            this.fName = fName;
            this.pips = pips;
            this.needs = needs;
        }

        public String getMName() {
            return mName;
        }

        public String getFName() {
            return fName;
        }

        public int getPips() {
            return pips;
        }

        public int getNeeds() {
            return needs;
        }

        public static DiceType getDiceTypeByName(String race) {
            for (DiceType value : DiceType.values()) {
                if (value.getMName().equalsIgnoreCase(race) || race.toLowerCase().contains(value.getMName().toLowerCase()))
                    return value;
            }
            return null;
        }

    }

}

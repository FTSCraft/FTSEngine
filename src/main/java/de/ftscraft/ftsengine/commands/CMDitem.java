package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMDitem implements CommandExecutor {

    private Engine plugin;

    private List<String> forbiddenItems;

    public CMDitem(Engine plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("item").setExecutor(this);

        forbiddenItems = new ArrayList<>();

        forbiddenItems.addAll(Arrays.asList(BackpackType.LARGE.getName(), BackpackType.TINY.getName(), BackpackType.ENDER.getName()));
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {


        if (cs instanceof Player) {

            Player p = (Player) cs;

            if(p.hasPermission("ftsengine.item")) {

                if (args.length >= 1) {

                    ItemStack is = p.getInventory().getItemInMainHand();

                    if(is.hasItemMeta()) {

                        if(forbiddenItems.contains(is.getItemMeta().getDisplayName())) {

                            p.sendMessage("§cDu darfst dieses Item nicht bearbeiten!");

                            return true;
                        }

                    }

                    if (args[0].equalsIgnoreCase("name")) {


                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 1; i < args.length; i++) {
                            stringBuilder.append(args[i] + " ");
                        }

                        stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);

                        String name = ChatColor.translateAlternateColorCodes('&', stringBuilder.toString());

                        if (is != null && is.getType() != Material.AIR) {

                            if(name.equalsIgnoreCase("§cÜberreste") || name.contains("schloss")) {

                                p.sendMessage("§cDas Item so zu nennen ist nicht erlaubt!");
                                return true;

                            }

                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(name);
                            is.setItemMeta(im);

                            p.sendMessage("§cDein Item heißt nun: §e" + name);

                        } else
                            p.sendMessage("§cDu musst ein Item in deiner Hand haben!");

                    } /*else if (args[0].equalsIgnoreCase("glow")) {

                        ItemStack item = p.getInventory().getItemInMainHand();

                        if (item != null && item.getType() != Material.AIR) {

                            ItemMeta itemStackMeta = item.getItemMeta();

                            if (!itemStackMeta.hasEnchants()) {
                                itemStackMeta.addEnchant(Enchantment.LURE, 0, true);
                                itemStackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                item.setItemMeta(itemStackMeta);
                                p.sendMessage("§cDein Item §4g§2l§1ä§5n§1z§7t §cnun!");
                            } else p.sendMessage("§cDas Item ist schon am krass glänzen!");
                        } else p.sendMessage("§cBite nehme das Item in die Hand!");

                    } */ else if (args[0].equalsIgnoreCase("lore")) {

                        StringBuilder stringBuilderAll = new StringBuilder();

                        for (int i = 1; i < args.length; i++) {
                            stringBuilderAll.append(args[i] + " ");
                        }

                        stringBuilderAll.deleteCharAt(stringBuilderAll.toString().length() - 1);

                        String all = ChatColor.translateAlternateColorCodes('&', stringBuilderAll.toString());

                        String[] lines = all.split("\\|");

                        List<String> lore = new ArrayList<>();

                        for (int i = 0; i < lines.length; i++) {
                            lines[i].replace("|", "");
                            lore.add(ChatColor.translateAlternateColorCodes('&', lines[i]));
                        }

                        ItemStack item = p.getInventory().getItemInMainHand();

                        if (item != null && item.getType() != Material.AIR) {

                            ItemMeta itemStackMeta = item.getItemMeta();
                            itemStackMeta.setLore(lore);
                            item.setItemMeta(itemStackMeta);

                            p.sendMessage("§cDu hast die Lore gesetzt!");

                        } else p.sendMessage("§cBite nehme das Item in die Hand!");

                    } else p.sendMessage(help());


                } else p.sendMessage(help());
            } else p.sendMessage("§cDieser Befehl ist nur für Leute die einen Rang gekauft haben. §6Du kannst das auch! http://musc1.buycraft.net/");

        } else cs
                .sendMessage("§cDer Befehl ist nur für krasse User!");

        return true;
    }

    private String help() {

        return "§c/item name §4NAME §7(ColorCodes mit '&', Leerzeichen ist möglich) \n" +
                "§c/item glow \n" +
                "§c/item lore §4LORE §7(ColorCodes mit '&', Leerzeichen ist möglich, neue Zeile mit '|')";

    }

}

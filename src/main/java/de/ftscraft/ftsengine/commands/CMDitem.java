package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMDitem implements CommandExecutor {

    private final List<String> forbiddenItems;
    private final List<String> forbiddenNames;

    public CMDitem(Engine plugin) {
        plugin.getCommand("item").setExecutor(this);

        forbiddenItems = new ArrayList<String>();
        forbiddenNames = new ArrayList<String>();

        forbiddenItems.addAll(Arrays.asList(BackpackType.LARGE.getName(), BackpackType.TINY.getName(), BackpackType.ENDER.getName()));

        forbiddenNames.addAll(Arrays.asList("§5Dietrich",
                "Pfeife",
                "Goldene Pfeife",
                "Misteltabak",
                "Tropischer Tabak",
                "Netherwarzen Tabak",
                "Pilztabak",
                "tabak",
                "DocWeed",
                "Rucksack",
                "Handschellen",
                "Horn",
                "Marmelade",
                "§cÜberreste",
                "schloss",
                "Süßer Fisch"));
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {


        if (cs instanceof Player) {

            Player p = (Player) cs;

            if (p.hasPermission("ftsengine.item")) {

                if (args.length >= 1) {
                    ItemStack is = p.getInventory().getItemInMainHand();
                    if (is.hasItemMeta()) {
                        if (forbiddenItems.contains(is.getItemMeta().getDisplayName())) {
                            p.sendMessage("§cDu darfst dieses Item nicht bearbeiten!");
                            return true;
                        }
                    }
                    if (args[0].equalsIgnoreCase("name") && args.length >= 2) {

                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 1; i < args.length; i++) {
                            stringBuilder.append(args[i] + " ");
                        }

                        stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);

                        String name = ChatColor.translateAlternateColorCodes('&', stringBuilder.toString());

                        if (is != null && is.getType() != Material.AIR) {

                            if (forbiddenNames.contains(name)) {
                                p.sendMessage("§cDas Item so zu nennen ist nicht erlaubt!");
                                return true;
                            }

                            ItemMeta im = is.getItemMeta();
                            im.setDisplayName(name);
                            is.setItemMeta(im);

                            p.sendMessage("§cDein Item heißt nun: §e" + name);

                        } else
                            p.sendMessage("§cDu musst ein Item in deiner Hand haben!");

                    } else if (args[0].equalsIgnoreCase("lore") && args.length >= 2) {

                        StringBuilder stringBuilderAll = new StringBuilder();

                        for (int i = 1; i < args.length; i++) {
                            stringBuilderAll.append(args[i] + " ");
                        }

                        stringBuilderAll.deleteCharAt(stringBuilderAll.toString().length() - 1);

                        String all = ChatColor.translateAlternateColorCodes('&', stringBuilderAll.toString());

                        String[] lines = all.split("\\|");

                        List<String> lore = new ArrayList<String>();

                        for (String line : lines) {
                            line.replace("|", "");
                            lore.add(ChatColor.translateAlternateColorCodes('&', line));
                        }

                        ItemStack item = p.getInventory().getItemInMainHand();

                        if (forbiddenItems.contains(item.getItemMeta().displayName())) {
                            p.sendMessage("§cDieses Item darfst du nicht bearbeiten");
                            return true;
                        }

                        if (item.getType() != null && item.getType() != Material.AIR) {

                            ItemMeta itemStackMeta = item.getItemMeta();
                            itemStackMeta.setLore(lore);
                            item.setItemMeta(itemStackMeta);

                            p.sendMessage("§cDu hast die Lore gesetzt!");

                        } else p.sendMessage("§cBitte nehme das Item in die Hand!");

                    } else p.sendMessage(help());


                } else p.sendMessage(help());
            } else
                p.sendMessage("§cDieser Befehl ist nur für Leute die einen Rang gekauft haben. §6Du kannst das auch! http://musc1.buycraft.net/");

        } else cs
                .sendMessage("§cDieser Befehl ist nur für Leute die einen Rang gekauft haben. §6Du kannst das auch! http://musc1.buycraft.net/");

        return true;
    }

    private String help() {

        return "§c/item name §4NAME §7(ColorCodes mit '&', Leerzeichen ist möglich) \n" +
                "§c/item lore §4LORE §7(ColorCodes mit '&', Leerzeichen ist möglich, neue Zeile mit '|')";

    }

}

package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.items.ItemBuilder;
import de.ftscraft.ftsutils.items.ItemReader;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
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
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMDitem implements CommandExecutor {

    private final List<String> forbiddenItems;
    private final List<String> forbiddenNames;
    private PlayerPointsAPI pointsAPI;

    private final static int COST_NAME = 15, COST_LORE = 10, COST_GLOW = 20;

    public CMDitem(Engine plugin) {
        plugin.getCommand("item").setExecutor(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) pointsAPI = PlayerPoints.getInstance().getAPI();
        else plugin.getLogger().warning("PlayerPoints is not enabled. Thus the functionality of /item is limited.");

        forbiddenItems = new ArrayList<>();
        forbiddenNames = new ArrayList<>();

        forbiddenItems.addAll(Arrays.asList(BackpackType.LARGE.getName(), BackpackType.TINY.getName(), BackpackType.ENDER.getName()));

        forbiddenNames.addAll(Arrays.asList("§5Dietrich", "Pfeife", "Goldene Pfeife", "Misteltabak", "Tropischer Tabak", "Netherwarzen Tabak", "Pilztabak", "tabak", "DocWeed", "Rucksack", "Handschellen", "Horn", "Marmelade", "§cÜberreste", "horse", "schloss", "Süßer Fisch"));
    }

    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {


        if (cs instanceof Player) {

            Player p = (Player) cs;

            if (args.length >= 1) {
                ItemStack is = p.getInventory().getItemInMainHand();

                if (is.hasItemMeta()) {
                    if (forbiddenItems.contains(is.getItemMeta().getDisplayName())) {
                        p.sendMessage(Messages.PREFIX + "Du darfst dieses Item nicht bearbeiten!");
                        return true;
                    }
                }

                if (args[0].equalsIgnoreCase("verify")) {
                    String sign = ItemReader.getSign(is);
                    if (sign == null) {
                        p.sendMessage(Messages.PREFIX + "Dieses Item ist §cnicht §7verifiziert wurden.");
                        return true;
                    }
                    p.sendMessage(Messages.PREFIX + "Dieses Item wurde mit der Signatur §c" + sign + " §7verifiziert.");
                } else if (args[0].equalsIgnoreCase("sign") && args.length == 2) {
                    if (!p.hasPermission("ftsengine.item.sign")) {
                        p.sendMessage(Messages.NO_PERMISSIONS);
                        return true;
                    }
                    String sign = args[1].toUpperCase();
                    new ItemBuilder(is).sign(sign).build();
                    p.sendMessage(Messages.PREFIX + "Du hast das Item in deiner Hand mit §c" + sign + " §7verifiziert");
                } else if (args[0].equalsIgnoreCase("name") && args.length >= 2) {

                    if (is.getType() == Material.AIR) {
                        p.sendMessage(Messages.PREFIX + "Du musst ein Item in der Hand haben!");
                        return true;
                    }

                    if (notAbleToPay(p, COST_NAME)) {
                        p.sendMessage(Messages.PREFIX + "Du kannst dir das nicht leisten. Mit Premium funktioniert der Command aber kostenlos.");
                        return true;
                    }

                    StringBuilder stringBuilder = new StringBuilder();

                    for (int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i]).append(" ");
                    }

                    stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);

                    String name = ChatColor.translateAlternateColorCodes('&', stringBuilder.toString());

                    if (forbiddenNames.contains(name)) {
                        p.sendMessage(Messages.PREFIX + "Das Item so zu nennen ist nicht erlaubt!");
                        return true;
                    }

                    new ItemBuilder(is).name(name).build();

                    pay(p, COST_NAME);
                    p.sendMessage(Messages.PREFIX + "Dein Item heißt nun: §e" + name);


                } else if (args[0].equalsIgnoreCase("glow")) {

                    if (is.getType() == Material.AIR) {
                        p.sendMessage(Messages.PREFIX + "Du musst ein Item in der Hand haben!");
                        return true;
                    }

                    if (notAbleToPay(p, COST_GLOW)) {
                        p.sendMessage(Messages.PREFIX + "Du kannst dir das nicht leisten. Mit Premium funktioniert der Command aber kostenlos.");
                        return true;
                    }

                    new ItemBuilder(is).enchant(Enchantment.UNBREAKING, 1).addPDC("glow", true, PersistentDataType.BOOLEAN).build();
                    is.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                    pay(p, COST_GLOW);
                    p.sendMessage(Messages.PREFIX + "Dein Item leuchtet nun :o");

                } else if (args[0].equalsIgnoreCase("lore") && args.length >= 2) {

                    if (is.getType() == Material.AIR) {
                        p.sendMessage(Messages.PREFIX + "Du musst ein Item in der Hand haben!");
                        return true;
                    }

                    if (notAbleToPay(p, COST_GLOW)) {
                        p.sendMessage(Messages.PREFIX + "Du kannst dir das nicht leisten. Mit Premium funktioniert der Command aber kostenlos.");
                        return true;
                    }

                    StringBuilder stringBuilderAll = new StringBuilder();

                    for (int i = 1; i < args.length; i++) {
                        stringBuilderAll.append(args[i]).append(" ");
                    }

                    stringBuilderAll.deleteCharAt(stringBuilderAll.toString().length() - 1);

                    String all = ChatColor.translateAlternateColorCodes('&', stringBuilderAll.toString());

                    String[] lines = all.split("\\|");

                    List<String> lore = new ArrayList<>();

                    for (String line : lines) {
                        line.replace("|", "");
                        lore.add(ChatColor.translateAlternateColorCodes('&', line));
                    }

                    ItemStack item = p.getInventory().getItemInMainHand();

                    if (forbiddenItems.contains(item.getItemMeta().displayName())) {
                        p.sendMessage(Messages.PREFIX + "Dieses Item darfst du nicht bearbeiten");
                        return true;
                    }

                    if (forbiddenNames.contains(lore.get(0))) {
                        p.sendMessage(Messages.PREFIX + "Das Item so zu nennen ist nicht erlaubt!");
                        return true;
                    }

                    ItemMeta itemStackMeta = item.getItemMeta();
                    itemStackMeta.setLore(lore);
                    item.setItemMeta(itemStackMeta);
                    new ItemBuilder(item).addPDC("edited", true, PersistentDataType.BOOLEAN).build();

                    pay(p, COST_LORE);
                    p.sendMessage(Messages.PREFIX + "Du hast die Lore gesetzt!");

                } else p.sendMessage(HELP);


            } else p.sendMessage(HELP);
        }

        return true;
    }

    private final String HELP = """
            %s
            /item verify
            /item name NAME (Kosten: %d PP)
            /item glow (Kosten: %d PP)
            /item lore LORE (Kosten: %d PP, neue Zeile mit '|')"""
            .formatted(Messages.PREFIX, COST_NAME, COST_GLOW, COST_LORE);


    private boolean notAbleToPay(Player p, int amount) {
        if (p.hasPermission("ftsengine.item")) return false;
        if (pointsAPI == null) return true;
        return pointsAPI.look(p.getUniqueId()) < amount;
    }

    private void pay(Player p, int amount) {
        if (p.hasPermission("ftsengine.item")) return;
        pointsAPI.take(p.getUniqueId(), amount);
        p.sendMessage(Messages.PREFIX + "Dir wurden " + amount + " PlayerPunkte abgezogen.");
    }

}

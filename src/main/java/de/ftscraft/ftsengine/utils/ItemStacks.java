package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsutils.items.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemStacks {

    private ItemStack largeBackpack, tinyBackpack, backpackKey, enderBackpack;
    private ItemStack ironHelmet, ironChestplate, ironLeggings, ironBoots,
            chainmailHelmet, chainmailChestplate, chainmailLeggings, chainmailBoots,
            diamondHelmet, diamondChestplate, diamondLeggings, diamondBoots, gold_ingot;
    private ItemStack diamondHelmetReplacement, diamondChestplateReplacement, diamondLeggingsReplacement, diamondBootsReplacement;
    private ItemStack leatherHorse, ironHorse, diamondHorse;
    private ItemStack gold;

    private ItemStack horn;

    private List<Material> disabledDefaultRecipes;

    private final Engine plugin;
    
    public ItemStacks(Engine plugin) {
        this.plugin = plugin;
        init();
        initRecipes();
    }

    private void init() {

        disabledDefaultRecipes = new ArrayList<>();
        disabledDefaultRecipes.addAll(Arrays.asList(
                Material.DIAMOND_BOOTS,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_HELMET,
                Material.LEATHER_HORSE_ARMOR,
                Material.IRON_HELMET,
                Material.IRON_CHESTPLATE,
                Material.IRON_LEGGINGS,
                Material.IRON_BOOTS));

        //BIG BP
        largeBackpack = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .color(Color.RED)
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .name(BackpackType.LARGE.getName())
                .lore("§7In diesen Rucksack passen viele, weitere Dinge rein", "ID: #-1")
                .sign(BackpackType.LARGE.getSign())
                .build();

        //TINY BP
        tinyBackpack = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .color(Color.GREEN)
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .name(BackpackType.TINY.getName())
                .lore("§7Dieser nützliche Rucksack hat Platz für viele Sachen", "ID: #-1")
                .sign(BackpackType.TINY.getSign())
                .build();

        //ENDER BP
        enderBackpack = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .color(Color.PURPLE)
                .addFlags(ItemFlag.HIDE_ATTRIBUTES)
                .name(BackpackType.ENDER.getName())
                .lore(BackpackType.ENDER.getLore())
                .sign(BackpackType.ENDER.getSign())
                .build();

        //BP KEY
        backpackKey = new ItemBuilder(Material.FEATHER)
                .name("§5Rucksack Schlüssel")
                .sign("BACKPACK_KEY")
                .build();

        //Horn
        horn = new ItemBuilder(Material.NAUTILUS_SHELL)
                .name("§6Horn")
                .sign("HORN")
                .build();

        ironHelmet = new ItemStack(Material.IRON_HELMET, 1);
        ironChestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ironLeggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        ironBoots = new ItemStack(Material.IRON_BOOTS, 1);

        chainmailHelmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        chainmailChestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        chainmailLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        chainmailBoots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);

        diamondHelmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        diamondChestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        diamondBoots = new ItemStack(Material.DIAMOND_BOOTS, 1);

        leatherHorse = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        ironHorse = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
        diamondHorse = new ItemStack(Material.DIAMOND_HORSE_ARMOR, 1);

        diamondHelmetReplacement = new ItemStack(Material.DIAMOND, 5);
        diamondChestplateReplacement = new ItemStack(Material.DIAMOND, 8);
        diamondLeggingsReplacement = new ItemStack(Material.DIAMOND, 7);
        diamondBootsReplacement = new ItemStack(Material.DIAMOND, 4);

        gold = new ItemStack(Material.GOLD_INGOT, 1);

    }

    public ItemStack getLargeBackpack() {
        return largeBackpack;
    }

    public ItemStack getGold() {
        return gold;
    }

    public ItemStack getTinyBackpack() {
        return tinyBackpack;
    }

    public ItemStack getBackpackKey() {
        return backpackKey;
    }

    public ItemStack getEnderBackpack() {
        return enderBackpack;
    }

    public ItemStack getIronChestplate() {
        return ironChestplate;
    }

    public ItemStack getChainmailChestplate() {
        return chainmailChestplate;
    }

    public ItemStack getIronHelmet() {
        return ironHelmet;
    }

    public ItemStack getIronLeggings() {
        return ironLeggings;
    }

    public ItemStack getIronBoots() {
        return ironBoots;
    }

    public ItemStack getChainmailHelmet() {
        return chainmailHelmet;
    }

    public ItemStack getChainmailLeggings() {
        return chainmailLeggings;
    }

    public ItemStack getChainmailBoots() {
        return chainmailBoots;
    }

    public ItemStack getDiamondHelmet() {
        return diamondHelmet;
    }

    public ItemStack getDiamondChestplate() {
        return diamondChestplate;
    }

    public ItemStack getDiamondLeggings() {
        return diamondLeggings;
    }

    public ItemStack getDiamondBoots() {
        return diamondBoots;
    }

    public ItemStack getLeatherHorse() {
        return leatherHorse;
    }

    public ItemStack getIronHorse() {
        return ironHorse;
    }

    public ItemStack getDiamondHelmetReplacement() {
        return diamondHelmetReplacement;
    }

    public ItemStack getDiamondChestplateReplacement() {
        return diamondChestplateReplacement;
    }

    public ItemStack getDiamondLeggingsReplacement() {
        return diamondLeggingsReplacement;
    }

    public ItemStack getDiamondBootsReplacement() {
        return diamondBootsReplacement;
    }

    public ItemStack getDiamondHorse() {
        return diamondHorse;
    }

    public List<Material> getDisabledDefaultRecipes() {
        return disabledDefaultRecipes;
    }

    public ItemStack getHorn() {
        return horn;
    }

    public ItemStack getAir() {

        return new ItemStack(Material.DIAMOND, 0);

    }

    private void initRecipes() {
        NamespacedKey lanzeKey = new NamespacedKey(plugin, "FTSlanze");
        ItemStack lanzeItemStack = new ItemStack(Material.STICK, 1);
        lanzeItemStack = new ItemBuilder(Material.STICK)
                .name("§6Lanze")
                .sign("LANZE")
                .build();

        ShapedRecipe lanzeRecipe = new ShapedRecipe(lanzeKey, lanzeItemStack);
        lanzeRecipe.shape("F**", "*S*", "**E");
        lanzeRecipe.setIngredient('F', Material.FLINT);
        lanzeRecipe.setIngredient('S', Material.STICK);
        lanzeRecipe.setIngredient('E', Material.IRON_INGOT);
        lanzeRecipe.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(lanzeRecipe);

        plugin.mats.add(Material.DIRT_PATH);

        plugin.mats.addAll(Var.getCarpets());

        //TINY BACKPACK

        NamespacedKey tbpkey = new NamespacedKey(plugin, "FTStinybackpack");

        ShapedRecipe tiny_backpack = new ShapedRecipe(tbpkey, this.getTinyBackpack());
        tiny_backpack.shape("HHH", "HCH", "HHH");
        tiny_backpack.setIngredient('H', Material.RABBIT_HIDE);
        tiny_backpack.setIngredient('C', Material.CHEST);
        plugin.getServer().addRecipe(tiny_backpack);

        //LARGE BACKPACK

        NamespacedKey lbpkey = new NamespacedKey(plugin, "FTSlargebackpack");
        ShapedRecipe large_backpack = new ShapedRecipe(lbpkey, this.getLargeBackpack());
        large_backpack.shape("LLL", "LRL", "LLL");
        large_backpack.setIngredient('L', Material.LEATHER);
        large_backpack.setIngredient('R', this.getTinyBackpack());
        plugin.getServer().addRecipe(large_backpack);


        //ENDER BACKPACK

        NamespacedKey ebpkey = new NamespacedKey(plugin, "FTSenderbackpack");
        ShapedRecipe ender_backpack = new ShapedRecipe(ebpkey, this.getEnderBackpack());
        ender_backpack.shape("LLL", "LEL", "LLL");
        ender_backpack.setIngredient('L', Material.LEATHER);
        ender_backpack.setIngredient('E', Material.ENDER_CHEST);
        plugin.getServer().addRecipe(ender_backpack);

        //BACKPACK KEY

        NamespacedKey bpkkey = new NamespacedKey(plugin, "FTSbackpackkey");

        ShapedRecipe backpack_key = new ShapedRecipe(bpkkey, this.getBackpackKey());
        backpack_key.shape("I*I", "*F*", "I*I");
        backpack_key.setIngredient('I', Material.IRON_INGOT);
        backpack_key.setIngredient('*', Material.AIR);
        backpack_key.setIngredient('F', Material.FEATHER);
        plugin.getServer().addRecipe(backpack_key);

        //Horn

        NamespacedKey hornkey = new NamespacedKey(plugin, "FTSHorn");

        ShapelessRecipe horn = new ShapelessRecipe(hornkey, this.getHorn());
        horn.addIngredient(Material.NAUTILUS_SHELL);
        horn.addIngredient(Material.NOTE_BLOCK);
        plugin.getServer().addRecipe(horn);

        List<Recipe> backup = new ArrayList<>();
        Iterator<Recipe> a = plugin.getServer().recipeIterator();

        while (a.hasNext()) {
            Recipe r = a.next();

            ItemStack is = r.getResult();

            if (this.getDisabledDefaultRecipes().contains(is.getType())) {
                a.remove();
            }

        }


        //Iron Helmet

        NamespacedKey ihkey = new NamespacedKey(plugin, "FTSironhelmet");
        ShapedRecipe iron_helmet = new ShapedRecipe(ihkey, this.getIronHelmet());
        iron_helmet.shape("III", "IBI", "***");
        iron_helmet.setIngredient('I', Material.IRON_INGOT);
        iron_helmet.setIngredient('*', Material.AIR);
        iron_helmet.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_helmet);

        NamespacedKey ihkey2 = new NamespacedKey(plugin, "FTSironhelmet2");
        ShapedRecipe iron_helmet2 = new ShapedRecipe(ihkey2, this.getIronHelmet());
        iron_helmet2.shape("***", "III", "IBI");
        iron_helmet2.setIngredient('I', Material.IRON_INGOT);
        iron_helmet2.setIngredient('*', Material.AIR);
        iron_helmet2.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_helmet2);

        //IronChestplate

        NamespacedKey icpkey = new NamespacedKey(plugin, "FTSironchestplate");
        ShapedRecipe iron_chestplate = new ShapedRecipe(icpkey, this.getIronChestplate());
        iron_chestplate.shape("I*I", "IBI", "III");
        iron_chestplate.setIngredient('I', Material.IRON_INGOT);
        iron_chestplate.setIngredient('*', Material.AIR);
        iron_chestplate.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_chestplate);

        //IronLeggings

        NamespacedKey ilkey = new NamespacedKey(plugin, "FTSironleggings");
        ShapedRecipe iron_leggings = new ShapedRecipe(ilkey, this.getIronLeggings());
        iron_leggings.shape("III", "IBI", "I*I");
        iron_leggings.setIngredient('I', Material.IRON_INGOT);
        iron_leggings.setIngredient('*', Material.AIR);
        iron_leggings.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_leggings);

        //IronBoots

        NamespacedKey ibkey = new NamespacedKey(plugin, "FTSironboots");
        ShapedRecipe iron_boots = new ShapedRecipe(ibkey, this.getIronBoots());
        iron_boots.shape("***", "IBI", "I*I");
        iron_boots.setIngredient('I', Material.IRON_INGOT);
        iron_boots.setIngredient('*', Material.AIR);
        iron_boots.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_boots);

        NamespacedKey ibkey2 = new NamespacedKey(plugin, "FTSironboots2");
        ShapedRecipe iron_boots2 = new ShapedRecipe(ibkey2, this.getIronBoots());
        iron_boots2.shape("IBI", "I*I", "***");
        iron_boots2.setIngredient('I', Material.IRON_INGOT);
        iron_boots2.setIngredient('*', Material.AIR);
        iron_boots2.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_boots2);


        //ChainmailHelmet

        NamespacedKey chkey = new NamespacedKey(plugin, "FTSchainhelmet");
        ShapedRecipe chain_helmet = new ShapedRecipe(chkey, this.getChainmailHelmet());
        chain_helmet.shape("III", "I*I", "***");
        chain_helmet.setIngredient('I', Material.IRON_INGOT);
        chain_helmet.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_helmet);

        NamespacedKey chkey2 = new NamespacedKey(plugin, "FTSchainhelmet2");
        ShapedRecipe chain_helmet2 = new ShapedRecipe(chkey2, this.getChainmailHelmet());
        chain_helmet2.shape("***", "III", "I*I");
        chain_helmet2.setIngredient('I', Material.IRON_INGOT);
        chain_helmet2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_helmet2);

        //ChainmailChestplate

        NamespacedKey ccpkey = new NamespacedKey(plugin, "FTSchainmailchestplate");
        ShapedRecipe chainmail_chestplate = new ShapedRecipe(ccpkey, this.getChainmailChestplate());
        chainmail_chestplate.shape("I*I", "III", "III");
        chainmail_chestplate.setIngredient('I', Material.IRON_INGOT);
        chainmail_chestplate.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chainmail_chestplate);

        //ChainmailLeggings

        NamespacedKey clkey = new NamespacedKey(plugin, "FTSchainleggings");
        ShapedRecipe chain_leggings = new ShapedRecipe(clkey, this.getChainmailLeggings());
        chain_leggings.shape("III", "I*I", "I*I");
        chain_leggings.setIngredient('I', Material.IRON_INGOT);
        chain_leggings.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_leggings);

        //ChainmailBoots

        NamespacedKey cbkey = new NamespacedKey(plugin, "FTSchainboots");
        ShapedRecipe chain_boots = new ShapedRecipe(cbkey, this.getChainmailBoots());
        chain_boots.shape("***", "I*I", "I*I");
        chain_boots.setIngredient('I', Material.IRON_INGOT);
        chain_boots.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_boots);

        NamespacedKey cbkey2 = new NamespacedKey(plugin, "FTSchainboots2");
        ShapedRecipe chain_boots2 = new ShapedRecipe(cbkey2, this.getChainmailBoots());
        chain_boots2.shape("I*I", "I*I", "***");
        chain_boots2.setIngredient('I', Material.IRON_INGOT);
        chain_boots2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_boots2);

        //Diamond Helmet

        NamespacedKey dhkey = new NamespacedKey(plugin, "FTSdiamondhelmet");
        ShapedRecipe diamond_helmet = new ShapedRecipe(dhkey, this.getDiamondHelmet());
        diamond_helmet.shape("DDD", "DBD", "***");
        diamond_helmet.setIngredient('D', Material.DIAMOND);
        diamond_helmet.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_helmet.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_helmet);

        NamespacedKey dhkey2 = new NamespacedKey(plugin, "FTSdiamondhelmet2");
        ShapedRecipe diamond_helmet2 = new ShapedRecipe(dhkey2, this.getDiamondHelmet());
        diamond_helmet2.shape("***", "DDD", "DBD");
        diamond_helmet2.setIngredient('D', Material.DIAMOND);
        diamond_helmet2.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_helmet2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_helmet2);

        //Diamond Chestplate

        NamespacedKey dckey = new NamespacedKey(plugin, "FTSdiamondchest");
        ShapedRecipe diamond_chest = new ShapedRecipe(dckey, this.getDiamondChestplate());
        diamond_chest.shape("D*D", "DBD", "DDD");
        diamond_chest.setIngredient('D', Material.DIAMOND);
        diamond_chest.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_chest.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_chest);

        //Diamond Leggings

        NamespacedKey dlkey = new NamespacedKey(plugin, "FTSdiamondlegs");
        ShapedRecipe diamond_leggings = new ShapedRecipe(dlkey, this.getDiamondLeggings());
        diamond_leggings.shape("DDD", "DBD", "D*D");
        diamond_leggings.setIngredient('D', Material.DIAMOND);
        diamond_leggings.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_leggings.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_leggings);

        //Diamond Boots

        NamespacedKey dbkey = new NamespacedKey(plugin, "FTSdiamondboots");
        ShapedRecipe diamond_boots = new ShapedRecipe(dbkey, this.getDiamondBoots());
        diamond_boots.shape("***", "DBD", "D*D");
        diamond_boots.setIngredient('D', Material.DIAMOND);
        diamond_boots.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_boots.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_boots);

        NamespacedKey dbkey2 = new NamespacedKey(plugin, "FTSdiamondboots2");
        ShapedRecipe diamond_boots2 = new ShapedRecipe(dbkey2, this.getDiamondBoots());
        diamond_boots2.shape("***", "DBD", "D*D");
        diamond_boots2.setIngredient('D', Material.DIAMOND);
        diamond_boots2.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_boots2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_boots2);

        //Horse Leather Armor

        NamespacedKey lhakey = new NamespacedKey(plugin, "FTSleatherhorse");
        ShapedRecipe leatherHorseArmor = new ShapedRecipe(lhakey, this.getLeatherHorse());
        leatherHorseArmor.shape("LLL", "LSL", "LLL");
        leatherHorseArmor.setIngredient('L', Material.LEATHER);
        leatherHorseArmor.setIngredient('S', Material.SADDLE);
        plugin.getServer().addRecipe(leatherHorseArmor);

        //Horse Iron Armor

        NamespacedKey ihakey = new NamespacedKey(plugin, "FTSironhorse");
        ShapedRecipe ironHorseArmor = new ShapedRecipe(ihakey, this.getIronHorse());
        ironHorseArmor.shape("III", "ISI", "III");
        ironHorseArmor.setIngredient('I', Material.IRON_INGOT);
        ironHorseArmor.setIngredient('S', Material.SADDLE);
        plugin.getServer().addRecipe(ironHorseArmor);

        //Horse Diamond Armor

        NamespacedKey dhakey = new NamespacedKey(plugin, "FTSdiamondhorse");
        ShapedRecipe diamondHorseArmor = new ShapedRecipe(dhakey, this.getDiamondHorse());
        diamondHorseArmor.shape("DDD", "DSD", "DDD");
        diamondHorseArmor.setIngredient('D', Material.DIAMOND);
        diamondHorseArmor.setIngredient('S', Material.SADDLE);
        plugin.getServer().addRecipe(diamondHorseArmor);

        //Diamond Helmet Replacement

        NamespacedKey dhairkey = new NamespacedKey(plugin, "FTSdiamondhelmetair");
        ShapedRecipe diamond_helmet_air = new ShapedRecipe(dhairkey, this.getDiamondHelmetReplacement());
        diamond_helmet_air.shape("DDD", "D*D", "***");
        diamond_helmet_air.setIngredient('D', Material.DIAMOND);
        diamond_helmet_air.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_helmet_air);

        //Diamond Chestplate Replacement

        NamespacedKey dcairkey = new NamespacedKey(plugin, "FTSdiamondchestair");
        ShapedRecipe diamond_chest_air = new ShapedRecipe(dcairkey, this.getDiamondChestplateReplacement());
        diamond_chest_air.shape("D*D", "DDD", "DDD");
        diamond_chest_air.setIngredient('D', Material.DIAMOND);
        diamond_chest_air.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_chest_air);

        //Diamond Leggings Replacement

        NamespacedKey dlairkey = new NamespacedKey(plugin, "FTSdiamondlegsair");
        ShapedRecipe diamond_leggings_air = new ShapedRecipe(dlairkey, this.getDiamondLeggingsReplacement());
        diamond_leggings_air.shape("DDD", "D*D", "D*D");
        diamond_leggings_air.setIngredient('D', Material.DIAMOND);
        diamond_leggings_air.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_leggings_air);

        //Diamond Boots Replacement

        NamespacedKey dbairkey = new NamespacedKey(plugin, "FTSdiamondbootsair");
        ShapedRecipe diamond_boots_air = new ShapedRecipe(dbairkey, this.getDiamondBootsReplacement());
        diamond_boots_air.shape("***", "D*D", "D*D");
        diamond_boots_air.setIngredient('D', Material.DIAMOND);
        diamond_boots_air.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_boots_air);

        //Diamond Boots Replacement 2

        NamespacedKey dbairkey2 = new NamespacedKey(plugin, "FTSdiamondbootsair2");
        ShapedRecipe diamond_boots_air2 = new ShapedRecipe(dbairkey2, this.getDiamondBootsReplacement());
        diamond_boots_air2.shape("D*D", "D*D", "***");
        diamond_boots_air2.setIngredient('D', Material.DIAMOND);
        diamond_boots_air2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_boots_air2);

        //Copperpickaxe
        NamespacedKey cpickkey = new NamespacedKey(plugin, "FTSkupferpicke");
        ItemStack cpick = new ItemBuilder(Material.STONE_PICKAXE)
                .name("§fKupferspitzhacke")
                .enchant(Enchantment.DURABILITY, 2)
                .enchant(Enchantment.DIG_SPEED, 2)
                .sign("COPPER_PICKAXE")
                .build();
        cpick.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ShapedRecipe cpickr = new ShapedRecipe(cpickkey, cpick);
        cpickr.shape("CCC", "*S*", "*S*");
        cpickr.setIngredient('C', Material.COPPER_INGOT);
        cpickr.setIngredient('S', Material.STICK);
        cpickr.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(cpickr);

        //Copperaxe
        NamespacedKey caxekey = new NamespacedKey(plugin, "FTSkupferaxt");
        ItemStack caxe = new ItemBuilder(Material.STONE_AXE, 1)
                .name("§fKupferaxt")
                .enchant(Enchantment.DURABILITY, 2)
                .enchant(Enchantment.DIG_SPEED, 2)
                .sign("COPPER_AXE")
                .build();
        caxe.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ShapedRecipe caxer = new ShapedRecipe(caxekey, caxe);
        caxer.shape("*CC", "*SC", "*S*");
        caxer.setIngredient('C', Material.COPPER_INGOT);
        caxer.setIngredient('S', Material.STICK);
        caxer.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(caxer);

        ShapedRecipe caxel = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferaxt2"), caxe);
        caxel.shape("CC*", "CS*", "*S*");
        caxel.setIngredient('C', Material.COPPER_INGOT);
        caxel.setIngredient('S', Material.STICK);
        caxel.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(caxel);

        ShapedRecipe caxell = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferaxt3"), caxe);
        caxell.shape("CC*", "SC*", "S**");
        caxell.setIngredient('C', Material.COPPER_INGOT);
        caxell.setIngredient('S', Material.STICK);
        caxell.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(caxell);

        ShapedRecipe caxerr = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferaxt4"), caxe);
        caxerr.shape("*CC", "*CS", "**S");
        caxerr.setIngredient('C', Material.COPPER_INGOT);
        caxerr.setIngredient('S', Material.STICK);
        caxerr.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(caxerr);

        //Coppersword
        NamespacedKey cswordkey = new NamespacedKey(plugin, "FTSkupferschwert");
        ItemStack csword = new ItemBuilder(Material.STONE_SWORD)
                .name("§fKupferschwert")
                .enchant(Enchantment.DURABILITY, 2)
                .enchant(Enchantment.DIG_SPEED, 2)
                .sign("COPPER_SWORD")
                .build();
        csword.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ShapedRecipe cswordl = new ShapedRecipe(cswordkey, csword);
        cswordl.shape("C**", "C**", "S**");
        cswordl.setIngredient('C', Material.COPPER_INGOT);
        cswordl.setIngredient('S', Material.STICK);
        cswordl.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(cswordl);

        ShapedRecipe cswordr = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferschwert2"), csword);
        cswordr.shape("**C", "**C", "**S");
        cswordr.setIngredient('C', Material.COPPER_INGOT);
        cswordr.setIngredient('S', Material.STICK);
        cswordr.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(cswordr);

        ShapedRecipe cswordm = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferschwert3"), csword);
        cswordm.shape("*C*", "*C*", "*S*");
        cswordm.setIngredient('C', Material.COPPER_INGOT);
        cswordm.setIngredient('S', Material.STICK);
        cswordm.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(cswordm);

        //Copperhoe
        NamespacedKey choekey = new NamespacedKey(plugin, "FTSkupferhacke");
        ItemStack choe = new ItemBuilder(Material.STONE_HOE)
                .name("§fKupferhacke")
                .enchant(Enchantment.DURABILITY, 2)
                .enchant(Enchantment.DIG_SPEED, 2)
                .sign("COPPER_HOE")
                .build();
        choe.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ShapedRecipe choel = new ShapedRecipe(choekey, choe);
        choel.shape("CC*", "*S*", "*S*");
        choel.setIngredient('C', Material.COPPER_INGOT);
        choel.setIngredient('S', Material.STICK);
        choel.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(choel);

        ShapedRecipe choer = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferhacke2"), choe);
        choer.shape("*CC", "*S*", "*s*");
        choer.setIngredient('C', Material.COPPER_INGOT);
        choer.setIngredient('S', Material.STICK);
        choer.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(choer);

        ShapedRecipe choerr = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferhacke3"), choe);
        choerr.shape("*CC", "**S", "**S");
        choerr.setIngredient('C', Material.COPPER_INGOT);
        choerr.setIngredient('S', Material.STICK);
        choerr.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(choerr);

        ShapedRecipe choell = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferhacke4"), choe);
        choell.shape("CC*", "S**", "S**");
        choell.setIngredient('C', Material.COPPER_INGOT);
        choell.setIngredient('S', Material.STICK);
        choell.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(choell);

        //Coppershovel
        NamespacedKey cshovelkey = new NamespacedKey(plugin, "FTSkupferschaufel");
        ItemStack cshovel = new ItemBuilder(Material.STONE_SHOVEL)
                .name("§fKupferschaufel")
                .enchant(Enchantment.DURABILITY, 2)
                .enchant(Enchantment.DIG_SPEED, 2)
                .sign("COPPER_SHOVEL")
                .build();
        cshovel.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ShapedRecipe cshovelr = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferschaufel2"), cshovel);
        cshovelr.shape("**C", "**S", "**S");
        cshovelr.setIngredient('C', Material.COPPER_INGOT);
        cshovelr.setIngredient('S', Material.STICK);
        cshovelr.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(cshovelr);

        ShapedRecipe cshovelm = new ShapedRecipe(new NamespacedKey(plugin, "FTSkupferschaufel3"), cshovel);
        cshovelm.shape("*C*", "*S*", "*S*");
        cshovelm.setIngredient('C', Material.COPPER_INGOT);
        cshovelm.setIngredient('S', Material.STICK);
        cshovelm.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(cshovelm);

        // FallAxt
        ItemStack lumberAxeStack = new ItemStack(Material.DIAMOND_AXE, 1);
        ItemMeta lumberAxeMeta = lumberAxeStack.getItemMeta();
        lumberAxeMeta.displayName(Component.text(ChatColor.DARK_PURPLE + "FallAxt"));
        lumberAxeMeta.setCustomModelData(1001);
        lumberAxeStack.setItemMeta(lumberAxeMeta);
        lumberAxeStack.addEnchantment(Enchantment.DURABILITY, 2);
        lumberAxeStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ShapedRecipe lumberAxeRecipe = new ShapedRecipe(new NamespacedKey(plugin, "FTSfallaxt"), lumberAxeStack);
        lumberAxeRecipe.shape("*EE", "*SE", "*S*");
        lumberAxeRecipe.setIngredient('E', Material.EMERALD);
        lumberAxeRecipe.setIngredient('S', Material.STICK);
        lumberAxeRecipe.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(lumberAxeRecipe);

        ShapedRecipe lumberAxeRecipe2 = new ShapedRecipe(new NamespacedKey(plugin, "FTSfallaxt2"), lumberAxeStack);
        lumberAxeRecipe2.shape("EE*", "ES*", "*S*");
        lumberAxeRecipe2.setIngredient('E', Material.EMERALD);
        lumberAxeRecipe2.setIngredient('S', Material.STICK);
        lumberAxeRecipe2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(lumberAxeRecipe2);
    }
}

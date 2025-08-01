package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.logport.LogportManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsutils.items.ItemBuilder;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ItemStacks {

    private ItemStack ironHelmet, ironChestplate, ironLeggings, ironBoots,
            chainmailHelmet, chainmailChestplate, chainmailLeggings, chainmailBoots,
            diamondHelmet, diamondChestplate, diamondLeggings, diamondBoots;
    private ItemStack diamondHelmetReplacement, diamondChestplateReplacement, diamondLeggingsReplacement, diamondBootsReplacement;
    private ItemStack leatherHorse, ironHorse, diamondHorse;
    private ItemStack dragonBreath;
    private ItemStack meissel;
    private ItemStack logport;
    private ItemStack fertilizer;
    private ItemStack horn;
    private ItemStack harp, chime, flute, guitar, bell, cowBell, xylophone, ironXylophone, bassGuitar;
    private ItemStack hiddenBundle;

    private List<Material> disabledDefaultRecipes;

    private final Engine plugin;
    private final LogportManager logportManager;

    public ItemStacks(Engine plugin) {
        this.plugin = plugin;
        logportManager = plugin.getLogportManager();
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

        dragonBreath = new ItemStack(Material.DRAGON_BREATH, 1);

        meissel = new ItemBuilder(Material.ARROW).name("§6Meißel").lore("§7Ein nützliches Werkzeug zum Steine verarbeiten").sign("MEISSEL").build();

        fertilizer = new ItemBuilder(Material.BONE_MEAL).name("§6Dünger").lore("§7Lässt Pflanzen wachsen!").sign("FERTILIZER").build();

        logport = new ItemBuilder(Material.RECOVERY_COMPASS).name("§6Logport").lore("§7Teleportiert dich zu einem vorher festgelegten Punkt").sign("LOGPORT").build();
        ItemMeta logportMeta = logport.getItemMeta();
        PersistentDataContainer data = logportMeta.getPersistentDataContainer();
        data.set(new NamespacedKey(plugin, LogportManager.USES_LEFT_KEY), PersistentDataType.INTEGER, 5);
        data.set(new NamespacedKey(plugin, LogportManager.MAX_USES_KEY), PersistentDataType.INTEGER, 5);

        logportManager.updateLogportLore(logportMeta);
        logport.setItemMeta(logportMeta);

        final String instrumentSign = "INSTRUMENT";
        harp = new ItemBuilder(Material.STICK).name("§6Harfe").sign(instrumentSign).addPDC("type", 0, PersistentDataType.INTEGER).build();
        chime = new ItemBuilder(Material.STICK).name("§6Chimes").sign(instrumentSign).addPDC("type", 1, PersistentDataType.INTEGER).build();
        flute = new ItemBuilder(Material.STICK).name("§6Flöte").sign(instrumentSign).addPDC("type", 2, PersistentDataType.INTEGER).build();
        bell = new ItemBuilder(Material.STICK).name("§6Glockenspiel").sign(instrumentSign).addPDC("type", 3, PersistentDataType.INTEGER).build();
        guitar = new ItemBuilder(Material.STICK).name("§6Laute").sign(instrumentSign).addPDC("type", 4, PersistentDataType.INTEGER).build();
        cowBell = new ItemBuilder(Material.STICK).name("§6Kuhglocke").sign(instrumentSign).addPDC("type", 5, PersistentDataType.INTEGER).build();
        xylophone = new ItemBuilder(Material.STICK).name("§6Knochenxylophon").sign(instrumentSign).addPDC("type", 6, PersistentDataType.INTEGER).build();
        ironXylophone = new ItemBuilder(Material.STICK).name("§6Eisen-Xylophon").sign(instrumentSign).addPDC("type", 7, PersistentDataType.INTEGER).build();
        bassGuitar = new ItemBuilder(Material.STICK).name("§6Kontrabass").sign(instrumentSign).addPDC("type", 8, PersistentDataType.INTEGER).build();

        hiddenBundle = new ItemBuilder(Material.BUNDLE).name("§6Verstecktes Bündel").lore("§7Dieses Bündel wird beim Durchsuchen nur mit").lore("§7einer Wahrscheinlichkeit von 10% gefunden.").sign("HIDDEN_BUNDLE").build();

    }

    public List<Material> getDisabledDefaultRecipes() {
        return disabledDefaultRecipes;
    }

    public ItemStack getHorn() {
        return horn;
    }

    private void initRecipes() {

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTSharp");
            ShapedRecipe recipe = new ShapedRecipe(key, harp);
            recipe.shape(" AB", "ACB", "AB ");
            recipe.setIngredient('A', Material.STRIPPED_MANGROVE_LOG);
            recipe.setIngredient('B', Material.STRING);
            recipe.setIngredient('C', Material.JUKEBOX);
            Bukkit.addRecipe(recipe);
        }

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTSbell");
            ShapedRecipe recipe = new ShapedRecipe(key, bell);
            recipe.shape("   ", "ABA", "CCC");
            recipe.setIngredient('A', Material.IRON_NUGGET);
            recipe.setIngredient('B', Material.JUKEBOX);
            recipe.setIngredient('C', Material.STRIPPED_CHERRY_LOG);
            Bukkit.addRecipe(recipe);
        }

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTSflute");
            ShapedRecipe recipe = new ShapedRecipe(key, flute);
            recipe.shape("   ", "ABA", "CCC");
            recipe.setIngredient('A', Material.QUARTZ);
            recipe.setIngredient('B', Material.JUKEBOX);
            recipe.setIngredient('C', Material.STRIPPED_OAK_LOG);
            Bukkit.addRecipe(recipe);
        }

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTSchime");
            ShapedRecipe recipe = new ShapedRecipe(key, chime);
            recipe.shape("AAA", "BCB", " B ");
            recipe.setIngredient('A', Material.STRIPPED_DARK_OAK_LOG);
            recipe.setIngredient('B', Material.GOLD_NUGGET);
            recipe.setIngredient('C', Material.JUKEBOX);
            Bukkit.addRecipe(recipe);
        }

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTScowbell");
            ShapedRecipe recipe = new ShapedRecipe(key, cowBell);
            recipe.shape("ASA", "AJA", "AGA");
            recipe.setIngredient('J', Material.JUKEBOX);
            recipe.setIngredient('S', Material.STICK);
            recipe.setIngredient('G', Material.GOLD_NUGGET);
            Bukkit.addRecipe(recipe);
        }

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTSxylophon");
            ShapedRecipe recipe = new ShapedRecipe(key, xylophone);
            recipe.shape("   ", "ABA", "CCC");
            recipe.setIngredient('A', Material.BONE);
            recipe.setIngredient('B', Material.JUKEBOX);
            recipe.setIngredient('C', Material.STRIPPED_BAMBOO_BLOCK);
            Bukkit.addRecipe(recipe);
        }

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTSironxylophon");
            ShapedRecipe recipe = new ShapedRecipe(key, ironXylophone);
            recipe.shape("AIA", "AJA", "AOA");
            recipe.setIngredient('I', Material.IRON_NUGGET);
            recipe.setIngredient('O', Material.OAK_PLANKS);
            recipe.setIngredient('J', Material.JUKEBOX);
            Bukkit.addRecipe(recipe);
        }

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTSguitar");
            ShapedRecipe recipe = new ShapedRecipe(key, guitar);
            recipe.shape("AB ", "ACB", "AB ");
            recipe.setIngredient('A', Material.STRING);
            recipe.setIngredient('B', Material.STRIPPED_ACACIA_LOG);
            recipe.setIngredient('C', Material.JUKEBOX);
            Bukkit.addRecipe(recipe);
        }

        {
            NamespacedKey key = new NamespacedKey(plugin, "FTSbassguitar");
            ShapedRecipe recipe = new ShapedRecipe(key, bassGuitar);
            recipe.shape("AB ", "ACB", "AB ");
            recipe.setIngredient('A', Material.STRING);
            recipe.setIngredient('C', Material.JUKEBOX);
            recipe.setIngredient('B', Material.STRIPPED_SPRUCE_LOG);
            Bukkit.addRecipe(recipe);
        }

        NamespacedKey lanzeKey = new NamespacedKey(plugin, "FTSlanze");
        ItemStack lanzeItemStack = new ItemBuilder(Material.STICK)
                .name("§6Lanze")
                .sign("LANZE")
                .build();

        ShapedRecipe lanzeRecipe = new ShapedRecipe(lanzeKey, lanzeItemStack);
        lanzeRecipe.shape("F  ", " S ", "  E");
        lanzeRecipe.setIngredient('F', Material.FLINT);
        lanzeRecipe.setIngredient('S', Material.STICK);
        lanzeRecipe.setIngredient('E', Material.IRON_INGOT);
        plugin.getServer().addRecipe(lanzeRecipe);

        //Horn

        NamespacedKey hornkey = new NamespacedKey(plugin, "FTSHorn");

        ShapedRecipe horn = new ShapedRecipe(hornkey, this.getHorn());
        horn.shape(" A ", " B ", " C ");
        horn.setIngredient('A', Material.NAUTILUS_SHELL);
        horn.setIngredient('B', Material.STRING);
        horn.setIngredient('C', Material.JUKEBOX);
        plugin.getServer().addRecipe(horn);


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
        ShapedRecipe iron_helmet = new ShapedRecipe(ihkey, ironHelmet);
        iron_helmet.shape("III", "IBI", "***");
        iron_helmet.setIngredient('I', Material.IRON_INGOT);
        iron_helmet.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_helmet);

        NamespacedKey ihkey2 = new NamespacedKey(plugin, "FTSironhelmet2");
        ShapedRecipe iron_helmet2 = new ShapedRecipe(ihkey2, ironHelmet);
        iron_helmet2.shape("***", "III", "IBI");
        iron_helmet2.setIngredient('I', Material.IRON_INGOT);
        iron_helmet2.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_helmet2);

        //IronChestplate

        NamespacedKey icpkey = new NamespacedKey(plugin, "FTSironchestplate");
        ShapedRecipe iron_chestplate = new ShapedRecipe(icpkey, ironChestplate);
        iron_chestplate.shape("I*I", "IBI", "III");
        iron_chestplate.setIngredient('I', Material.IRON_INGOT);
        iron_chestplate.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_chestplate);

        //IronLeggings

        NamespacedKey ilkey = new NamespacedKey(plugin, "FTSironleggings");
        ShapedRecipe iron_leggings = new ShapedRecipe(ilkey, ironLeggings);
        iron_leggings.shape("III", "IBI", "I*I");
        iron_leggings.setIngredient('I', Material.IRON_INGOT);
        iron_leggings.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_leggings);

        //IronBoots

        NamespacedKey ibkey = new NamespacedKey(plugin, "FTSironboots");
        ShapedRecipe iron_boots = new ShapedRecipe(ibkey, ironBoots);
        iron_boots.shape("***", "IBI", "I*I");
        iron_boots.setIngredient('I', Material.IRON_INGOT);
        iron_boots.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_boots);

        NamespacedKey ibkey2 = new NamespacedKey(plugin, "FTSironboots2");
        ShapedRecipe iron_boots2 = new ShapedRecipe(ibkey2, ironBoots);
        iron_boots2.shape("IBI", "I*I", "***");
        iron_boots2.setIngredient('I', Material.IRON_INGOT);
        iron_boots2.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_boots2);


        //ChainmailHelmet

        NamespacedKey chkey = new NamespacedKey(plugin, "FTSchainhelmet");
        ShapedRecipe chain_helmet = new ShapedRecipe(chkey, chainmailHelmet);
        chain_helmet.shape("III", "I*I", "***");
        chain_helmet.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(chain_helmet);

        NamespacedKey chkey2 = new NamespacedKey(plugin, "FTSchainhelmet2");
        ShapedRecipe chain_helmet2 = new ShapedRecipe(chkey2, chainmailHelmet);
        chain_helmet2.shape("***", "III", "I*I");
        chain_helmet2.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(chain_helmet2);

        //ChainmailChestplate

        NamespacedKey ccpkey = new NamespacedKey(plugin, "FTSchainmailchestplate");
        ShapedRecipe chainmail_chestplate = new ShapedRecipe(ccpkey, chainmailChestplate);
        chainmail_chestplate.shape("I*I", "III", "III");
        chainmail_chestplate.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(chainmail_chestplate);

        //ChainmailLeggings

        NamespacedKey clkey = new NamespacedKey(plugin, "FTSchainleggings");
        ShapedRecipe chain_leggings = new ShapedRecipe(clkey, chainmailLeggings);
        chain_leggings.shape("III", "I*I", "I*I");
        chain_leggings.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(chain_leggings);

        //ChainmailBoots

        NamespacedKey cbkey = new NamespacedKey(plugin, "FTSchainboots");
        ShapedRecipe chain_boots = new ShapedRecipe(cbkey, chainmailBoots);
        chain_boots.shape("***", "I*I", "I*I");
        chain_boots.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(chain_boots);

        NamespacedKey cbkey2 = new NamespacedKey(plugin, "FTSchainboots2");
        ShapedRecipe chain_boots2 = new ShapedRecipe(cbkey2, chainmailBoots);
        chain_boots2.shape("I*I", "I*I", "***");
        chain_boots2.setIngredient('I', Material.IRON_INGOT);
        plugin.getServer().addRecipe(chain_boots2);

        //Diamond Helmet

        NamespacedKey dhkey = new NamespacedKey(plugin, "FTSdiamondhelmet");
        ShapedRecipe diamond_helmet = new ShapedRecipe(dhkey, diamondHelmet);
        diamond_helmet.shape("DDD", "DBD", "***");
        diamond_helmet.setIngredient('D', Material.DIAMOND);
        diamond_helmet.setIngredient('B', Material.DIAMOND_BLOCK);
        plugin.getServer().addRecipe(diamond_helmet);

        NamespacedKey dhkey2 = new NamespacedKey(plugin, "FTSdiamondhelmet2");
        ShapedRecipe diamond_helmet2 = new ShapedRecipe(dhkey2, diamondHelmet);
        diamond_helmet2.shape("***", "DDD", "DBD");
        diamond_helmet2.setIngredient('D', Material.DIAMOND);
        diamond_helmet2.setIngredient('B', Material.DIAMOND_BLOCK);
        plugin.getServer().addRecipe(diamond_helmet2);

        //Diamond Chestplate

        NamespacedKey dckey = new NamespacedKey(plugin, "FTSdiamondchest");
        ShapedRecipe diamond_chest = new ShapedRecipe(dckey, diamondChestplate);
        diamond_chest.shape("D*D", "DBD", "DDD");
        diamond_chest.setIngredient('D', Material.DIAMOND);
        diamond_chest.setIngredient('B', Material.DIAMOND_BLOCK);
        plugin.getServer().addRecipe(diamond_chest);

        //Diamond Leggings

        NamespacedKey dlkey = new NamespacedKey(plugin, "FTSdiamondlegs");
        ShapedRecipe diamond_leggings = new ShapedRecipe(dlkey, diamondLeggings);
        diamond_leggings.shape("DDD", "DBD", "D*D");
        diamond_leggings.setIngredient('D', Material.DIAMOND);
        diamond_leggings.setIngredient('B', Material.DIAMOND_BLOCK);
        plugin.getServer().addRecipe(diamond_leggings);

        //Diamond Boots

        NamespacedKey dbkey = new NamespacedKey(plugin, "FTSdiamondboots");
        ShapedRecipe diamond_boots = new ShapedRecipe(dbkey, diamondBoots);
        diamond_boots.shape("***", "DBD", "D*D");
        diamond_boots.setIngredient('D', Material.DIAMOND);
        diamond_boots.setIngredient('B', Material.DIAMOND_BLOCK);
        plugin.getServer().addRecipe(diamond_boots);

        NamespacedKey dbkey2 = new NamespacedKey(plugin, "FTSdiamondboots2");
        ShapedRecipe diamond_boots2 = new ShapedRecipe(dbkey2, diamondBoots);
        diamond_boots2.shape("***", "DBD", "D*D");
        diamond_boots2.setIngredient('D', Material.DIAMOND);
        diamond_boots2.setIngredient('B', Material.DIAMOND_BLOCK);
        plugin.getServer().addRecipe(diamond_boots2);

        //Horse Leather Armor

        NamespacedKey lhakey = new NamespacedKey(plugin, "FTSleatherhorse");
        ShapedRecipe leatherHorseArmor = new ShapedRecipe(lhakey, leatherHorse);
        leatherHorseArmor.shape("LLL", "LSL", "LLL");
        leatherHorseArmor.setIngredient('L', Material.LEATHER);
        leatherHorseArmor.setIngredient('S', Material.SADDLE);
        plugin.getServer().addRecipe(leatherHorseArmor);

        //Horse Iron Armor

        NamespacedKey ihakey = new NamespacedKey(plugin, "FTSironhorse");
        ShapedRecipe ironHorseArmor = new ShapedRecipe(ihakey, ironHorse);
        ironHorseArmor.shape("III", "ISI", "III");
        ironHorseArmor.setIngredient('I', Material.IRON_INGOT);
        ironHorseArmor.setIngredient('S', Material.SADDLE);
        plugin.getServer().addRecipe(ironHorseArmor);

        //Horse Diamond Armor

        NamespacedKey dhakey = new NamespacedKey(plugin, "FTSdiamondhorse");
        ShapedRecipe diamondHorseArmor = new ShapedRecipe(dhakey, diamondHorse);
        diamondHorseArmor.shape("DDD", "DSD", "DDD");
        diamondHorseArmor.setIngredient('D', Material.DIAMOND);
        diamondHorseArmor.setIngredient('S', Material.SADDLE);
        plugin.getServer().addRecipe(diamondHorseArmor);

        //Diamond Helmet Replacement

        NamespacedKey dhairkey = new NamespacedKey(plugin, "FTSdiamondhelmetair");
        ShapedRecipe diamond_helmet_air = new ShapedRecipe(dhairkey, diamondHelmetReplacement);
        diamond_helmet_air.shape("DDD", "D*D", "***");
        diamond_helmet_air.setIngredient('D', Material.DIAMOND);
        plugin.getServer().addRecipe(diamond_helmet_air);

        //Diamond Chestplate Replacement

        NamespacedKey dcairkey = new NamespacedKey(plugin, "FTSdiamondchestair");
        ShapedRecipe diamond_chest_air = new ShapedRecipe(dcairkey, diamondChestplateReplacement);
        diamond_chest_air.shape("D*D", "DDD", "DDD");
        diamond_chest_air.setIngredient('D', Material.DIAMOND);
        plugin.getServer().addRecipe(diamond_chest_air);

        //Diamond Leggings Replacement

        NamespacedKey dlairkey = new NamespacedKey(plugin, "FTSdiamondlegsair");
        ShapedRecipe diamond_leggings_air = new ShapedRecipe(dlairkey, diamondLeggingsReplacement);
        diamond_leggings_air.shape("DDD", "D*D", "D*D");
        diamond_leggings_air.setIngredient('D', Material.DIAMOND);
        plugin.getServer().addRecipe(diamond_leggings_air);

        //Diamond Boots Replacement

        NamespacedKey dbairkey = new NamespacedKey(plugin, "FTSdiamondbootsair");
        ShapedRecipe diamond_boots_air = new ShapedRecipe(dbairkey, diamondBootsReplacement);
        diamond_boots_air.shape("***", "D*D", "D*D");
        diamond_boots_air.setIngredient('D', Material.DIAMOND);
        plugin.getServer().addRecipe(diamond_boots_air);

        //Diamond Boots Replacement 2

        NamespacedKey dbairkey2 = new NamespacedKey(plugin, "FTSdiamondbootsair2");
        ShapedRecipe diamond_boots_air2 = new ShapedRecipe(dbairkey2, diamondBootsReplacement);
        diamond_boots_air2.shape("D*D", "D*D", "***");
        diamond_boots_air2.setIngredient('D', Material.DIAMOND);
        plugin.getServer().addRecipe(diamond_boots_air2);

        // Dragon Breath

        ShapedRecipe dragonBreathRecipe = new ShapedRecipe(new NamespacedKey(plugin, "FTS_DRAGONBREATH"), dragonBreath);
        dragonBreathRecipe.shape("RLR", "LRL", "LWL");
        dragonBreathRecipe.setIngredient('R', Material.REDSTONE);
        dragonBreathRecipe.setIngredient('L', Material.LAPIS_LAZULI);
        dragonBreathRecipe.setIngredient('W', Material.POTION);
        plugin.getServer().addRecipe(dragonBreathRecipe);

        //Meissel

        ShapedRecipe meisselRecipe = new ShapedRecipe(new NamespacedKey(plugin, "FTS_MEISSEL"), meissel);
        meisselRecipe.shape("AAA", "AIA", "EEE");
        meisselRecipe.setIngredient('I', Material.IRON_INGOT);
        meisselRecipe.setIngredient('E', Material.EMERALD);
        plugin.getServer().addRecipe(meisselRecipe);

        //Logport

        ShapedRecipe logportRecipe = new ShapedRecipe(new NamespacedKey(plugin, "FTS_LOGPORT"), logport);
        logportRecipe.shape("EEE", "ECE", "EEE");
        logportRecipe.setIngredient('C', Material.COMPASS);
        logportRecipe.setIngredient('E', Material.EMERALD);
        plugin.getServer().addRecipe(logportRecipe);

        //Dünger

        ShapedRecipe fertilizerRecipe = new ShapedRecipe(new NamespacedKey(plugin, "FERTILIZER"), fertilizer);
        fertilizerRecipe.shape("BBB", "BLB", "BRB");
        fertilizerRecipe.setIngredient('B', Material.BONE_MEAL);
        fertilizerRecipe.setIngredient('L', Material.LAPIS_LAZULI);
        fertilizerRecipe.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(fertilizerRecipe);

        //Hidden Bundle

        ShapedRecipe hiddenBundleRecipe1 = new ShapedRecipe(new NamespacedKey(plugin, "HIDDEN_BUNDLE1"), hiddenBundle);
        hiddenBundleRecipe1.shape("***", "BLH", "***");
        hiddenBundleRecipe1.setIngredient('B', Material.BUNDLE);
        hiddenBundleRecipe1.setIngredient('L', Material.LEAD);
        hiddenBundleRecipe1.setIngredient('H', Material.TRIPWIRE_HOOK);
        plugin.getServer().addRecipe(hiddenBundleRecipe1);

        ShapedRecipe hiddenBundleRecipe2 = new ShapedRecipe(new NamespacedKey(plugin, "HIDDEN_BUNDLE2"), hiddenBundle);
        hiddenBundleRecipe2.shape("BLH", "***", "***");
        hiddenBundleRecipe2.setIngredient('B', Material.BUNDLE);
        hiddenBundleRecipe2.setIngredient('L', Material.LEAD);
        hiddenBundleRecipe2.setIngredient('H', Material.TRIPWIRE_HOOK);
        plugin.getServer().addRecipe(hiddenBundleRecipe2);

        ShapedRecipe hiddenBundleRecipe3 = new ShapedRecipe(new NamespacedKey(plugin, "HIDDEN_BUNDLE3"), hiddenBundle);
        hiddenBundleRecipe3.shape("***", "***", "BLH");
        hiddenBundleRecipe3.setIngredient('B', Material.BUNDLE);
        hiddenBundleRecipe3.setIngredient('L', Material.LEAD);
        hiddenBundleRecipe3.setIngredient('H', Material.TRIPWIRE_HOOK);
        plugin.getServer().addRecipe(hiddenBundleRecipe3);
    }
}

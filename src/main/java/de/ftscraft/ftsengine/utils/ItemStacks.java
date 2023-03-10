package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.main.Engine;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class ItemStacks {

    private ItemStack big_bp, tiny_bp, bp_key, ender_bp;
    private ItemStack iron_helmet, iron_chestplate, iron_leggings, iron_boots,
            chainmail_helmet, chainmail_chestplate, chainmail_leggings, chainmail_boots,
            diamond_helmet, diamond_chestplate, diamond_leggings, diamond_boots, gold_ingot;
    private ItemStack diamondHelmetReplacement, diamondChestplateReplacement, diamondLeggingsReplacement, diamondBootsReplacement;
    private ItemStack leather_horse, iron_horse, diamond_horse;
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

        disabledDefaultRecipes = new ArrayList<Material>();
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
        big_bp = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta big_bpM = (LeatherArmorMeta) big_bp.getItemMeta();
        big_bpM.setColor(Color.RED);
        big_bpM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        big_bpM.setDisplayName(BackpackType.LARGE.getName());
        big_bpM.setLore(Arrays.asList("§7In diesen Rucksack passen viele, weitere Dinge rein", "ID: #-1"));
        big_bp.setItemMeta(big_bpM);

        //TINY BP
        tiny_bp = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta tiny_bpM = (LeatherArmorMeta) tiny_bp.getItemMeta();
        tiny_bpM.setColor(Color.GREEN);
        tiny_bpM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        tiny_bpM.setDisplayName(BackpackType.TINY.getName());
        tiny_bpM.setLore(Arrays.asList("§7Dieser nützliche Rucksack hat Platz für viele Sachen", "ID: #-1"));
        tiny_bp.setItemMeta(tiny_bpM);

        //ENDER BP
        ender_bp = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta ender_bpM = (LeatherArmorMeta) ender_bp.getItemMeta();
        ender_bpM.setColor(Color.PURPLE);
        ender_bpM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        ender_bpM.setDisplayName(BackpackType.ENDER.getName());
        tiny_bpM.setLore(Collections.singletonList(BackpackType.ENDER.getLore()));
        ender_bp.setItemMeta(ender_bpM);

        //BP KEY
        bp_key = new ItemStack(Material.FEATHER, 1);
        ItemMeta bp_keyM = bp_key.getItemMeta();
        bp_keyM.setDisplayName("§5Rucksack Schlüssel");
        bp_key.setItemMeta(bp_keyM);

        //Horn
        horn = new ItemStack(Material.NAUTILUS_SHELL, 1);
        ItemMeta hornM = horn.getItemMeta();
        hornM.setDisplayName("§6Horn");
        horn.setItemMeta(hornM);

        iron_helmet = new ItemStack(Material.IRON_HELMET, 1);
        iron_chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        iron_leggings = new ItemStack(Material.IRON_LEGGINGS, 1);
        iron_boots = new ItemStack(Material.IRON_BOOTS, 1);

        chainmail_helmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        chainmail_chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        chainmail_leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        chainmail_boots = new ItemStack(Material.CHAINMAIL_BOOTS, 1);

        diamond_helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        diamond_chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        diamond_leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        diamond_boots = new ItemStack(Material.DIAMOND_BOOTS, 1);

        leather_horse = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        iron_horse = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
        diamond_horse = new ItemStack(Material.DIAMOND_HORSE_ARMOR, 1);

        diamondHelmetReplacement = new ItemStack(Material.DIAMOND, 5);
        diamondChestplateReplacement = new ItemStack(Material.DIAMOND, 8);
        diamondLeggingsReplacement = new ItemStack(Material.DIAMOND, 7);
        diamondBootsReplacement = new ItemStack(Material.DIAMOND, 4);

        gold = new ItemStack(Material.GOLD_INGOT, 1);

    }

    public ItemStack getBig_bp() {
        return big_bp;
    }

    public ItemStack getGold() {
        return gold;
    }

    public ItemStack getTiny_bp() {
        return tiny_bp;
    }

    public ItemStack getBp_key() {
        return bp_key;
    }

    public ItemStack getEnder_bp() {
        return ender_bp;
    }

    public ItemStack getIron_chestplate() {
        return iron_chestplate;
    }

    public ItemStack getChainmail_chestplate() {
        return chainmail_chestplate;
    }

    public ItemStack getIron_helmet() {
        return iron_helmet;
    }

    public ItemStack getIron_leggings() {
        return iron_leggings;
    }

    public ItemStack getIron_boots() {
        return iron_boots;
    }

    public ItemStack getChainmail_helmet() {
        return chainmail_helmet;
    }

    public ItemStack getChainmail_leggings() {
        return chainmail_leggings;
    }

    public ItemStack getChainmail_boots() {
        return chainmail_boots;
    }

    public ItemStack getDiamond_helmet() {
        return diamond_helmet;
    }

    public ItemStack getDiamond_chestplate() {
        return diamond_chestplate;
    }

    public ItemStack getDiamond_leggings() {
        return diamond_leggings;
    }

    public ItemStack getDiamond_boots() {
        return diamond_boots;
    }

    public ItemStack getLeather_horse() {
        return leather_horse;
    }

    public ItemStack getIron_horse() {
        return iron_horse;
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

    public ItemStack getDiamond_horse() {
        return diamond_horse;
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
        NamespacedKey lanzekey = new NamespacedKey(plugin, "FTSlanze");
        ItemStack isl = new ItemStack(Material.STICK, 1);
        ItemMeta iml = isl.getItemMeta();
        iml.setDisplayName("§6Lanze");
        isl.setItemMeta(iml);

        ShapedRecipe lanze = new ShapedRecipe(lanzekey, isl);
        lanze.shape("F**", "*S*", "**E");
        lanze.setIngredient('F', Material.FLINT);
        lanze.setIngredient('S', Material.STICK);
        lanze.setIngredient('E', Material.IRON_INGOT);
        lanze.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(lanze);

        plugin.mats.add(Material.DIRT_PATH);

        plugin.mats.addAll(Var.getCarpets());

        //TINY BACKPACK

        NamespacedKey tbpkey = new NamespacedKey(plugin, "FTStinybackpack");

        ShapedRecipe tiny_backpack = new ShapedRecipe(tbpkey, this.getTiny_bp());
        tiny_backpack.shape("HHH", "HCH", "HHH");
        tiny_backpack.setIngredient('H', Material.RABBIT_HIDE);
        tiny_backpack.setIngredient('C', Material.CHEST);
        plugin.getServer().addRecipe(tiny_backpack);

        //LARGE BACKPACK

        NamespacedKey lbpkey = new NamespacedKey(plugin, "FTSlargebackpack");
        ShapedRecipe large_backpack = new ShapedRecipe(lbpkey, this.getBig_bp());
        large_backpack.shape("LLL", "LRL", "LLL");
        large_backpack.setIngredient('L', Material.LEATHER);
        large_backpack.setIngredient('R', this.getTiny_bp());
        plugin.getServer().addRecipe(large_backpack);

        //GOLD INGOT
/*        NamespacedKey goldKey = new NamespacedKey(plugin, "FTSGOLD");
        ShapelessRecipe goldRec = new ShapelessRecipe(goldKey, this.getGold());
        goldRec.addIngredient(2, Material.RAW_GOLD);
        goldRec.addIngredient(Material.COPPER_INGOT);
        plugin.getServer().addRecipe(goldRec);*/


        //ENDER BACKPACK

        NamespacedKey ebpkey = new NamespacedKey(plugin, "FTSenderbackpack");
        ShapedRecipe ender_backpack = new ShapedRecipe(ebpkey, this.getEnder_bp());
        ender_backpack.shape("LLL", "LEL", "LLL");
        ender_backpack.setIngredient('L', Material.LEATHER);
        ender_backpack.setIngredient('E', Material.ENDER_CHEST);
        plugin.getServer().addRecipe(ender_backpack);

        //BACKPACK KEY

        NamespacedKey bpkkey = new NamespacedKey(plugin, "FTSbackpackkey");

        ShapedRecipe backpack_key = new ShapedRecipe(bpkkey, this.getBp_key());
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

        List<Recipe> backup = new ArrayList<Recipe>();
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
        ShapedRecipe iron_helmet = new ShapedRecipe(ihkey, this.getIron_helmet());
        iron_helmet.shape("III", "IBI", "***");
        iron_helmet.setIngredient('I', Material.IRON_INGOT);
        iron_helmet.setIngredient('*', Material.AIR);
        iron_helmet.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_helmet);

        NamespacedKey ihkey2 = new NamespacedKey(plugin, "FTSironhelmet2");
        ShapedRecipe iron_helmet2 = new ShapedRecipe(ihkey2, this.getIron_helmet());
        iron_helmet2.shape("***", "III", "IBI");
        iron_helmet2.setIngredient('I', Material.IRON_INGOT);
        iron_helmet2.setIngredient('*', Material.AIR);
        iron_helmet2.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_helmet2);

        //IronChestplate

        NamespacedKey icpkey = new NamespacedKey(plugin, "FTSironchestplate");
        ShapedRecipe iron_chestplate = new ShapedRecipe(icpkey, this.getIron_chestplate());
        iron_chestplate.shape("I*I", "IBI", "III");
        iron_chestplate.setIngredient('I', Material.IRON_INGOT);
        iron_chestplate.setIngredient('*', Material.AIR);
        iron_chestplate.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_chestplate);

        //IronLeggings

        NamespacedKey ilkey = new NamespacedKey(plugin, "FTSironleggings");
        ShapedRecipe iron_leggings = new ShapedRecipe(ilkey, this.getIron_leggings());
        iron_leggings.shape("III", "IBI", "I*I");
        iron_leggings.setIngredient('I', Material.IRON_INGOT);
        iron_leggings.setIngredient('*', Material.AIR);
        iron_leggings.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_leggings);

        //IronBoots

        NamespacedKey ibkey = new NamespacedKey(plugin, "FTSironboots");
        ShapedRecipe iron_boots = new ShapedRecipe(ibkey, this.getIron_boots());
        iron_boots.shape("***", "IBI", "I*I");
        iron_boots.setIngredient('I', Material.IRON_INGOT);
        iron_boots.setIngredient('*', Material.AIR);
        iron_boots.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_boots);

        NamespacedKey ibkey2 = new NamespacedKey(plugin, "FTSironboots2");
        ShapedRecipe iron_boots2 = new ShapedRecipe(ibkey2, this.getIron_boots());
        iron_boots2.shape("IBI", "I*I", "***");
        iron_boots2.setIngredient('I', Material.IRON_INGOT);
        iron_boots2.setIngredient('*', Material.AIR);
        iron_boots2.setIngredient('B', Material.IRON_BLOCK);
        plugin.getServer().addRecipe(iron_boots2);


        //ChainmailHelmet

        NamespacedKey chkey = new NamespacedKey(plugin, "FTSchainhelmet");
        ShapedRecipe chain_helmet = new ShapedRecipe(chkey, this.getChainmail_helmet());
        chain_helmet.shape("III", "I*I", "***");
        chain_helmet.setIngredient('I', Material.IRON_INGOT);
        chain_helmet.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_helmet);

        NamespacedKey chkey2 = new NamespacedKey(plugin, "FTSchainhelmet2");
        ShapedRecipe chain_helmet2 = new ShapedRecipe(chkey2, this.getChainmail_helmet());
        chain_helmet2.shape("***", "III", "I*I");
        chain_helmet2.setIngredient('I', Material.IRON_INGOT);
        chain_helmet2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_helmet2);

        //ChainmailChestplate

        NamespacedKey ccpkey = new NamespacedKey(plugin, "FTSchainmailchestplate");
        ShapedRecipe chainmail_chestplate = new ShapedRecipe(ccpkey, this.getChainmail_chestplate());
        chainmail_chestplate.shape("I*I", "III", "III");
        chainmail_chestplate.setIngredient('I', Material.IRON_INGOT);
        chainmail_chestplate.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chainmail_chestplate);

        //ChainmailLeggings

        NamespacedKey clkey = new NamespacedKey(plugin, "FTSchainleggings");
        ShapedRecipe chain_leggings = new ShapedRecipe(clkey, this.getChainmail_leggings());
        chain_leggings.shape("III", "I*I", "I*I");
        chain_leggings.setIngredient('I', Material.IRON_INGOT);
        chain_leggings.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_leggings);

        //ChainmailBoots

        NamespacedKey cbkey = new NamespacedKey(plugin, "FTSchainboots");
        ShapedRecipe chain_boots = new ShapedRecipe(cbkey, this.getChainmail_boots());
        chain_boots.shape("***", "I*I", "I*I");
        chain_boots.setIngredient('I', Material.IRON_INGOT);
        chain_boots.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_boots);

        NamespacedKey cbkey2 = new NamespacedKey(plugin, "FTSchainboots2");
        ShapedRecipe chain_boots2 = new ShapedRecipe(cbkey2, this.getChainmail_boots());
        chain_boots2.shape("I*I", "I*I", "***");
        chain_boots2.setIngredient('I', Material.IRON_INGOT);
        chain_boots2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(chain_boots2);

        //Diamond Helmet

        NamespacedKey dhkey = new NamespacedKey(plugin, "FTSdiamondhelmet");
        ShapedRecipe diamond_helmet = new ShapedRecipe(dhkey, this.getDiamond_helmet());
        diamond_helmet.shape("DDD", "DBD", "***");
        diamond_helmet.setIngredient('D', Material.DIAMOND);
        diamond_helmet.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_helmet.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_helmet);

        NamespacedKey dhkey2 = new NamespacedKey(plugin, "FTSdiamondhelmet2");
        ShapedRecipe diamond_helmet2 = new ShapedRecipe(dhkey2, this.getDiamond_helmet());
        diamond_helmet2.shape("***", "DDD", "DBD");
        diamond_helmet2.setIngredient('D', Material.DIAMOND);
        diamond_helmet2.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_helmet2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_helmet2);

        //Diamond Chestplate

        NamespacedKey dckey = new NamespacedKey(plugin, "FTSdiamondchest");
        ShapedRecipe diamond_chest = new ShapedRecipe(dckey, this.getDiamond_chestplate());
        diamond_chest.shape("D*D", "DBD", "DDD");
        diamond_chest.setIngredient('D', Material.DIAMOND);
        diamond_chest.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_chest.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_chest);

        //Diamond Leggings

        NamespacedKey dlkey = new NamespacedKey(plugin, "FTSdiamondlegs");
        ShapedRecipe diamond_leggings = new ShapedRecipe(dlkey, this.getDiamond_leggings());
        diamond_leggings.shape("DDD", "DBD", "D*D");
        diamond_leggings.setIngredient('D', Material.DIAMOND);
        diamond_leggings.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_leggings.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_leggings);

        //Diamond Boots

        NamespacedKey dbkey = new NamespacedKey(plugin, "FTSdiamondboots");
        ShapedRecipe diamond_boots = new ShapedRecipe(dbkey, this.getDiamond_boots());
        diamond_boots.shape("***", "DBD", "D*D");
        diamond_boots.setIngredient('D', Material.DIAMOND);
        diamond_boots.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_boots.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_boots);

        NamespacedKey dbkey2 = new NamespacedKey(plugin, "FTSdiamondboots2");
        ShapedRecipe diamond_boots2 = new ShapedRecipe(dbkey2, this.getDiamond_boots());
        diamond_boots2.shape("***", "DBD", "D*D");
        diamond_boots2.setIngredient('D', Material.DIAMOND);
        diamond_boots2.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_boots2.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(diamond_boots2);

        //Horse Leather Armor

        NamespacedKey lhakey = new NamespacedKey(plugin, "FTSleatherhorse");
        ShapedRecipe leatherHorseArmor = new ShapedRecipe(lhakey, this.getLeather_horse());
        leatherHorseArmor.shape("LLL", "LSL", "LLL");
        leatherHorseArmor.setIngredient('L', Material.LEATHER);
        leatherHorseArmor.setIngredient('S', Material.SADDLE);
        plugin.getServer().addRecipe(leatherHorseArmor);

        //Horse Iron Armor

        NamespacedKey ihakey = new NamespacedKey(plugin, "FTSironhorse");
        ShapedRecipe ironHorseArmor = new ShapedRecipe(ihakey, this.getIron_horse());
        ironHorseArmor.shape("III", "ISI", "III");
        ironHorseArmor.setIngredient('I', Material.IRON_INGOT);
        ironHorseArmor.setIngredient('S', Material.SADDLE);
        plugin.getServer().addRecipe(ironHorseArmor);

        //Horse  Diamond Armor

        NamespacedKey dhakey = new NamespacedKey(plugin, "FTSdiamondhorse");
        ShapedRecipe diamondHorseArmor = new ShapedRecipe(dhakey, this.getDiamond_horse());
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

        //Quarz

        NamespacedKey quartzkey = new NamespacedKey(plugin, "FTSquartz");
        ShapedRecipe quartzRecipie = new ShapedRecipe(quartzkey, new ItemStack(Material.QUARTZ, 2));
        quartzRecipie.shape("GGG", "GLG", "GGG");
        quartzRecipie.setIngredient('G', Material.POLISHED_DIORITE);
        quartzRecipie.setIngredient('L', Material.LAPIS_LAZULI);
        plugin.getServer().addRecipe(quartzRecipie);

        //Copperpickaxe
        NamespacedKey cpickkey = new NamespacedKey(plugin, "FTSkupferpicke");
        ItemStack cpick = new ItemStack(Material.STONE_PICKAXE, 1);
        ItemMeta cpickM = cpick.getItemMeta();
        cpickM.setDisplayName("§fKupferspitzhacke");
        cpick.setItemMeta(cpickM);
        cpick.addEnchantment(Enchantment.DURABILITY, 2);
        cpick.addEnchantment(Enchantment.DIG_SPEED, 2);
        cpick.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ShapedRecipe cpickr = new ShapedRecipe(cpickkey, cpick);
        cpickr.shape("CCC", "*S*", "*S*");
        cpickr.setIngredient('C', Material.COPPER_INGOT);
        cpickr.setIngredient('S', Material.STICK);
        cpickr.setIngredient('*', Material.AIR);
        plugin.getServer().addRecipe(cpickr);

        //Copperaxe
        NamespacedKey caxekey = new NamespacedKey(plugin, "FTSkupferaxt");
        ItemStack caxe = new ItemStack(Material.STONE_AXE, 1);
        ItemMeta caxeM = cpick.getItemMeta();
        caxeM.setDisplayName("§fKupferaxt");
        caxe.setItemMeta(caxeM);
        caxe.addEnchantment(Enchantment.DURABILITY, 2);
        caxe.addEnchantment(Enchantment.DIG_SPEED, 2);
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
        ItemStack csword = new ItemStack(Material.STONE_SWORD, 1);
        ItemMeta cswordM = csword.getItemMeta();
        cswordM.setDisplayName("§fKupferschwert");
        csword.setItemMeta(cswordM);
        csword.addEnchantment(Enchantment.DURABILITY, 2);
        csword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
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
        ItemStack choe = new ItemStack(Material.STONE_HOE, 1);
        ItemMeta choeM = choe.getItemMeta();
        choeM.setDisplayName("§fKupferhacke");
        choe.setItemMeta(choeM);
        choe.addEnchantment(Enchantment.DURABILITY, 2);
        choe.addEnchantment(Enchantment.DIG_SPEED, 2);
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
        ItemStack cshovel = new ItemStack(Material.STONE_SHOVEL, 1);
        ItemMeta cshovelM = cshovel.getItemMeta();
        cshovelM.setDisplayName("§fKupferschaufel");
        cshovel.setItemMeta(cshovelM);
        cshovel.addEnchantment(Enchantment.DURABILITY, 2);
        cshovel.addEnchantment(Enchantment.DIG_SPEED, 2);
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

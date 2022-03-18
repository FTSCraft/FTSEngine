package de.ftscraft.ftsengine.main;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.commands.*;
import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.listener.*;
import de.ftscraft.ftsengine.utils.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.logging.Logger;

public class Engine extends JavaPlugin implements Listener {

    public HashMap<String, Ausweis> ausweis;
    private HashMap<Player, FTSUser> player;
    private Team team;
    private UUIDFetcher uF;
    private Var var;
    private ItemStacks itemStacks;
    public int highestId;
    public int biggestBpId;
    public int biggestBriefId;
    public int biggestPferdId;
    private ArrayList<Player> reiter;
    public HashMap<Integer, Backpack> backpacks;
    public HashMap<Integer, Brief> briefe;
    public HashMap<Location, Brett> bretter;
    public HashMap<Player, BrettNote> playerBrettNote;
    public HashMap<UUID, Briefkasten> briefkasten;
    private Scoreboard sb;

    private static Economy econ = null;
    private Permission perms = null;
    private Chat chat = null;

    private static final Logger log = Logger.getLogger("Minecraft");

    public List<Material> mats = new ArrayList<Material>();
    private ProtocolManager protocolManager;
    private ShopkeepersPlugin shopkeepersPlugin;

    @Override
    public void onEnable() {
        setupEconomy();
        setupPermissions();
        setupChat();
        init();
        for (Player a : Bukkit.getOnlinePlayers()) {
            FTSUser user = new FTSUser(this, a);
            this.getPlayer().put(a, user);
        }
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    @Override
    public void onDisable() {
        safeAll();
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private void init() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        if (getServer().getPluginManager().isPluginEnabled("Shopkeepers"))
            this.shopkeepersPlugin = ShopkeepersPlugin.getInstance();
        highestId = 0;
        biggestBpId = 0;
        biggestPferdId = 0;
        biggestBriefId = 0;
        playerBrettNote = new HashMap<Player, BrettNote>();
        bretter = new HashMap<Location, Brett>();
        //sitting = new HashMap<>();
        msgs = new Messages();
        backpacks = new HashMap<Integer, Backpack>();
        briefkasten = new HashMap<UUID, Briefkasten>();
        ausweis = new HashMap<String, Ausweis>();
        uF = new UUIDFetcher();
        briefe = new HashMap<Integer, Brief>();
        itemStacks = new ItemStacks();
        reiter = new ArrayList<Player>();
        player = new HashMap<Player, FTSUser>();
        var = new Var(this);


        recipie();
        new CMDausweis(this);
        new CMDwürfel(this);
        new CMDreiten(this);
        //new CMDchannel(this);
        new CMDtaube(this);
        new CMDwinken(this);
        new CMDschlagen(this);
        new CMDfasten(this);
        new CMDohrfeige(this);
        new CMDumarmen(this);
        new CMDremovearmorstand(this);
        new CMDspucken(this);
        new CMDcountdown(this);
        new CMDgehen(this);
        new CMDzauber(this);
        new CMDitem(this);
        new CMDftsengine(this);
        new CMDklopfen(this);
        new CMDbrief(this);
        new CMDkussen(this);
        new UserIO(this);

        new EntityClickListener(this);
        new DamageListener(this);
        new SneakListener(this);
        //new HorseListener(this);
        new PlayerJoinListener(this);
        new SignWriteListener(this);
        new ItemMoveListener(this);
        new BlockBreakListener(this);
        new ItemSwitchListener(this);
        new PlayerInteractListener(this);
        new InventoryClickListener(this);
        new PlayerQuitListener(this);
        new PlayerChatListener(this);
        new VillagerTradeListener(this);
        new ProjectileHitListener(this);
        new PacketReciveListener(this);

        new Runner(this);
        getServer().getPluginManager().registerEvents(this, this);

        //setupScoreboad();

    }

    public Permission getPerms() {
        return perms;
    }

    public Chat getChat() {
        return chat;
    }

    private final HashMap<OfflinePlayer, Long> ausweisCooldown = new HashMap<OfflinePlayer, Long>();

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (mats.contains(e.getClickedBlock().getType())) {
                    if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
                        player.get(e.getPlayer()).setSitting(e.getClickedBlock());
                }
            }
            if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {
                String iName = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                if (iName != null) {
                    if (iName.startsWith("§6Personalausweis")) {
                        if (ausweisCooldown.containsKey(e.getPlayer())) {
                            if (ausweisCooldown.get(e.getPlayer()) > System.currentTimeMillis())
                                return;
                        }
                        ausweisCooldown.put(e.getPlayer(), System.currentTimeMillis() + 1000);
                        String idS = iName.replaceAll(".*#", "");
                        int id;
                        //Bei Fehlern bei Item gucken : Id da?
                        try {
                            id = Integer.valueOf(idS);
                        } catch (NumberFormatException ex) {
                            e.getPlayer().sendMessage("Irgendwas ist falsch! guck mal Konsole " +
                                    "(sag Musc bescheid, dass er halberfan sagen soll: " +
                                    "\"Fehler bei Main - onItemInteract - NumberFormatException\"");
                            return;
                        }

                        for (Ausweis a : ausweis.values()) {
                            if (a.id == id) {
                                var.sendAusweisMsg(e.getPlayer(), a);
                                break;
                            }
                        }

                    }
                }
            }
        } else if (e.getAction() == Action.LEFT_CLICK_AIR) {
            for (Player reiter : reiter) {
                if (e.getPlayer().getPassengers().contains(reiter)) {
                    e.getPlayer().removePassenger(reiter);
                }
            }
        }

    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void safeAll() {
        for (Ausweis a : ausweis.values()) {
            a.save();
        }

        for (Backpack a : backpacks.values()) {
            a.safe();
        }

        new UserIO(this, true);

    }

    public Messages msgs;

    public Ausweis getAusweis(Player player) {
        if (ausweis.containsKey(player.getName())) {
            return ausweis.get(player.getName());
        } else return null;
    }

    public Ausweis getAusweis(String player) {
        if (ausweis.containsKey(player)) {
            return ausweis.get(player);
        } else return null;
    }

    public boolean hasAusweis(Player player) {
        return ausweis.containsKey(player.getName());
    }

    public boolean hasAusweis(String player) {
        return ausweis.containsKey(player);
    }

    public void addAusweis(Ausweis a) {
        String name = uF.getName(UUID.fromString(a.getUUID()));
        ausweis.put(name, a);
    }


    public Economy getEcon() {
        return econ;
    }

    public Var getVar() {
        return var;
    }

    public Team getTeam() {
        return team;
    }

    public ArrayList<Player> getReiter() {
        return reiter;
    }


    private void recipie() {
        NamespacedKey lanzekey = new NamespacedKey(this, "FTSlanze");
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
        getServer().addRecipe(lanze);

        mats.add(Material.DIRT_PATH);

        mats.addAll(var.getCarpets());

        //TINY BACKPACK

        NamespacedKey tbpkey = new NamespacedKey(this, "FTStinybackpack");

        ShapedRecipe tiny_backpack = new ShapedRecipe(tbpkey, itemStacks.getTiny_bp());
        tiny_backpack.shape("HHH", "HCH", "HHH");
        tiny_backpack.setIngredient('H', Material.RABBIT_HIDE);
        tiny_backpack.setIngredient('C', Material.CHEST);
        getServer().addRecipe(tiny_backpack);

        //LARGE BACKPACK

        NamespacedKey lbpkey = new NamespacedKey(this, "FTSlargebackpack");
        ShapedRecipe large_backpack = new ShapedRecipe(lbpkey, itemStacks.getBig_bp());
        large_backpack.shape("LLL", "LRL", "LLL");
        large_backpack.setIngredient('L', Material.LEATHER);
        large_backpack.setIngredient('R', itemStacks.getTiny_bp());
        getServer().addRecipe(large_backpack);

        //GOLD INGOT
        NamespacedKey goldKey = new NamespacedKey(this, "FTSGOLD");
        ShapelessRecipe goldRec = new ShapelessRecipe(goldKey, itemStacks.getGold());
        goldRec.addIngredient(2, Material.RAW_GOLD);
        goldRec.addIngredient(Material.COPPER_INGOT);
        getServer().addRecipe(goldRec);


        //ENDER BACKPACK

        NamespacedKey ebpkey = new NamespacedKey(this, "FTSenderbackpack");
        ShapedRecipe ender_backpack = new ShapedRecipe(ebpkey, itemStacks.getEnder_bp());
        ender_backpack.shape("LLL", "LEL", "LLL");
        ender_backpack.setIngredient('L', Material.LEATHER);
        ender_backpack.setIngredient('E', Material.ENDER_CHEST);
        getServer().addRecipe(ender_backpack);

        //BACKPACK KEY

        NamespacedKey bpkkey = new NamespacedKey(this, "FTSbackpackkey");

        ShapedRecipe backpack_key = new ShapedRecipe(bpkkey, itemStacks.getBp_key());
        backpack_key.shape("I*I", "*F*", "I*I");
        backpack_key.setIngredient('I', Material.IRON_INGOT);
        backpack_key.setIngredient('*', Material.AIR);
        backpack_key.setIngredient('F', Material.FEATHER);
        getServer().addRecipe(backpack_key);

        //Horn

        NamespacedKey hornkey = new NamespacedKey(this, "FTSHorn");

        ShapelessRecipe horn = new ShapelessRecipe(hornkey, itemStacks.getHorn());
        horn.addIngredient(Material.NAUTILUS_SHELL);
        horn.addIngredient(Material.NOTE_BLOCK);
        getServer().addRecipe(horn);

        List<Recipe> backup = new ArrayList<Recipe>();
        Iterator<Recipe> a = getServer().recipeIterator();

        while (a.hasNext()) {
            Recipe r = a.next();

            ItemStack is = r.getResult();

            if (itemStacks.getDisabledDefaultRecipes().contains(is.getType())) {
                a.remove();
            }

        }


        //Iron Helmet

        NamespacedKey ihkey = new NamespacedKey(this, "FTSironhelmet");
        ShapedRecipe iron_helmet = new ShapedRecipe(ihkey, itemStacks.getIron_helmet());
        iron_helmet.shape("III", "IBI", "***");
        iron_helmet.setIngredient('I', Material.IRON_INGOT);
        iron_helmet.setIngredient('*', Material.AIR);
        iron_helmet.setIngredient('B', Material.IRON_BLOCK);
        getServer().addRecipe(iron_helmet);

        NamespacedKey ihkey2 = new NamespacedKey(this, "FTSironhelmet2");
        ShapedRecipe iron_helmet2 = new ShapedRecipe(ihkey2, itemStacks.getIron_helmet());
        iron_helmet2.shape("***", "III", "IBI");
        iron_helmet2.setIngredient('I', Material.IRON_INGOT);
        iron_helmet2.setIngredient('*', Material.AIR);
        iron_helmet2.setIngredient('B', Material.IRON_BLOCK);
        getServer().addRecipe(iron_helmet2);

        //IronChestplate

        NamespacedKey icpkey = new NamespacedKey(this, "FTSironchestplate");
        ShapedRecipe iron_chestplate = new ShapedRecipe(icpkey, itemStacks.getIron_chestplate());
        iron_chestplate.shape("I*I", "IBI", "III");
        iron_chestplate.setIngredient('I', Material.IRON_INGOT);
        iron_chestplate.setIngredient('*', Material.AIR);
        iron_chestplate.setIngredient('B', Material.IRON_BLOCK);
        getServer().addRecipe(iron_chestplate);

        //IronLeggings

        NamespacedKey ilkey = new NamespacedKey(this, "FTSironleggings");
        ShapedRecipe iron_leggings = new ShapedRecipe(ilkey, itemStacks.getIron_leggings());
        iron_leggings.shape("III", "IBI", "I*I");
        iron_leggings.setIngredient('I', Material.IRON_INGOT);
        iron_leggings.setIngredient('*', Material.AIR);
        iron_leggings.setIngredient('B', Material.IRON_BLOCK);
        getServer().addRecipe(iron_leggings);

        //IronBoots

        NamespacedKey ibkey = new NamespacedKey(this, "FTSironboots");
        ShapedRecipe iron_boots = new ShapedRecipe(ibkey, itemStacks.getIron_boots());
        iron_boots.shape("***", "IBI", "I*I");
        iron_boots.setIngredient('I', Material.IRON_INGOT);
        iron_boots.setIngredient('*', Material.AIR);
        iron_boots.setIngredient('B', Material.IRON_BLOCK);
        getServer().addRecipe(iron_boots);

        NamespacedKey ibkey2 = new NamespacedKey(this, "FTSironboots2");
        ShapedRecipe iron_boots2 = new ShapedRecipe(ibkey2, itemStacks.getIron_boots());
        iron_boots2.shape("IBI", "I*I", "***");
        iron_boots2.setIngredient('I', Material.IRON_INGOT);
        iron_boots2.setIngredient('*', Material.AIR);
        iron_boots2.setIngredient('B', Material.IRON_BLOCK);
        getServer().addRecipe(iron_boots2);


        //ChainmailHelmet

        NamespacedKey chkey = new NamespacedKey(this, "FTSchainhelmet");
        ShapedRecipe chain_helmet = new ShapedRecipe(chkey, itemStacks.getChainmail_helmet());
        chain_helmet.shape("III", "I*I", "***");
        chain_helmet.setIngredient('I', Material.IRON_INGOT);
        chain_helmet.setIngredient('*', Material.AIR);
        getServer().addRecipe(chain_helmet);

        NamespacedKey chkey2 = new NamespacedKey(this, "FTSchainhelmet2");
        ShapedRecipe chain_helmet2 = new ShapedRecipe(chkey2, itemStacks.getChainmail_helmet());
        chain_helmet2.shape("***", "III", "I*I");
        chain_helmet2.setIngredient('I', Material.IRON_INGOT);
        chain_helmet2.setIngredient('*', Material.AIR);
        getServer().addRecipe(chain_helmet2);

        //ChainmailChestplate

        NamespacedKey ccpkey = new NamespacedKey(this, "FTSchainmailchestplate");
        ShapedRecipe chainmail_chestplate = new ShapedRecipe(ccpkey, itemStacks.getChainmail_chestplate());
        chainmail_chestplate.shape("I*I", "III", "III");
        chainmail_chestplate.setIngredient('I', Material.IRON_INGOT);
        chainmail_chestplate.setIngredient('*', Material.AIR);
        getServer().addRecipe(chainmail_chestplate);

        //ChainmailLeggings

        NamespacedKey clkey = new NamespacedKey(this, "FTSchainleggings");
        ShapedRecipe chain_leggings = new ShapedRecipe(clkey, itemStacks.getChainmail_leggings());
        chain_leggings.shape("III", "I*I", "I*I");
        chain_leggings.setIngredient('I', Material.IRON_INGOT);
        chain_leggings.setIngredient('*', Material.AIR);
        getServer().addRecipe(chain_leggings);

        //ChainmailBoots

        NamespacedKey cbkey = new NamespacedKey(this, "FTSchainboots");
        ShapedRecipe chain_boots = new ShapedRecipe(cbkey, itemStacks.getChainmail_boots());
        chain_boots.shape("***", "I*I", "I*I");
        chain_boots.setIngredient('I', Material.IRON_INGOT);
        chain_boots.setIngredient('*', Material.AIR);
        getServer().addRecipe(chain_boots);

        NamespacedKey cbkey2 = new NamespacedKey(this, "FTSchainboots2");
        ShapedRecipe chain_boots2 = new ShapedRecipe(cbkey2, itemStacks.getChainmail_boots());
        chain_boots2.shape("I*I", "I*I", "***");
        chain_boots2.setIngredient('I', Material.IRON_INGOT);
        chain_boots2.setIngredient('*', Material.AIR);
        getServer().addRecipe(chain_boots2);

        //Diamond Helmet

        NamespacedKey dhkey = new NamespacedKey(this, "FTSdiamondhelmet");
        ShapedRecipe diamond_helmet = new ShapedRecipe(dhkey, itemStacks.getDiamond_helmet());
        diamond_helmet.shape("DDD", "DBD", "***");
        diamond_helmet.setIngredient('D', Material.DIAMOND);
        diamond_helmet.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_helmet.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_helmet);

        NamespacedKey dhkey2 = new NamespacedKey(this, "FTSdiamondhelmet2");
        ShapedRecipe diamond_helmet2 = new ShapedRecipe(dhkey2, itemStacks.getDiamond_helmet());
        diamond_helmet2.shape("***", "DDD", "DBD");
        diamond_helmet2.setIngredient('D', Material.DIAMOND);
        diamond_helmet2.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_helmet2.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_helmet2);

        //Diamond Chestplate

        NamespacedKey dckey = new NamespacedKey(this, "FTSdiamondchest");
        ShapedRecipe diamond_chest = new ShapedRecipe(dckey, itemStacks.getDiamond_chestplate());
        diamond_chest.shape("D*D", "DBD", "DDD");
        diamond_chest.setIngredient('D', Material.DIAMOND);
        diamond_chest.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_chest.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_chest);

        //Diamond Leggings

        NamespacedKey dlkey = new NamespacedKey(this, "FTSdiamondlegs");
        ShapedRecipe diamond_leggings = new ShapedRecipe(dlkey, itemStacks.getDiamond_leggings());
        diamond_leggings.shape("DDD", "DBD", "D*D");
        diamond_leggings.setIngredient('D', Material.DIAMOND);
        diamond_leggings.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_leggings.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_leggings);

        //Diamond Boots

        NamespacedKey dbkey = new NamespacedKey(this, "FTSdiamondboots");
        ShapedRecipe diamond_boots = new ShapedRecipe(dbkey, itemStacks.getDiamond_boots());
        diamond_boots.shape("***", "DBD", "D*D");
        diamond_boots.setIngredient('D', Material.DIAMOND);
        diamond_boots.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_boots.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_boots);

        NamespacedKey dbkey2 = new NamespacedKey(this, "FTSdiamondboots2");
        ShapedRecipe diamond_boots2 = new ShapedRecipe(dbkey2, itemStacks.getDiamond_boots());
        diamond_boots2.shape("***", "DBD", "D*D");
        diamond_boots2.setIngredient('D', Material.DIAMOND);
        diamond_boots2.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_boots2.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_boots2);

        //Horse Leather Armor

        NamespacedKey lhakey = new NamespacedKey(this, "FTSleatherhorse");
        ShapedRecipe leatherHorseArmor = new ShapedRecipe(lhakey, itemStacks.getLeather_horse());
        leatherHorseArmor.shape("LLL", "LSL", "LLL");
        leatherHorseArmor.setIngredient('L', Material.LEATHER);
        leatherHorseArmor.setIngredient('S', Material.SADDLE);
        getServer().addRecipe(leatherHorseArmor);

        //Horse Iron Armor

        NamespacedKey ihakey = new NamespacedKey(this, "FTSironhorse");
        ShapedRecipe ironHorseArmor = new ShapedRecipe(ihakey, itemStacks.getIron_horse());
        ironHorseArmor.shape("III", "ISI", "III");
        ironHorseArmor.setIngredient('I', Material.IRON_INGOT);
        ironHorseArmor.setIngredient('S', Material.SADDLE);
        getServer().addRecipe(ironHorseArmor);

        //Horse  Diamond Armor

        NamespacedKey dhakey = new NamespacedKey(this, "FTSdiamondhorse");
        ShapedRecipe diamondHorseArmor = new ShapedRecipe(dhakey, itemStacks.getDiamond_horse());
        diamondHorseArmor.shape("DDD", "DSD", "DDD");
        diamondHorseArmor.setIngredient('D', Material.DIAMOND);
        diamondHorseArmor.setIngredient('S', Material.SADDLE);
        getServer().addRecipe(diamondHorseArmor);

        //Diamond Helmet Replacement

        NamespacedKey dhairkey = new NamespacedKey(this, "FTSdiamondhelmetair");
        ShapedRecipe diamond_helmet_air = new ShapedRecipe(dhairkey, itemStacks.getDiamondHelmetReplacement());
        diamond_helmet_air.shape("DDD", "D*D", "***");
        diamond_helmet_air.setIngredient('D', Material.DIAMOND);
        diamond_helmet_air.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_helmet_air);

        //Diamond Chestplate Replacement

        NamespacedKey dcairkey = new NamespacedKey(this, "FTSdiamondchestair");
        ShapedRecipe diamond_chest_air = new ShapedRecipe(dcairkey, itemStacks.getDiamondChestplateReplacement());
        diamond_chest_air.shape("D*D", "DDD", "DDD");
        diamond_chest_air.setIngredient('D', Material.DIAMOND);
        diamond_chest_air.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_chest_air);

        //Diamond Leggings Replacement

        NamespacedKey dlairkey = new NamespacedKey(this, "FTSdiamondlegsair");
        ShapedRecipe diamond_leggings_air = new ShapedRecipe(dlairkey, itemStacks.getDiamondLeggingsReplacement());
        diamond_leggings_air.shape("DDD", "D*D", "D*D");
        diamond_leggings_air.setIngredient('D', Material.DIAMOND);
        diamond_leggings_air.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_leggings_air);

        //Diamond Boots Replacement

        NamespacedKey dbairkey = new NamespacedKey(this, "FTSdiamondbootsair");
        ShapedRecipe diamond_boots_air = new ShapedRecipe(dbairkey, itemStacks.getDiamondBootsReplacement());
        diamond_boots_air.shape("***", "D*D", "D*D");
        diamond_boots_air.setIngredient('D', Material.DIAMOND);
        diamond_boots_air.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_boots_air);

        //Diamond Boots Replacement 2

        NamespacedKey dbairkey2 = new NamespacedKey(this, "FTSdiamondbootsair2");
        ShapedRecipe diamond_boots_air2 = new ShapedRecipe(dbairkey2, itemStacks.getDiamondBootsReplacement());
        diamond_boots_air2.shape("D*D", "D*D", "***");
        diamond_boots_air2.setIngredient('D', Material.DIAMOND);
        diamond_boots_air2.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_boots_air2);

        //Quarz

        NamespacedKey quartzkey = new NamespacedKey(this, "FTSquartz");
        ShapedRecipe quartzRecipie = new ShapedRecipe(quartzkey, new ItemStack(Material.QUARTZ, 2));
        quartzRecipie.shape("GGG", "GLG", "GGG");
        quartzRecipie.setIngredient('G', Material.POLISHED_DIORITE);
        quartzRecipie.setIngredient('L', Material.LAPIS_LAZULI);
        getServer().addRecipe(quartzRecipie);

        //Copperpickaxe
        NamespacedKey cpickkey = new NamespacedKey(this, "FTSkupferpicke");
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
        getServer().addRecipe(cpickr);

        //Copperaxe
        NamespacedKey caxekey = new NamespacedKey(this, "FTSkupferaxt");
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
        getServer().addRecipe(caxer);

        ShapedRecipe caxel = new ShapedRecipe(new NamespacedKey(this, "FTSkupferaxt2"), caxe);
        caxel.shape("CC*", "CS*", "*S*");
        caxel.setIngredient('C', Material.COPPER_INGOT);
        caxel.setIngredient('S', Material.STICK);
        caxel.setIngredient('*', Material.AIR);
        getServer().addRecipe(caxel);

        ShapedRecipe caxell = new ShapedRecipe(new NamespacedKey(this, "FTSkupferaxt3"), caxe);
        caxell.shape("CC*", "SC*", "S**");
        caxell.setIngredient('C', Material.COPPER_INGOT);
        caxell.setIngredient('S', Material.STICK);
        caxell.setIngredient('*', Material.AIR);
        getServer().addRecipe(caxell);

        ShapedRecipe caxerr = new ShapedRecipe(new NamespacedKey(this, "FTSkupferaxt4"), caxe);
        caxerr.shape("*CC", "*CS", "**S");
        caxerr.setIngredient('C', Material.COPPER_INGOT);
        caxerr.setIngredient('S', Material.STICK);
        caxerr.setIngredient('*', Material.AIR);
        getServer().addRecipe(caxerr);

        //Coppersword
        NamespacedKey cswordkey = new NamespacedKey(this, "FTSkupferschwert");
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
        getServer().addRecipe(cswordl);

        ShapedRecipe cswordr = new ShapedRecipe(new NamespacedKey(this, "FTSkupferschwert2"), csword);
        cswordr.shape("**C", "**C", "**S");
        cswordr.setIngredient('C', Material.COPPER_INGOT);
        cswordr.setIngredient('S', Material.STICK);
        cswordr.setIngredient('*', Material.AIR);
        getServer().addRecipe(cswordr);

        ShapedRecipe cswordm = new ShapedRecipe(new NamespacedKey(this, "FTSkupferschwert3"), csword);
        cswordm.shape("*C*", "*C*", "*S*");
        cswordm.setIngredient('C', Material.COPPER_INGOT);
        cswordm.setIngredient('S', Material.STICK);
        cswordm.setIngredient('*', Material.AIR);
        getServer().addRecipe(cswordm);

        //Copperhoe
        NamespacedKey choekey = new NamespacedKey(this, "FTSkupferhacke");
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
        getServer().addRecipe(choel);

        ShapedRecipe choer = new ShapedRecipe(new NamespacedKey(this, "FTSkupferhacke2"), choe);
        choer.shape("*CC", "*S*", "*s*");
        choer.setIngredient('C', Material.COPPER_INGOT);
        choer.setIngredient('S', Material.STICK);
        choer.setIngredient('*', Material.AIR);
        getServer().addRecipe(choer);

        ShapedRecipe choerr = new ShapedRecipe(new NamespacedKey(this, "FTSkupferhacke3"), choe);
        choerr.shape("*CC", "**S", "**S");
        choerr.setIngredient('C', Material.COPPER_INGOT);
        choerr.setIngredient('S', Material.STICK);
        choerr.setIngredient('*', Material.AIR);
        getServer().addRecipe(choerr);

        ShapedRecipe choell = new ShapedRecipe(new NamespacedKey(this, "FTSkupferhacke4"), choe);
        choell.shape("CC*", "S**", "S**");
        choell.setIngredient('C', Material.COPPER_INGOT);
        choell.setIngredient('S', Material.STICK);
        choell.setIngredient('*', Material.AIR);
        getServer().addRecipe(choell);

        //Coppershovel
        NamespacedKey cshovelkey = new NamespacedKey(this, "FTSkupferschaufel");
        ItemStack cshovel = new ItemStack(Material.STONE_SHOVEL, 1);
        ItemMeta cshovelM = cshovel.getItemMeta();
        cshovelM.setDisplayName("§fKupferschaufel");
        cshovel.setItemMeta(cshovelM);
        cshovel.addEnchantment(Enchantment.DURABILITY, 2);
        cshovel.addEnchantment(Enchantment.DIG_SPEED, 2);
        cshovel.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        ShapedRecipe cshovelr = new ShapedRecipe(new NamespacedKey(this, "FTSkupferschaufel2"), cshovel);
        cshovelr.shape("**C", "**S", "**S");
        cshovelr.setIngredient('C', Material.COPPER_INGOT);
        cshovelr.setIngredient('S', Material.STICK);
        cshovelr.setIngredient('*', Material.AIR);
        getServer().addRecipe(cshovelr);

        ShapedRecipe cshovelm = new ShapedRecipe(new NamespacedKey(this, "FTSkupferschaufel3"), cshovel);
        cshovelm.shape("*C*", "*S*", "*S*");
        cshovelm.setIngredient('C', Material.COPPER_INGOT);
        cshovelm.setIngredient('S', Material.STICK);
        cshovelm.setIngredient('*', Material.AIR);
        getServer().addRecipe(cshovelm);
    }

    public HashMap<Player, FTSUser> getPlayer() {
        return player;
    }

    public HashMap<Integer, Backpack> getBackpacks() {
        return backpacks;
    }

    public void sendTablistHeaderAndFooter(Player p, String header, String footer) {
        PacketContainer pc = protocolManager.createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        pc.getChatComponents().write(0, WrappedChatComponent.fromText(header)).write(1, WrappedChatComponent.fromText(footer));

        try {
            protocolManager.sendServerPacket(p, pc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public ShopkeepersPlugin getShopkeepersPlugin() {
        return shopkeepersPlugin;
    }
}

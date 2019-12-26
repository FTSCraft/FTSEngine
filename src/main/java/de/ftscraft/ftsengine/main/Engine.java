package de.ftscraft.ftsengine.main;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.commands.*;
import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.listener.*;
import de.ftscraft.ftsengine.utils.*;
import me.lucko.luckperms.api.LuckPermsApi;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.List;
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
    private Scoreboard sb;

    private static Economy econ = null;
    private LuckPermsApi lpapi = null;
    private Permission perms = null;
    private Chat chat = null;

    private static final Logger log = Logger.getLogger("Minecraft");

    public List<Material> mats = new ArrayList<>();
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider != null) {
            lpapi = provider.getProvider();
        }
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
        highestId = 0;
        biggestBpId = 0;
        biggestPferdId = 0;
        biggestBriefId = 0;
        playerBrettNote = new HashMap<>();
        bretter = new HashMap<>();
        //sitting = new HashMap<>();
        msgs = new Messages();
        backpacks = new HashMap<>();
        ausweis = new HashMap<>();
        uF = new UUIDFetcher();
        briefe = new HashMap<>();
        itemStacks = new ItemStacks();
        reiter = new ArrayList<>();
        player = new HashMap<>();
        var = new Var(this);


        recipie();
        new CMDausweis(this);
        new CMDwürfel(this);
        new CMDreiten(this);
        //new CMDchannel(this);
        new CMDtaube(this);
        new CMDschlagen(this);
        new CMDfasten(this);
        new CMDremovearmorstand(this);
        new CMDcountdown(this);
        new CMDgehen(this);
        new CMDitem(this);
        new CMDklopfen(this);
        new CMDbrief(this);
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

        new Runner(this);
        getServer().getPluginManager().registerEvents(this, this);

        setupScoreboad();

    }

    public Permission getPerms() {
        return perms;
    }

    public Chat getChat() {
        return chat;
    }

    private void setupScoreboad() {
        sb = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Team t : sb.getTeams()) {
            t.unregister();
        }
        if (sb.getTeam("roleplay_modus") == null)
            team = sb.registerNewTeam("roleplay_modus");
        else team = sb.getTeam("roleplay_modus");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        try {
            for (int i = 0; i < TeamPrefixs.values().length; i++) {
                String name = i + TeamPrefixs.values()[i].getTeamName();
                sb.registerNewTeam(name);
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }

    public void setPrefix(Player p) {
        String team = null;
        for (int i = 0; i < TeamPrefixs.values().length; i++) {
            TeamPrefixs t = TeamPrefixs.values()[i];
            if (p.hasPermission(t.getPermission())) {
                team = i + t.getTeamName();
            }
            if (t == TeamPrefixs.REISENDER)
                team = i + t.getTeamName();
            if (team != null)
                break;
        }
        Team t = sb.getTeam(team);
        t.addPlayer(p);
        //sendTablistHeaderAndFooter(p, "§6§lplay.ftscraft.de", "");
    }

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
                        String idS = iName.replaceAll(".*#", "");
                        int id;
                        //Bei Fehlern bei Item gucken : Id da?
                        try {
                            id = Integer.valueOf(idS);
                        } catch (NumberFormatException ex) {
                            e.getPlayer().sendMessage("Irgendwas ist falsch! guck mal Konsole " +
                                    "(sag Musc gescheid, dass er halberfan sagen soll: " +
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
            a.safe();
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

        /*mats.addAll(Arrays.asList(Material.ACACIA_STAIRS, Material.BRICK_STAIRS, Material.BIRCH_STAIRS, Material.COBBLESTONE_STAIRS,
                Material.DARK_OAK_STAIRS, Material.JUNGLE_STAIRS, Material.NETHER_BRICK_STAIRS, Material.PURPUR_STAIRS, Material.QUARTZ_STAIRS,
                Material.QUARTZ_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.SANDSTONE_STAIRS, Material.SPRUCE_STAIRS, Material.MOSSY_STONE_BRICKS,
                Material.OAK_STAIRS, Material.DARK_PRISMARINE_STAIRS, Material.PRISMARINE_STAIRS, Material.PRISMARINE_BRICK_STAIRS,
                Material.END_STONE_BRICK_STAIRS,
                Material.STONE_BRICK_STAIRS));*/
        mats.add(Material.GRASS_PATH);
        mats.addAll(var.getCarpets());

        //TINY BACKPACK

        NamespacedKey tbpkey = new NamespacedKey(this, "FTStinybackpack");

        ShapedRecipe tiny_backpack = new ShapedRecipe(tbpkey, itemStacks.getTiny_bp());
        tiny_backpack.shape("LLL", "L*L", "LLL");
        tiny_backpack.setIngredient('L', Material.LEATHER);
        tiny_backpack.setIngredient('*', Material.AIR);
        getServer().addRecipe(tiny_backpack);

        //LARGE BACKPACK

        NamespacedKey lbpkey = new NamespacedKey(this, "FTSlargebackpack");
        ShapedRecipe large_backpack = new ShapedRecipe(lbpkey, itemStacks.getBig_bp());
        large_backpack.shape("LLL", "LCL", "LLL");
        large_backpack.setIngredient('L', Material.LEATHER);
        large_backpack.setIngredient('C', Material.CHEST);
        getServer().addRecipe(large_backpack);

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

        List<Recipe> backup = new ArrayList<>();
        Iterator<Recipe> a = getServer().recipeIterator();

        while (a.hasNext()) {
            Recipe r = a.next();

            ItemStack is = r.getResult();

            if (itemStacks.getDisabledDefaultRecipes().contains(is.getType())) {
                a.remove();
            }

        }


        /*getServer().clearRecipes();
        System.out.println("----");
        for (Recipe recipe : backup) {
            getServer().addRecipe(recipe);
            System.out.println(recipe.getResult());
        }*/

        //Iron Helmet

        NamespacedKey ihkey = new NamespacedKey(this, "FTSironhelmet");
        ShapedRecipe iron_helmet = new ShapedRecipe(ihkey, itemStacks.getIron_helmet());
        iron_helmet.shape("III", "IBI", "***");
        iron_helmet.setIngredient('I', Material.IRON_INGOT);
        iron_helmet.setIngredient('*', Material.AIR);
        iron_helmet.setIngredient('B', Material.IRON_BLOCK);
        getServer().addRecipe(iron_helmet);

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

        //ChainmailHelmet

        NamespacedKey chkey = new NamespacedKey(this, "FTSchainhelmet");
        ShapedRecipe chain_helmet = new ShapedRecipe(chkey, itemStacks.getChainmail_helmet());
        chain_helmet.shape("III", "I*I", "***");
        chain_helmet.setIngredient('I', Material.IRON_INGOT);
        chain_helmet.setIngredient('*', Material.AIR);
        getServer().addRecipe(chain_helmet);

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

        //Diamond Helmet

        NamespacedKey dhkey = new NamespacedKey(this, "FTSdiamondhelmet");
        ShapedRecipe diamond_helmet = new ShapedRecipe(dhkey, itemStacks.getDiamond_helmet());
        diamond_helmet.shape("DDD", "DBD", "***");
        diamond_helmet.setIngredient('D', Material.DIAMOND);
        diamond_helmet.setIngredient('B', Material.DIAMOND_BLOCK);
        diamond_helmet.setIngredient('*', Material.AIR);
        getServer().addRecipe(diamond_helmet);

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

    public LuckPermsApi getLpapi() {
        return lpapi;
    }


}

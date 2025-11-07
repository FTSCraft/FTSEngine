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
import de.ftscraft.ftsengine.commands.emotes.*;
import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.feature.time.TimeManager;
import de.ftscraft.ftsengine.feature.weather.WeatherManager;
import de.ftscraft.ftsengine.listener.*;
import de.ftscraft.ftsengine.logport.LogportManager;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.ConfigManager;
import de.ftscraft.ftsengine.utils.ItemStacks;
import de.ftscraft.ftsengine.utils.UserIO;
import de.ftscraft.ftsutils.services.IChatInputService;
import de.ftscraft.ftsutils.storage.DataHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Engine extends JavaPlugin implements Listener {

    private static Engine instance;

    private ConfigManager configManager;

    public HashMap<UUID, Ausweis> ausweis;
    private HashMap<Player, FTSUser> player;
    public int biggestBpId;
    public int biggestBriefId;
    private ArrayList<Player> reiter;
    public HashMap<Integer, Backpack> backpacks;
    public HashMap<Integer, Brief> briefe;
    public HashMap<Location, Brett> bretter;
    public HashMap<Player, BrettNote> playerBrettNote;
    public HashMap<UUID, Briefkasten> briefkasten;
    public LogportManager logportManager;
    private EntityClickListener entityClickListener;
    private CMDstreicheln streicheln;

    private static Economy econ = null;

    public List<Material> mats = new ArrayList<>();
    private ProtocolManager protocolManager = null;
    private ShopkeepersPlugin shopkeepersPlugin = null;
    private DataHandler storage;
    private IChatInputService chatInputService;

    @Override
    public void onEnable() {
        instance = this;
        chatInputService = Bukkit.getServicesManager().load(IChatInputService.class);
        storage = DataHandler.forPlugin(this);
        configManager = new ConfigManager();
        WeatherManager.init();
        setupEconomy();
        init();
        for (Player a : Bukkit.getOnlinePlayers()) {
            FTSUser user = new FTSUser(this, a);
            this.getPlayer().put(a, user);

            if (getProtocolManager() != null)
                sendTablistHeaderAndFooter(a, " §cHeutiger Tipp: \nGeht voten!", "");

            //Map
            a.getInventory().getItemInMainHand();
            if (a.getInventory().getItemInMainHand().getType() == Material.FILLED_MAP) {
                ItemStack itemMap = a.getInventory().getItemInMainHand();
                Brief brief = briefe.get((int) itemMap.getDurability());
                if (brief != null) {
                    brief.loadMap(itemMap);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        saveAll();
        logportManager.onDisableLogic();
        storage.saveStorages();
    }

    private void init() {
        if (getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
            this.protocolManager = ProtocolLibrary.getProtocolManager();
        if (getServer().getPluginManager().isPluginEnabled("Shopkeepers"))
            this.shopkeepersPlugin = ShopkeepersPlugin.getInstance();
        logportManager = new LogportManager(this);
        biggestBpId = 0;
        biggestBriefId = 0;
        playerBrettNote = new HashMap<>();
        bretter = new HashMap<>();
        backpacks = new HashMap<>();
        briefkasten = new HashMap<>();
        ausweis = new HashMap<>();
        briefe = new HashMap<>();
        new ItemStacks(this);
        reiter = new ArrayList<>();
        player = new HashMap<>();

        initCommands();
        new UserIO(this);

        initListeners();

        TimeManager.init();

    }

    private void initCommands() {
        new CMDausweis(this);

        if (getProtocolManager() != null)
            new CMDreiten(this);

        new CMDtaube(this);
        new CMDwinken(this);
        new CMDschlagen(this);
        new CMDfasten(this);
        new CMDohrfeige(this);
        new CMDplaytime(this);
        new CMDumarmen(this);
        new CMDremovearmorstand(this);
        new CMDspucken(this);
        new CMDcountdown(this);
        new CMDgehen(this);
        new CMDitem(this);
        new CMDbuch(this);
        new CMDftsengine(this);
        new CMDklopfen(this);
        new CMDbrief(this);
        new CMDkussen(this);
        new CMDewuerfel(this);
        new CMDzeit(this);
        new CMDdurchsuchen(this);
        new CMDsearchreact(this);
        new CMDlehrtafel(this);
        streicheln = new CMDstreicheln(this);
    }

    private void initListeners() {
        new AnvilEntchamentBlockingListener(this);
        entityClickListener = new EntityClickListener(this);
        new DamageListener(this);
        //new HorseListener(this);
        new PlayerJoinListener(this);
        new SignWriteListener(this);
        new BlockBreakListener(this);
        new BlockPlaceListener(this);
        new ItemSwitchListener(this);
        new PlayerInteractListener(this);
        new InventoryClickListener(this);
        new PlayerQuitListener(this);
        new PlayerChatListener(this);
        new VillagerTradeListener(this);
        new PlayerDropListener(this);
        new ProjectileHitListener();
        new BlockExplodeListener();
        new EntityDeathListener();
        new ItemBreakListener(this);
        new PlayerMountListener(this);
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    private void saveAll() {
        configManager.save();
        for (Ausweis a : ausweis.values()) {
            a.save();
        }

        for (Backpack a : backpacks.values()) {
            a.safe();
        }

        new UserIO(this, true);

    }

    public void sendTablistHeaderAndFooter(Player p, String header, String footer) {
        PacketContainer pc = getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);

        pc.getChatComponents().write(0, WrappedChatComponent.fromText(header)).write(1, WrappedChatComponent.fromText(footer));

        try {
            getProtocolManager().sendServerPacket(p, pc);
        } catch (Exception ex) {
            getLogger().log(Level.WARNING, "Was not able to send header and footer package to player.");
        }
    }

    public DataHandler getStorage() {
        return storage;
    }

    public IChatInputService getChatInputService() {
        return chatInputService;
    }

    public Ausweis getAusweis(Player player) {
        return ausweis.getOrDefault(player.getUniqueId(), null);
    }

    public Ausweis getAusweis(UUID player) {
        return ausweis.getOrDefault(player, null);
    }

    public boolean hasAusweis(Player player) {
        return ausweis.containsKey(player.getUniqueId());
    }

    public boolean hasAusweis(UUID player) {
        return ausweis.containsKey(player);
    }

    public void addAusweis(Ausweis a) {
        ausweis.put(a.getUuid(), a);
    }

    public Economy getEcon() {
        return econ;
    }

    public ArrayList<Player> getReiter() {
        return reiter;
    }

    public HashMap<Player, FTSUser> getPlayer() {
        return player;
    }

    public HashMap<Integer, Backpack> getBackpacks() {
        return backpacks;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public ShopkeepersPlugin getShopkeepersPlugin() {
        return shopkeepersPlugin;
    }

    public LogportManager getLogportManager() {
        return logportManager;
    }

    public EntityClickListener getEntityClickListener() {
        return entityClickListener;
    }

    public CMDstreicheln getStreicheln() {
        return streicheln;
    }

    public static Engine getInstance() {
        return instance;
    }

    public static ConfigManager getConfigManager() {
        return instance.configManager;
    }

}

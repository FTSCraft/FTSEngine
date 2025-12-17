package de.ftscraft.ftsengine.main;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import de.ftscraft.ftsengine.commands.*;
import de.ftscraft.ftsengine.commands.ausweis.CMDausweis;
import de.ftscraft.ftsengine.commands.emotes.*;
import de.ftscraft.ftsengine.feature.FeatureHandler;
import de.ftscraft.ftsengine.feature.brett.Brett;
import de.ftscraft.ftsengine.feature.brett.BrettNote;
import de.ftscraft.ftsengine.feature.courier.Brief;
import de.ftscraft.ftsengine.feature.courier.Briefkasten;
import de.ftscraft.ftsengine.feature.items.backpacks.Backpack;
import de.ftscraft.ftsengine.feature.items.logport.LogportManager;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisManager;
import de.ftscraft.ftsengine.feature.texturepack.catalog.command.CatalogCommand;
import de.ftscraft.ftsengine.feature.time.TimeManager;
import de.ftscraft.ftsengine.feature.weather.WeatherManager;
import de.ftscraft.ftsengine.listener.*;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.EngineConfig;
import de.ftscraft.ftsengine.utils.ItemStacks;
import de.ftscraft.ftsengine.utils.UserIO;
import de.ftscraft.ftsengine.utils.storage.EngineDataHandler;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class Engine extends JavaPlugin implements Listener {

    private static Engine instance;

    private EngineConfig engineConfig;
    private FeatureHandler featureHandler;
    private AusweisManager ausweisManager;

    private HashMap<UUID, EngineUser> player;
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

    private ProtocolManager protocolManager = null;
    private ShopkeepersPlugin shopkeepersPlugin = null;
    private DataHandler storage;
    private EngineDataHandler databaseHandler;
    private IChatInputService chatInputService;

    @Override
    public void onEnable() {
        instance = this;

        // Config laden (wichtig für Datenbankverbindung)
        saveDefaultConfig();

        // Storage und EngineConfig initialisieren
        chatInputService = Bukkit.getServicesManager().load(IChatInputService.class);
        storage = DataHandler.forPlugin(this);
        storage.registerClass(EngineConfig.class);
        storage.loadStorages(EngineConfig.class);
        engineConfig = storage.get(EngineConfig.class);

        // Database-Handler mit EngineConfig initialisieren
        databaseHandler = new EngineDataHandler(this, engineConfig);
        databaseHandler.initialize();

        WeatherManager.init();
        featureHandler = new FeatureHandler(this);
        setupEconomy();
        init();
        for (Player a : Bukkit.getOnlinePlayers()) {
            EngineUser user = databaseHandler.getUserStorageManager().getOrCreateUser(a.getUniqueId());
            this.getPlayer().put(a.getUniqueId(), user);

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

        // Database-Handler herunterfahren
        if (databaseHandler != null) {
            databaseHandler.shutdown();
        }

        storage.saveStorages();
    }

    private void init() {
        if (getServer().getPluginManager().isPluginEnabled("ProtocolLib"))
            this.protocolManager = ProtocolLibrary.getProtocolManager();
        if (getServer().getPluginManager().isPluginEnabled("Shopkeepers"))
            this.shopkeepersPlugin = ShopkeepersPlugin.getInstance();
        ausweisManager = new de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisManager(this);

        logportManager = new LogportManager(this);
        biggestBpId = 0;
        biggestBriefId = 0;
        playerBrettNote = new HashMap<>();
        bretter = new HashMap<>();
        backpacks = new HashMap<>();
        briefkasten = new HashMap<>();
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
        new CatalogCommand();
        streicheln = new CMDstreicheln(this);
    }

    private void initListeners() {
        new AnvilEntchamentBlockingListener(this);
        entityClickListener = new EntityClickListener(this);
        new DamageListener(this);
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
        // Kalender speichern
        Calendar calendar = TimeManager.getCalendar();
        engineConfig.calendar.year = calendar.get(Calendar.YEAR);
        engineConfig.calendar.month = calendar.get(Calendar.MONTH);
        engineConfig.calendar.day = calendar.get(Calendar.DAY_OF_MONTH);
        engineConfig.calendar.hour = calendar.get(Calendar.HOUR_OF_DAY);
        engineConfig.calendar.minute = calendar.get(Calendar.MINUTE);

        storage.saveStorages();

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
        return ausweisManager.getAusweis(player);
    }

    public Ausweis getAusweis(UUID uuid) {
        return ausweisManager.getAusweis(uuid);
    }

    public boolean hasAusweis(Player player) {
        return ausweisManager.hasAusweis(player);
    }

    public boolean hasAusweis(UUID player) {
        return ausweisManager.hasAusweis(player);
    }

    public void addAusweis(Ausweis a) {
        ausweisManager.addAusweis(a);
    }

    public void saveAusweis(Ausweis a) {
        ausweisManager.saveAusweis(a);
    }

    public Economy getEcon() {
        return econ;
    }

    public ArrayList<Player> getReiter() {
        return reiter;
    }

    public HashMap<UUID, EngineUser> getPlayer() {
        return player;
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

    public static Engine getInstance() {
        return instance;
    }

    public static FeatureHandler getFeatureHandler() {
        return getInstance().featureHandler;
    }

    public static EngineConfig getEngineConfig() {
        return instance.engineConfig;
    }

    public EngineDataHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public AusweisManager getAusweisManager() {
        return ausweisManager;
    }

}


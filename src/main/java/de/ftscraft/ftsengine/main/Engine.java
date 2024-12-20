package de.ftscraft.ftsengine.main;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.nisovin.shopkeepers.api.ShopkeepersPlugin;
import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.commands.*;
import de.ftscraft.ftsengine.commands.emotes.*;
import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.listener.*;
import de.ftscraft.ftsengine.logport.LogportManager;
import de.ftscraft.ftsengine.feature.time.TimeManager;
import de.ftscraft.ftsengine.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

    private static Economy econ = null;

    public List<Material> mats = new ArrayList<>();
    private ProtocolManager protocolManager = null;
    private ShopkeepersPlugin shopkeepersPlugin = null;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        setupEconomy();
        init();
        for (Player a : Bukkit.getOnlinePlayers()) {
            FTSUser user = new FTSUser(this, a);
            this.getPlayer().put(a, user);
        }
    }

    @Override
    public void onDisable() {
        saveAll();
        logportManager.onDisableLogic();
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
        new CMDzauber(this);
        new CMDitem(this);
        new CMDbuch(this);
        new CMDftsengine(this);
        new CMDklopfen(this);
        new CMDbrief(this);
        new CMDkussen(this);
        new CMDewuerfel(this);
        new CMDzeit(this);
    }
    private void initListeners() {
        new AnvilEntchamentBlockingListener(this);
        new EntityClickListener(this);
        new DamageListener(this);
        //new HorseListener(this);
        new PlayerJoinListener(this);
        new SignWriteListener(this);
        new BlockBreakListener(this);
        new ItemSwitchListener(this);
        new PlayerInteractListener(this);
        new InventoryClickListener(this);
        new PlayerQuitListener(this);
        new PlayerChatListener(this);
        new VillagerTradeListener(this);
        new ProjectileHitListener();
        new BlockExplodeListener();
        new EntityDeathListener();
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

    public LogportManager getLogportManager() {return logportManager;}

    public static Engine getInstance() {
        return instance;
    }

    public static ConfigManager getConfigManager() {
        return instance.configManager;
    }

}

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
import de.ftscraft.ftsengine.listener.*;
import de.ftscraft.ftsengine.utils.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
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
    private HashMap<Integer, Race> races;
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
        itemStacks = new ItemStacks(this);
        reiter = new ArrayList<Player>();
        player = new HashMap<Player, FTSUser>();
        var = new Var(this);


        new CMDausweis(this);
        new CMDwürfel(this);
        new CMDreiten(this);
        //new CMDchannel(this);
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
        new CMDewürfel(this);
        new UserIO(this);


        new AnvilEntchamentBlockingListener(this);
        new EntityClickListener(this);
        new DamageListener(this);
        new SneakListener(this);
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

    public final HashMap<OfflinePlayer, Long> ausweisCooldown = new HashMap<OfflinePlayer, Long>();

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

    public HashMap<Integer, Race> getRaces() {
        return races;
    }
}

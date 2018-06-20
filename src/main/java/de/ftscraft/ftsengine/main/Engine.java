package de.ftscraft.ftsengine.main;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.chat.ChatChannels;
import de.ftscraft.ftsengine.commands.*;
import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.courier.BriefLieferung;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.listener.*;
import de.ftscraft.ftsengine.pferd.Pferd;
import de.ftscraft.ftsengine.reisepunkt.Reisepunkt;
import de.ftscraft.ftsengine.utils.*;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.permissions.CommandPermissions;

import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class Engine extends JavaPlugin implements Listener
{

    public HashMap<String, Ausweis> ausweis;
    private HashMap<Player, FTSUser> player;
    private Team team;
    private UUIDFetcher uF;
    private Var var;
    private ItemStacks itemStacks;
    public int highestId;
    public int biggestBpId;
    public int biggestBriefId;
    private HashMap<Player, ChatChannels> chats;
    private ArrayList<Player> reiter;
    public HashMap<UUID, Pferd> pferde;
    public HashMap<Integer, Backpack> backpacks;
    public HashMap<Integer, Brief> briefe;
    public HashMap<Location, Brett> bretter;
    public HashMap<String, Briefkasten> briefkasten;
    public ArrayList<BriefLieferung> lieferungen;
    public ArrayList<Reisepunkt> reisepunkte;
    public HashMap<Player, BrettNote> playerBrettNote;
    private Scoreboard sb;

    private static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");

    public List<Material> mats = new ArrayList<>();
    private ProtocolManager protocolManager;

    @Override
    public void onEnable()
    {
        if (!setupEconomy())
        {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        init();
    }

    @Override
    public void onDisable()
    {
        safeAll();
    }

    private void init()
    {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        chats = new HashMap<>();
        highestId = 0;
        biggestBpId = 0;
        biggestBriefId = 0;
        playerBrettNote = new HashMap<>();
        bretter = new HashMap<>();
        //sitting = new HashMap<>();
        msgs = new Messages();
        backpacks = new HashMap<>();
        ausweis = new HashMap<>();
        uF = new UUIDFetcher();
        briefe = new HashMap<>();
        briefkasten = new HashMap<>();
        itemStacks = new ItemStacks();
        reiter = new ArrayList<>();
        reisepunkte = new ArrayList<>();
        player = new HashMap<>();
        lieferungen = new ArrayList<>();
        pferde = new HashMap<>();
        var = new Var(this);


        recipie();
        new CMDausweis(this);
        new CMDwürfel(this);
        new CMDreiten(this);
        //new CMDchannel(this);
        new CMDtaube(this);
        new CMDschlagen(this);
        new CMDremovearmorstand(this);
        new CMDpferd(this);
        new CMDbrief(this);
        new CMDcountdown(this);
        new CMDroleplay(this);
        new UserIO(this);

        new EntityClickListener(this);
        new DamageListener(this);
        new SneakListener(this);
        new HorseListener(this);
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

    private void setupScoreboad()
    {
        sb = Bukkit.getScoreboardManager().getMainScoreboard();
        for(Team t : sb.getTeams()) {
            t.unregister();
        }
        if (sb.getTeam("roleplay_modus") == null)
            team = sb.registerNewTeam("roleplay_modus");
        else team = sb.getTeam("roleplay_modus");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

        sb.registerNewTeam("0000Admin").setPrefix("§bAdmin");
        sb.registerNewTeam("0002Moderator").setPrefix("§bModerator");
        sb.registerNewTeam("0003Helfer").setPrefix("§bHelfer");
        sb.registerNewTeam("0004Walkure").setPrefix("§cWalküre");
        sb.registerNewTeam("0005Einherjer").setPrefix("§cEinherjer");
        sb.registerNewTeam("0006Architekt").setPrefix("§eArchitekt");
        sb.registerNewTeam("0007Ehrenburger").setPrefix("§eEhrenbürger");
        sb.registerNewTeam("0008Rauber").setPrefix("§9Räuber");
        sb.registerNewTeam("0009Richter").setPrefix("§9Richter");
        sb.registerNewTeam("0010Mejster").setPrefix("§9Mejster");
        sb.registerNewTeam("0011Konig").setPrefix("§2König");
        sb.registerNewTeam("0012Herzog").setPrefix("§2Herzog");
        sb.registerNewTeam("0013Furst").setPrefix("§2Fürst");
        sb.registerNewTeam("0014Graf").setPrefix("§2Graf");
        sb.registerNewTeam("0015Burgherr").setPrefix("§2Burgherr");
        sb.registerNewTeam("0016Ritter").setPrefix("§2Ritter");
        sb.registerNewTeam("0017Intendant").setPrefix("§2Intendant");
        sb.registerNewTeam("0018Kurator").setPrefix("§2Kurator");
        sb.registerNewTeam("0019Kaufmann").setPrefix("§2Kaufmann");
        sb.registerNewTeam("0020Gildenherr").setPrefix("§2Gildenherr");
        sb.registerNewTeam("0021Stadtherr").setPrefix("§2Stadtherr");
        sb.registerNewTeam("0022BMeister").setPrefix("§2Bürgermeister");
        sb.registerNewTeam("0023Siedler").setPrefix("§2Siedler");
        sb.registerNewTeam("0024Vogt").setPrefix("§6Vogt");
        sb.registerNewTeam("0025Herold").setPrefix("§6Herold");
        sb.registerNewTeam("0026Knappe").setPrefix("§6Knappe");
        sb.registerNewTeam("0027SchauSpieler").setPrefix("§6Schauspieler");
        sb.registerNewTeam("0028Musiker").setPrefix("§6Musiker");
        sb.registerNewTeam("0029Schreiber").setPrefix("§6Schreiber");
        sb.registerNewTeam("0030Seefahrer").setPrefix("§6Seefahrer");
        sb.registerNewTeam("0031Hafenmeister").setPrefix("§6Hafenmeister");
        sb.registerNewTeam("0032Handler").setPrefix("§6Händler");
        sb.registerNewTeam("0033Burger").setPrefix("§6Bürger");
        sb.registerNewTeam("0034Reisender").setPrefix("§6Reisender");
    }

    public void setPrefix(Player p) {
        String team;
        if(p.hasPermission("ftsengine.admin")) {
            team = "0000Admin";
        } else if(p.hasPermission("ftsengine.moderator")) {
            team = "0002Moderator";
        } else if(p.hasPermission("ftsengine.helfer")) {
            team = "0003Helfer";
        } else if(p.hasPermission("ftsengine.walkure")) {
            team = "0004Walkure";
        } else if(p.hasPermission("ftsengine.einherjer")) {
            team = "0005Einherjer";
        } else if(p.hasPermission("ftsengine.architekt")) {
            team = "0006Architekt";
        } else if(p.hasPermission("ftsengine.ehrenburger")) {
            team = "0007Ehrenburger";
        } else if(p.hasPermission("ftsengine.rauber")) {
            team = "0008Rauber";
        } else if(p.hasPermission("ftsengine.richter")) {
            team = "0009Richter";
        } else if(p.hasPermission("ftsengine.mejster")) {
            team = "0010Mejster";
        } else if(p.hasPermission("ftsengine.konig")) {
            team = "0011Konig";
        } else if(p.hasPermission("ftsengine.herzog")) {
            team = "0012Herzog";
        } else if(p.hasPermission("ftsengine.furst")) {
            team = "0013Furst";
        } else if(p.hasPermission("ftsengine.graf")){
            team = "0014Graf";
        } else if(p.hasPermission("ftsengine.burgherr")) {
            team = "0015Burgherr";
        } else if(p.hasPermission("ftsengine.ritter")) {
            team = "0016Ritter";
        } else if(p.hasPermission("ftsengine.intendant")) {
            team = "0017Intendant";
        } else if(p.hasPermission("ftsengine.kurator")) {
            team = "0018Kurator";
        } else if(p.hasPermission("ftsengine.kaufmann")) {
            team = "0019Kaufmann";
        } else if(p.hasPermission("ftsengine.gildenherr")) {
            team = "0020Gildenherr";
        } else if(p.hasPermission("ftsengine.stadtherr")) {
            team = "0021Stadtherr";
        } else if(p.hasPermission("ftsengine.burgermeister")) {
            team = "0022BMeister";
        } else if(p.hasPermission("ftsengine.siedler")) {
            team = "0023Siedler";
        } else if(p.hasPermission("ftsengine.vogt")) {
            team = "0024Vogt";
        } else if(p.hasPermission("ftsengine.herold")) {
            team = "0025Herold";
        } else if(p.hasPermission("ftsengine.knappe")) {
            team = "0026Knappe";
        } else if(p.hasPermission("ftsengine.schauspieler")) {
            team = "0027SchauSpieler";
        } else if(p.hasPermission("ftsengine.musiker")) {
            team = "0028Musiker";
        } else if(p.hasPermission("ftsengine.schreiber")) {
            team = "0029Schreiber";
        } else if(p.hasPermission("ftsengine.seefahrer")) {
            team = "0030Seefahrer";
        } else if(p.hasPermission("ftsengine.hafenmeister")) {
            team = "0031Hafenmeister";
        } else if(p.hasPermission("ftsengine.handler")) {
            team = "0032Handler";
        } else if(p.hasPermission("ftsengine.burger")) {
            team = "0033Burger";
        } else {
            team = "0034Reisender";
        }
        Team t = sb.getTeam(team);
        p.setPlayerListName(t.getPrefix() + " §7| " + ChatColor.RESET + p.getName());
        t.addPlayer(p);
        sendTablistHeaderAndFooter(p, "§6§lplay.ftscraft.de", "");
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e)
    {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
        {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
            {

                if (mats.contains(e.getClickedBlock().getType()))
                {
                    if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR)
                        player.get(e.getPlayer()).setSitting(e.getClickedBlock());
                }
            }
            if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR)
            {
                String iName = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
                if (iName != null)
                {
                    if (iName.startsWith("§6Personalausweis"))
                    {
                        String idS = iName.replaceAll(".*#", "");
                        int id;
                        //Bei Fehlern bei Item gucken : Id da?
                        try
                        {
                            id = Integer.valueOf(idS);
                        } catch (NumberFormatException ex)
                        {
                            e.getPlayer().sendMessage("Irgendwas ist falsch! guck mal Konsole " +
                                    "(sag Musc gescheid, dass er halberfan sagen soll: " +
                                    "\"Fehler bei Main - onItemInteract - NumberFormatException\"");
                            return;
                        }

                        for (Ausweis a : ausweis.values())
                        {
                            if (a.id == id)
                            {
                                var.sendAusweisMsg(e.getPlayer(), a);
                                break;
                            }
                        }

                    }
                }
            }
        } else if (e.getAction() == Action.LEFT_CLICK_AIR)
        {
            for (Player reiter : reiter)
            {
                if (e.getPlayer().getPassengers().contains(reiter))
                {
                    e.getPlayer().removePassenger(reiter);
                }
            }
        }

    }


    private boolean setupEconomy()
    {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
        {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    private void safeAll()
    {
        for (Ausweis a : ausweis.values())
        {
            a.safe();
        }

        for (Pferd a : pferde.values())
        {
            a.safe();
        }

        for (Backpack a : backpacks.values())
        {
            a.safe();
        }

        for(Briefkasten a : briefkasten.values()) {
            a.safe();
        }

        new UserIO(this);

    }

    public Messages msgs;

    public Ausweis getAusweis(Player player)
    {
        if (ausweis.containsKey(player.getName()))
        {
            return ausweis.get(player.getName());
        } else return null;
    }

    public Ausweis getAusweis(String player)
    {
        if (ausweis.containsKey(player))
        {
            return ausweis.get(player);
        } else return null;
    }

    public boolean hasAusweis(Player player)
    {
        return ausweis.containsKey(player.getName());
    }

    public boolean hasAusweis(String player)
    {
        return ausweis.containsKey(player);
    }

    public void addAusweis(Ausweis a)
    {
        String name = uF.getName(UUID.fromString(a.getUUID()));
        team.addPlayer(Bukkit.getOfflinePlayer(a.getUUID()));
        ausweis.put(name, a);
    }


    public Economy getEcon()
    {
        return econ;
    }

    public Var getVar()
    {
        return var;
    }

    public Team getTeam()
    {
        return team;
    }

    public ArrayList<Player> getReiter()
    {
        return reiter;
    }

    public HashMap<Player, ChatChannels> getChats()
    {
        return chats;
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    private void recipie()
    {
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

        mats.addAll(Arrays.asList(Material.ACACIA_STAIRS, Material.BRICK_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.COBBLESTONE_STAIRS,
                Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.PURPUR_STAIRS, Material.QUARTZ_STAIRS,
                Material.QUARTZ_STAIRS, Material.RED_SANDSTONE_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS,
                Material.WOOD_STAIRS, Material.GRASS_PATH));

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

    }

    public HashMap<Player, FTSUser> getPlayer()
    {
        return player;
    }

    public HashMap<Integer, Backpack> getBackpacks()
    {
        return backpacks;
    }

    public boolean horseIsDa(UUID horse)
    {
        return pferde.containsKey(horse);
    }

    public HashMap<UUID, Pferd> getPferde()
    {
        return pferde;
    }

    public boolean isRegistered(Horse horse)
    {
        for (Pferd a : pferde.values())
        {
            if (a.getUUID() == horse.getUniqueId())
            {
                return true;
            }
        }
        return false;
    }

    public boolean BriefkastenExists(Location loc) {
        for(Briefkasten a : briefkasten.values()) {
            if(a.getChest().getLocation() == loc)
                return true;
        }
        return false;
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
}

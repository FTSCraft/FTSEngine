package de.ftscraft.ftsengine.main;

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
import de.ftscraft.ftsengine.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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
    public ItemStacks itemStacks;
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
    public HashMap<Player, BrettNote> playerBrettNote;
    public Scoreboard sb;

    private static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");

    public List<Material> mats = new ArrayList<>();

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
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        if (scoreboard.getTeam("roleplay_modus") == null)
            team = scoreboard.registerNewTeam("roleplay_modus");
        else team = scoreboard.getTeam("roleplay_modus");
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
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
        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        sb.registerNewTeam("0000Admin");
        sb.registerNewTeam("0001Moderator");
        sb.registerNewTeam("0002Helfer");
        sb.registerNewTeam("0003Spieler");
        sb.getTeam("0000Admin").setPrefix("§4Kaiser §7| §7");
        sb.getTeam("0001Moderator").setPrefix("§3Mod §7| §7");
        sb.getTeam("0002Helfer").setPrefix("§bHelfer §7| §7");
        sb.getTeam("0003Spieler").setPrefix("§7");
    }

    public void setPrefix(Player p) {
        String team;
        if(p.hasPermission("ftsengine.admin")) {
            team = "0000Admin";
        } else if(p.hasPermission("ftsengine.moderator")) {
            team = "0001Moderator";
        } else if(p.hasPermission("ftsengine.helfer")) {
            team = "0002Helfer";
        } else {
            team = "0003Spieler";
        }
        sb.getTeam(team).addPlayer(p);
        for(Player a : Bukkit.getOnlinePlayers()) {
            a.setScoreboard(sb);
        }
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


    public void safeAll()
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

        new UserIO(this, true);

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
}

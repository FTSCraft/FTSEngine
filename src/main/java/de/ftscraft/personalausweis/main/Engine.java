package de.ftscraft.personalausweis.main;

import de.ftscraft.personalausweis.backpacks.BackpackType;
import de.ftscraft.personalausweis.chat.ChatChannels;
import de.ftscraft.personalausweis.commands.*;
import de.ftscraft.personalausweis.listener.*;
import de.ftscraft.personalausweis.pferd.Pferd;
import de.ftscraft.personalausweis.utils.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.List;
import java.util.logging.Logger;

public class Engine extends JavaPlugin implements Listener
{

    private HashMap<String, Ausweis> ausweis;
    private HashMap<Player, FTSUser> player;
    private Team team;
    private UUIDFetcher uF;
    private Var var;
    public int highestId;
    private HashMap<Player, ChatChannels> chats;
    private ArrayList<Player> reiter;
    public HashMap<FTSUser, ArmorStand> sitting;
    public HashMap<UUID, Pferd> pferde;

    private static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");

    private List<Material> mats = new ArrayList<>();

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

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e)
    {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            if(e.getPlayer().getVehicle() != null) {
                if(mats.contains(e.getClickedBlock().getType()))
                {
                    if (e.getPlayer().getVehicle() instanceof ArmorStand)
                    {
                        if (!((ArmorStand) e.getPlayer().getVehicle()).isVisible())
                        {
                            e.setCancelled(true);
                            e.getPlayer().sendMessage("§cBitte steht erst auf");
                            return;
                        }
                    }
                }
            }
        }

        // try
        // {
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
        //} catch(Exception ex) {

        //}


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
        sitting = new HashMap<>();
        msgs = new Messages();
        ausweis = new HashMap<>();
        uF = new UUIDFetcher();
        reiter = new ArrayList<>();
        player = new HashMap<>();
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
        new CMDcountdown(this);
        new CMDroleplay(this);
        new UserIO(this);

        new EntityClickListener(this);
        new DamageListener(this);
        new SneakListener(this);
        new HorseListener(this);
        new PlayerJoinListener(this);
        new PlayerInteractListener(this);
        new PlayerQuitListener(this);
        new PlayerChatListener(this);

        new Runner(this);
        getServer().getPluginManager().registerEvents(this, this);

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
        ItemStack bptli = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta bptlm = (LeatherArmorMeta) bptli.getItemMeta();
        bptlm.setColor(Color.GREEN);
        bptlm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bptlm.setDisplayName(BackpackType.TINY.getName());
        bptlm.setLore(Arrays.asList("§7Dieser nützliche Rucksack hat Platz für viele Sachen", "ID: -1"));
        bptli.setItemMeta(bptlm);

        ShapedRecipe tiny_backpack = new ShapedRecipe(tbpkey, bptli);
        tiny_backpack.shape("LLL","L*L","LLL");
        tiny_backpack.setIngredient('L', Material.LEATHER);
        tiny_backpack.setIngredient('*', Material.AIR);
        getServer().addRecipe(tiny_backpack);

        //LARGE BACKPACK

        NamespacedKey lbpkey = new NamespacedKey(this, "FTSlargebackpack");
        ItemStack bplli = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta bpllm = (LeatherArmorMeta) bplli.getItemMeta();
        bpllm.setColor(Color.RED);
        bpllm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        bpllm.setDisplayName(BackpackType.LARGE.getName());
        bpllm.setLore(Arrays.asList("§7In diesen Rucksack passen viele, weitere Dinge rein", "ID: -1"));
        bplli.setItemMeta(bpllm);

        ShapedRecipe large_backpack = new ShapedRecipe(lbpkey, bplli);
        large_backpack.shape("LLL", "LCL", "LLL");
        large_backpack.setIngredient('L', Material.LEATHER);
        large_backpack.setIngredient('C', Material.CHEST);
        getServer().addRecipe(large_backpack);

        //BACKPACK KEY

        NamespacedKey bpkkey = new NamespacedKey(this, "FTSbackpackkey");
        ItemStack bpkey = new ItemStack(Material.FEATHER, 1);
        ItemMeta bpkeym = bpkey.getItemMeta();
        bpkeym.setDisplayName("§5Rucksack Schlüssel");
        bpkey.setItemMeta(bpkeym);

        ShapedRecipe backpack_key = new ShapedRecipe(bpkkey, bpkey);
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
}

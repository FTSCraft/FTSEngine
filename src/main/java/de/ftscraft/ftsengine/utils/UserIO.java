package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.pferd.Pferd;
import de.ftscraft.ftsengine.reisepunkt.Reisepunkt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class UserIO {

    private Engine plugin;

    private File folder;

    public UserIO(Engine plugin)
    {
        this.plugin = plugin;
        folder = plugin.getDataFolder();
        getAusweise();
        getBackpacks();
        getBretter();
        //getPferde();
        loadBriefe();
        getBriefkasten();
    }

    public UserIO(Engine plugin, boolean save)
    {
        this.plugin = plugin;
        safeBriefe();
        //safeReisepunkte();
    }

    public void getAusweise()
    {
        File aFolder = new File(folder + "//ausweise//");
        if (!aFolder.exists()) {
            aFolder.mkdirs();
        }

        try {
            for (File aFile : Objects.requireNonNull(aFolder.listFiles())) {
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(aFile);

                String UUID = aFile.getName().replace(".yml", "");
                String lastName = cfg.getString("lastName");
                String firstName = cfg.getString("firstName");
                Gender gender = null;
                if (cfg.isSet("gender"))
                    gender = Gender.valueOf(cfg.getString("gender"));
                String race = cfg.getString("race");
                String religion = cfg.getString("religion");
                String nation = cfg.getString("nation");
                String desc = cfg.getString("desc");
                long millis = cfg.getLong("birthday");
                Integer id = cfg.getInt("id");
                Calendar cal;
                Date date = new Date();
                date.setTime(millis);
                cal = Calendar.getInstance();
                cal.setTime(date);


                if (id > plugin.highestId)
                    plugin.highestId = id;

                new Ausweis(plugin, UUID, firstName, lastName, gender, race, nation, desc, religion, cal, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getBackpacks()
    {
        File aFolder = new File(folder + "//backpacks//");

        try {
            for (File aFile : Objects.requireNonNull(aFolder.listFiles())) {
                FileConfiguration c = YamlConfiguration.loadConfiguration(aFile);
                int id;
                Inventory inv;
                BackpackType type;

                type = BackpackType.valueOf(c.getString("type"));

                List itemsList = c.getList("inventory");
                ItemStack[] items = (ItemStack[]) itemsList.toArray(new ItemStack[itemsList.size()]);
                inv = Bukkit.createInventory(null, type.getSize(), type.getName());
                inv.setContents(items);

                id = c.getInt("id");

                new Backpack(plugin, type, id, inv);

            }
        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }
    }

    private void getBretter()
    {
        File aFolder = new File(folder + "//bretter");

        if (!aFolder.exists())
            aFolder.mkdirs();

        try {

            for (File files : Objects.requireNonNull(aFolder.listFiles())) {
                try {
                    YamlConfiguration cfg = YamlConfiguration.loadConfiguration(files);
                    UUID creator;
                    if (!(cfg.get("brett.creator") instanceof String)) {
                        files.delete();
                        continue;
                    }
                    creator = UUID.fromString(cfg.getString("brett.creator"));
                    String name = files.getName().replace(".yml", "");
                    int loc_X = cfg.getInt("brett.location.X");
                    int loc_Y = cfg.getInt("brett.location.Y");
                    int loc_Z = cfg.getInt("brett.location.Z");
                    String world = cfg.getString("brett.location.world");
                    Location locaton = new Location(Bukkit.getWorld(world), loc_X, loc_Y, loc_Z);

                    BlockState bs = Bukkit.getWorld(world).getBlockAt(locaton).getState();
                    if (!(bs instanceof Sign)) {
                        continue;
                    }
                    Sign sign = (Sign) bs;

                    Brett brett = new Brett(sign, locaton, creator, name, plugin, true);

                    for (String keys : cfg.getConfigurationSection("brett.note").getKeys(false)) {
                        String title = cfg.getString("brett.note." + keys + ".title");
                        String content = cfg.getString("brett.note." + keys + ".content");
                        String note_creator = cfg.getString("brett.note." + keys + ".creator");
                        long time = cfg.getLong("brett.note." + keys + ".creation");
                        if (!title.equalsIgnoreCase("null"))
                            brett.addNote(title, content, note_creator, time, Integer.valueOf(keys));
                    }
                } catch (Exception ex) {

                }
            }
        } catch (Exception ignored) {

        }
    }

    private void getBriefkasten()
    {
        File aFolder = new File(folder + "//briefkasten//");
        if (!aFolder.exists())
            aFolder.mkdirs();

        try {

            for (File files : Objects.requireNonNull(aFolder.listFiles())) {
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(files);

                String owner = cfg.getString("owner");
                int x = cfg.getInt("location.x");
                int y = cfg.getInt("location.y");
                int z = cfg.getInt("location.z");
                String world = cfg.getString("location.world");

                Location loc = new Location(Bukkit.getWorld(world), x, y, z);

                new Briefkasten(plugin, owner, loc);

            }

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void loadBriefe()
    {
        File file = new File(plugin.getDataFolder() + "//briefe.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        for (String keys : cfg.getKeys(false)) {
            String message = cfg.getString(keys + ".message");
            String creator = cfg.getString(keys + ".creator");
            long creation = cfg.getLong(keys + ".creation");
            int id = Integer.valueOf(keys);
            new Brief(plugin, creator, message, creation, id);
            if (id > plugin.biggestBriefId)
                plugin.biggestBriefId = id;
        }
    }

    private void safeBriefe()
    {
        File file = new File(plugin.getDataFolder() + "//briefe.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        for (Brief brief : plugin.briefe.values()) {
            String id = String.valueOf(brief.id);
            cfg.set(id + ".message", brief.msg);
            cfg.set(id + ".creator", brief.creator);
            cfg.set(id + ".creation", brief.creation);
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    private void safeReisepunkte() {
        File file = new File(plugin.getDataFolder() + "//reisepunkte.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        for (Reisepunkt a : plugin.reisepunkte) {
            cfg.set(a.getName() + ".location.x", a.getLocation().getX());
            cfg.set(a.getName() + ".location.y", a.getLocation().getY());
            cfg.set(a.getName() + ".location.z", a.getLocation().getZ());
            cfg.set(a.getName() + ".location.world", a.getLocation().getWorld().getName());
            cfg.set(a.getName() + ".ziel.x", a.getZiel().getX());
            cfg.set(a.getName() + ".ziel.y", a.getZiel().getY());
            cfg.set(a.getName() + ".ziel.z", a.getZiel().getZ());
            cfg.set(a.getName() + ".ziel.world", a.getZiel().getWorld().getName());
            cfg.set(a.getName() + ".duration", a.getDuration());
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    */

}

package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.backpacks.Backpack;
import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.courier.Brief;
import de.ftscraft.ftsengine.courier.Briefkasten;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserIO {

    private final Engine plugin;

    private File folder;

    public UserIO(Engine plugin) {
        this.plugin = plugin;
        folder = plugin.getDataFolder();
        getAusweise();
        getBackpacks();
        getBretter();
        //getPferde();
        loadBriefe();
        loadBriefkasten();
    }

    public UserIO(Engine plugin, boolean save) {
        this.plugin = plugin;
        safeBriefe();
        saveBriefkasten();
        //safeReisepunkte();
    }

    public void getAusweise() {
        File aFolder = new File(folder + "//ausweise//");
        if (!aFolder.exists()) {
            aFolder.mkdirs();
        }

        try {
            for (File aFile : Objects.requireNonNull(aFolder.listFiles())) {
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(aFile);

                String UUID = aFile.getName().replace(".yml", "");
                String lastName = cfg.getString("lastName");
                if (lastName != null)
                    lastName.replace('_', ' ');
                String firstName = cfg.getString("firstName");
                if (firstName != null)
                    firstName.replace('_', ' ');
                Gender gender = null;
                if (cfg.isSet("gender"))
                    gender = Gender.valueOf(cfg.getString("gender"));
                String race = cfg.getString("race");
                String religion = cfg.getString("religion");
                String nation = cfg.getString("nation");
                if (nation != null)
                    nation = nation.replace('_', ' ');
                String desc = cfg.getString("desc");
                String spitzname = cfg.getString("spitzname");
                if (spitzname != null)
                    spitzname.replace('_', ' ');
                String link = cfg.getString("link");
                int id = cfg.getInt("id");


                if (id > plugin.highestId)
                    plugin.highestId = id;

                new Ausweis(plugin, UUID, firstName, lastName, spitzname, gender, race, nation, desc, religion, link, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadRaces() {

        //Ordner Instanz
        File raceFolder = new File(plugin.getDataFolder() + "/races/");

        //Ordner erstellen, falls noch nicht vorhanden
        if (!raceFolder.exists()) {
            raceFolder.mkdirs();
        }

        //Erstelle eine Beispieldatei wenn es noch keine anderen Dateien gibt.
        if (raceFolder.listFiles().length == 0) {

            File defaultRaceFile = new File(raceFolder + "/beispielrasse.yml");
            YamlConfiguration defaultRaceCfg = YamlConfiguration.loadConfiguration(defaultRaceFile);

            defaultRaceCfg.set("skill.nahkampf", 2);
            defaultRaceCfg.set("skill.fernkampf", 13);
            defaultRaceCfg.set("skill.agilitaet", 10);
            defaultRaceCfg.set("skill.magie", 15);
            defaultRaceCfg.set("skill.leben", 0);
            defaultRaceCfg.set("meta.name", "Hochelf");
            defaultRaceCfg.set("meta.oberrassen_name", "Elf");
            defaultRaceCfg.set("meta.textureValue", "ewogICJ0aW1lc3RhbXAiIDogMTY1MTgyOTMxNTk3MCwKICAicHJvZmlsZUlkIiA6ICIyYWFhYzk2YWIxZTA0ZWMzYjAzMmNkYjNlNjkxMzRkNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJVbHRpbWF0ZXdhZmZlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kZWE3NzhmMmFmZTMxYzMxMjJjNjFhY2VjNWQ0MzI0YmZjYWI4MGZiMGIzNjNlNWM2NWU3YWI5MTQ4ZmEzYTRmIgogICAgfQogIH0KfQ==");
            defaultRaceCfg.set("meta.id", -1);

            try {
                defaultRaceCfg.save(defaultRaceFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //Lade die Rassen
        for (File file : raceFolder.listFiles()) {

            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            Race race = new Race(
                    cfg.getInt("skill.nahkampf"),
                    cfg.getInt("skill.fernkampf"),
                    cfg.getInt("skill.agilitaet"),
                    cfg.getInt("skill.magie"),
                    cfg.getInt("skill.leben"),
                    cfg.getString("meta.name"),
                    cfg.getString("meta.oberrassen_name"),
                    cfg.getString("meta.textureValue"),
                    cfg.getInt("meta.id")
            );

            plugin.getRaces().put(race.getId(), race);

        }
    }

    private void getBackpacks() {
        File aFolder = new File(folder + "//backpacks//");

        try {
            for (File aFile : Objects.requireNonNull(aFolder.listFiles())) {
                FileConfiguration c = YamlConfiguration.loadConfiguration(aFile);
                int id;
                Inventory inv;
                BackpackType type;

                if (c.getString("type") != null)
                    type = BackpackType.valueOf(c.getString("type"));
                else type = BackpackType.LARGE;

                if(c.getList("inventory") == null) {
                    continue;
                }
                List itemsList = c.getList("inventory");
                ItemStack[] items = (ItemStack[]) itemsList.toArray(new ItemStack[itemsList.size()]);
                inv = Bukkit.createInventory(null, type.getSize(), type.getName());
                inv.setContents(items);

                if(!c.contains("id")) {
                    continue;
                }
                id = c.getInt("id");

                new Backpack(plugin, type, id, inv);

            }
        } catch (NullPointerException ignored) {

        }
    }

    private void getBretter() {
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
                    boolean admin = false;
                    if (cfg.contains("brett.admin"))
                        admin = cfg.getBoolean("brett.admin");
                    Location locaton = new Location(Bukkit.getWorld(world), loc_X, loc_Y, loc_Z);

                    BlockState bs = Bukkit.getWorld(world).getBlockAt(locaton).getState();
                    if (!(bs instanceof Sign)) {
                        continue;
                    }
                    Sign sign = (Sign) bs;

                    Brett brett = new Brett(sign, locaton, creator, name, plugin, admin, true);

                    for (String keys : cfg.getConfigurationSection("brett.note").getKeys(false)) {
                        String title = cfg.getString("brett.note." + keys + ".title");
                        String content = cfg.getString("brett.note." + keys + ".content");
                        String note_creator = cfg.getString("brett.note." + keys + ".creator");
                        long time = cfg.getLong("brett.note." + keys + ".creation");
                        if (!title.equalsIgnoreCase("null"))
                            brett.addNote(title, content, note_creator, time, Integer.valueOf(keys));
                    }
                } catch (Exception ignored) {

                }
            }
        } catch (Exception ignored) {

        }
    }


    private void loadBriefe() {
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

    private void safeBriefe() {
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

    private void loadBriefkasten() {

        File aFolder = new File(folder + "//briefkasten//");
        if (!aFolder.exists()) {
            aFolder.mkdirs();
        }

        for (File file : aFolder.listFiles()) {

            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            double x = cfg.getDouble("loc.x");
            double y = cfg.getDouble("loc.y");
            double z = cfg.getDouble("loc.z");
            String world = cfg.getString("loc.world");
            UUID player = UUID.fromString(cfg.getString("player"));

            Briefkasten briefkasten = new Briefkasten(plugin, new Location(Bukkit.getWorld(world), x, y, z), player);

        }

    }

    public void saveBriefkasten() {

        for (Briefkasten briefkasten : plugin.briefkasten.values()) {
            File file = new File(plugin.getDataFolder() + "//briefkasten//" + briefkasten.getPlayer().toString() + ".yml");

            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            cfg.set("loc.x", briefkasten.getLocation().getX());
            cfg.set("loc.y", briefkasten.getLocation().getY());
            cfg.set("loc.z", briefkasten.getLocation().getZ());

            cfg.set("loc.world", briefkasten.getLocation().getWorld().getName());

            cfg.set("player", briefkasten.getPlayer().toString());

            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

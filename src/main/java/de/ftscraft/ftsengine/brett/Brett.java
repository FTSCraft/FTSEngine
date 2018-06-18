package de.ftscraft.ftsengine.brett;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class Brett
{

    private String name;
    private UUID creator;
    private BrettSign sign;
    private BrettGUI gui;
    private ArrayList<BrettNote> notes;
    private File file;
    private YamlConfiguration cfg;
    private Engine plugin;

    public Brett(Sign sign, Location location, UUID creator, String name, Engine plugin) {
        this.name = name;
        this.creator = creator;
        this.gui = new BrettGUI(this, plugin);
        this.notes = new ArrayList();
        this.plugin = plugin;
        this.sign = new BrettSign(sign, location, creator);
        this.file = new File(plugin.getDataFolder() + "//bretter//" + name + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(this.file);
        if (!this.file.exists()) {
            this.cfg.set("brett.creator", creator);
            this.cfg.set("brett.location.X", location.getX());
            this.cfg.set("brett.location.Y", location.getY());
            this.cfg.set("brett.location.Z", location.getZ());
            this.cfg.set("brett.location.world", location.getWorld().getName());

            for(int i = 0; i < 28; ++i) {
                this.cfg.set("brett.note." + i + ".title", "null");
                this.cfg.set("brett.note." + i + ".content", "null");
                this.cfg.set("brett.note." + i + ".creator", "null");
                this.cfg.set("brett.note." + i + ".creation", 0);
            }

            try {
                this.cfg.save(this.file);
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

    }

    public Brett(Sign sign, Location location, UUID creator, String name, Engine plugin, boolean onSetup) {
        this.name = name;
        this.creator = creator;
        this.gui = new BrettGUI(this, plugin);
        this.notes = new ArrayList();
        this.plugin = plugin;
        this.sign = new BrettSign(sign, location, creator);
        this.file = new File(plugin.getDataFolder() + "//bretter//" + name + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(this.file);
        if (!this.file.exists()) {
            this.cfg.set("brett.creator", creator.toString());
            this.cfg.set("brett.location.X", location.getX());
            this.cfg.set("brett.location.Y", location.getY());
            this.cfg.set("brett.location.Z", location.getZ());
            this.cfg.set("brett.location.world", location.getWorld().getName());

            for(int i = 0; i < 27; ++i) {
                this.cfg.set("brett.note." + i + ".title", "null");
                this.cfg.set("brett.note." + i + ".content", "null");
                this.cfg.set("brett.note." + i + ".creator", "null");
                this.cfg.set("brett.note." + i + ".creation", 0);
            }

            try {
                this.cfg.save(this.file);
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

        if (onSetup) {
            plugin.bretter.put(location, this);
        }

    }

    public String getName() {
        return this.name;
    }

    public UUID getCreator() {
        return this.creator;
    }

    public BrettSign getSign() {
        return this.sign;
    }

    public BrettGUI getGui() {
        return this.gui;
    }

    public ArrayList<BrettNote> getNotes() {
        return this.notes;
    }

    public YamlConfiguration getCfg() {
        return this.cfg;
    }

    public boolean addNote(String title, String content, String creator, long time, int id) {
        new BrettNote(this, title, content, creator, id, time, this.plugin);
        return false;
    }

    public void saveCfg() {
        try {
            this.cfg.save(this.file);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public void checkForRunOut() {
        Iterator var1 = this.getNotes().iterator();

        while(var1.hasNext()) {
            BrettNote note = (BrettNote)var1.next();
            if (note.getTime() + 864000000L <= System.currentTimeMillis()) {
                note.remove();
            }
        }

    }

    public void remove() {
        this.plugin.bretter.remove(this.sign.getLocation());
        this.file.delete();
    }

}

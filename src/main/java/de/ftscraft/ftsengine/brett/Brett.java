package de.ftscraft.ftsengine.brett;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Brett {

    private final String name;
    private final UUID creator;
    private final Location signLocation;
    private final BrettGUI gui;
    private final ArrayList<BrettNote> notes;
    private final File file;
    private final YamlConfiguration cfg;
    private final Engine plugin;
    private final boolean admin;

    public Brett(Location location, UUID creator, String name, boolean admin, Engine plugin) {
        this.name = name;
        this.creator = creator;
        this.admin = admin;
        this.notes = new ArrayList<>();
        this.plugin = plugin;
        this.gui = new BrettGUI(this);
        this.signLocation = location;
        this.file = new File(plugin.getDataFolder() + "//bretter//" + name + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(this.file);
        if (!this.file.exists()) {
            this.cfg.set("brett.creator", creator.toString());
            this.cfg.set("brett.admin", admin);
            this.cfg.set("brett.location.X", location.getX());
            this.cfg.set("brett.location.Y", location.getY());
            this.cfg.set("brett.location.Z", location.getZ());
            this.cfg.set("brett.location.world", location.getWorld().getName());

            for (int i = 0; i < 27 * 5; ++i) {
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

    public Brett(Location location, UUID creator, String name, Engine plugin, boolean admin, boolean onSetup) {
        this.name = name;
        this.creator = creator;
        this.admin = admin;
        this.gui = new BrettGUI(this);
        this.notes = new ArrayList<>();
        this.plugin = plugin;
        this.signLocation = location;
        this.file = new File(plugin.getDataFolder() + "//bretter//" + name + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(this.file);
        if (!this.file.exists()) {
            this.cfg.set("brett.creator", creator.toString());
            this.cfg.set("brett.location.X", location.getX());
            this.cfg.set("brett.location.Y", location.getY());
            this.cfg.set("brett.location.Z", location.getZ());
            this.cfg.set("brett.location.world", location.getWorld().getName());

            for (int i = 0; i < 27 * 5; ++i) {
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

    public BrettGUI getGui() {
        return this.gui;
    }

    public ArrayList<BrettNote> getNotes() {
        return this.notes;
    }

    public YamlConfiguration getCfg() {
        return this.cfg;
    }

    public void addNote(String title, String content, String creator, long time, int id, boolean anonym) {
        new BrettNote(this, title, content, creator, id, time, anonym);
    }

    public void saveCfg() {
        try {
            this.cfg.save(this.file);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public void checkForRunOut() {

        for (BrettNote note : this.getNotes()) {
            if (note.getTime() + 604800000L <= System.currentTimeMillis()) {
                note.remove();
            }
        }

    }

    public void remove() {
        this.plugin.bretter.remove(signLocation);
        this.file.delete();
    }

    public boolean isAdmin() {
        return admin;
    }
}

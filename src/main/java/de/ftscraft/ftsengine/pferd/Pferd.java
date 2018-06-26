package de.ftscraft.ftsengine.pferd;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Pferd {

    private Horse horse;
    private UUID owner;
    private boolean locked;
    private boolean chosed;
    private int persID;
    private World world;
    private Engine plugin;
    private int price;
    private String name;
    private int id;

    private String color;
    private String style;
    private double speed;
    private ItemStack[] inventory;
    private double health;
    private double jump;
    private double x, y, z;
    private String worldName;

    public Location location;

    public Pferd(Engine plugin, int id, World world, UUID owner, boolean locked, int price, int persID, String name, boolean choosed) {
        this.owner = owner;
        this.locked = locked;
        this.name = name;
        this.persID = persID;
        this.price = price;
        this.chosed = choosed;
        this.id = id;

        if (this.persID == 0) {
            FTSUser user = plugin.getPlayer().get(Bukkit.getPlayer(owner));
            this.persID = 1;
            while (user.pferdIDIsDa(this.persID)) {
                this.persID++;
                if (this.persID > 3) {
                    user.sendMsg("§7Du hast zu viele Pferde!");
                    return;
                }
            }
        }

        this.plugin = plugin;
        this.world = world;
        plugin.pferde.put(id, this);

    }

    public boolean isOwner(Player p) {
        return p.getUniqueId().toString().equals(owner.toString());
    }

    public void teleport(Player p) {
        this.location = p.getLocation().clone();
        spawnHorse(p);
        this.location = horse.getLocation();
    }

    public void lock(Player p) {
        if (p.getUniqueId().toString().equals(owner.toString())) {
            if (locked) {
                locked = false;
                horse.setInvulnerable(false);
                horse.setAI(true);
                p.sendMessage("§eDas Pferd ist nun entsichert!");
            } else {
                locked = true;
                horse.setInvulnerable(true);
                horse.setAI(false);
                p.sendMessage("§eDas Pferd ist nun gesichert!");
            }
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public int getPrice() {
        return price;
    }

    public int getPersID() {
        return persID;
    }

    public boolean isChosed() {
        return chosed;
    }

    public void safe() {
        File file = new File(plugin.getDataFolder() + "//pferde//" + id + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("horseID", id);
        cfg.set("owner", owner.toString());
        cfg.set("locked", locked);
        cfg.set("id", persID);
        cfg.set("price", price);
        cfg.set("world", world.getName());
        cfg.set("name", name);
        cfg.set("chosed", chosed);
        if (horse != null) {
            cfg.set("horse.color", horse.getColor().toString());
            cfg.set("horse.style", horse.getStyle().toString());
            cfg.set("horse.speed", horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue());
            cfg.set("horse.inventory", horse.getInventory().getContents());
            cfg.set("horse.health", horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            cfg.set("horse.jump", horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getValue());
            cfg.set("horse.location.x", location.getX());
            cfg.set("horse.location.y", location.getY());
            cfg.set("horse.location.z", location.getZ());
            cfg.set("horse.location.world", location.getWorld().getName());
            horse.getLocation().getChunk().load();
            horse.remove();
        }

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getName() {
        return name;
    }

    public Integer getID() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setChosed(boolean chosed) {
        this.chosed = chosed;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setOwner(UUID owner, Player p) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(this.owner);
        plugin.getEcon().depositPlayer(op, price);
        this.owner = owner;

        for (FTSUser users : plugin.getPlayer().values()) {
            if (users.getPferde().contains(this))
                users.getPferde().remove(this);
        }

        p.sendMessage("§eDir gehört nun §c" + this.getName());
        FTSUser user = plugin.getPlayer().get(p);
        user.addPferd(this);
        setPrice(-1);

    }

    public void dead() {
        for (FTSUser users : plugin.getPlayer().values()) {
            if (users.getPferde().contains(this))
                users.getPferde().remove(this);
        }

        plugin.getPferde().remove(id);


        File file = new File(plugin.getDataFolder() + "//pferde//" + id + ".yml");
        file.delete();

    }

    public void setHorseData(String color, String style, double speed, ItemStack[] inventory, double health, double jump, double x, double y, double z, String world) {
        this.color = color;
        this.style = style;
        this.speed = speed;
        this.inventory = inventory;
        this.health = health;
        this.jump = jump;
        this.worldName = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.location = new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    public void setHorse(Horse horse) {
        this.horse = horse;
        setHorseData(horse.getColor().toString(), horse.getStyle().toString(), horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue(),
                horse.getInventory().getContents(), horse.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getValue(),
                horse.getLocation().getX(), horse.getLocation().getY(), horse.getLocation().getZ(), horse.getLocation().getWorld().getName());
        this.location = horse.getLocation();
        horse.setMetadata("FTSEngine.Horse", new FixedMetadataValue(plugin, id));
    }

    public void removeHorse() {
        if (this.horse != null) {
            this.location = horse.getLocation().clone();
            this.horse.remove();
        }
    }

    public void spawnHorse(Player p) {
        removeHorse();
        World w = Bukkit.getWorld(worldName);
        Location loc = new Location(w, x, y, z);
        if (p != null)
            loc = p.getLocation().clone();
        loc.getChunk().load();
        Horse h = w.spawn(loc, Horse.class);
        Horse.Color c = Horse.Color.valueOf(color);
        Horse.Style s = Horse.Style.valueOf(style);
        h.setColor(c);
        h.setStyle(s);
        h.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
        h.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        h.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(jump);
        h.getInventory().setContents(inventory);
        h.setCustomName(name);
        h.setAdult();
        h.setOwner(Bukkit.getOfflinePlayer(owner));
        h.setMetadata("FTSEngine.Horse", new FixedMetadataValue(plugin, id));
        this.location = h.getLocation();
        this.horse = h;
        loc.getChunk().unload();
    }
}

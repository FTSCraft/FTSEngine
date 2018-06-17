package de.ftscraft.ftsengine.pferd;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Pferd
{

    private Horse horse;
    private UUID uuid;
    private UUID owner;
    private Player player;
    private boolean locked;
    private boolean chosed;
    private int persID;
    private World world;
    private Engine plugin;
    private int price;
    private String name;

    public Pferd(Engine plugin, UUID horseUuid, World world, UUID owner, boolean locked, int price, int persID, String name, boolean choosed)
    {
        this.uuid = horseUuid;
        this.owner = owner;
        this.locked = locked;
        this.name = name;
        this.persID = persID;
        this.price = price;
        this.chosed = choosed;

        if (this.persID == 0)
        {
            FTSUser user = plugin.getPlayer().get(Bukkit.getPlayer(owner));
            this.persID = 1;
            while (user.pferdIDIsDa(this.persID))
            {
                this.persID++;
                if (this.persID > 3)
                {
                    user.sendMsg("§7Du hast zu viele Pferde!");
                    return;
                }
            }
        }

        for (Entity e : world.getEntities())
        {
            if (e.getUniqueId().toString().equalsIgnoreCase(horseUuid.toString()))
                this.horse = (Horse) e;
        }

        this.plugin = plugin;
        this.world = world;
        plugin.pferde.put(horseUuid, this);

    }

    public boolean isOwner(Player p)
    {
        return p.getUniqueId().toString().equals(owner.toString());
    }

    public void teleport(Player p)
    {
        if(!checkHorse()) {
            p.sendMessage("§cDein Pferd wurde nicht Gefunden. Wir versuchen derzeit dieses Problem zu lösen!");
            return;
        }
        if (p.getUniqueId().toString().equals(p.getUniqueId().toString()))
        {
            horse.teleport(p.getLocation());
        }
    }

    public void lock(Player p)
    {
        if(!checkHorse()) {
            p.sendMessage("§cDein Pferd wurde nicht Gefunden. Wir versuchen derzeit dieses Problem zu lösen!");
        }
        if (p.getUniqueId().toString().equals(owner.toString()))
        {
            if (locked)
            {
                locked = false;
                horse.setInvulnerable(false);
                horse.setAI(true);
                p.sendMessage("§eDas Pferd ist nun entsichert!");
            } else
            {
                locked = true;
                horse.setInvulnerable(true);
                horse.setAI(false);
                p.sendMessage("§eDas Pferd ist nun gesichert!");
            }
        }
    }

    public boolean isLocked()
    {
        return locked;
    }

    public int getPrice()
    {
        return price;
    }

    public int getPersID()
    {
        return persID;
    }

    public boolean isChosed()
    {
        return chosed;
    }

    public void safe()
    {
        File file = new File(plugin.getDataFolder() + "//pferde//" + uuid.toString() + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("uuid", uuid.toString());
        cfg.set("owner", owner.toString());
        cfg.set("locked", locked);
        cfg.set("id", persID);
        cfg.set("price", price);
        cfg.set("world", world.getName());
        cfg.set("name", name);
        cfg.set("chosed", chosed);

        try
        {
            cfg.save(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public String getName()
    {
        return name;
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public void setChosed(boolean chosed)
    {
        this.chosed = chosed;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public void setOwner(UUID owner, Player p)
    {
        OfflinePlayer op = Bukkit.getOfflinePlayer(this.owner);
        plugin.getEcon().depositPlayer(op, price);
        this.owner = owner;

        for(FTSUser users : plugin.getPlayer().values()) {
            if(users.getPferde().contains(this))
                users.getPferde().remove(this);
        }

        p.sendMessage("§eDir gehört nun §c"+this.getName());
        FTSUser user = plugin.getPlayer().get(p);
        user.addPferd(this);
        setPrice(-1);

    }

    public void dead()
    {
        for(FTSUser users : plugin.getPlayer().values()) {
            if(users.getPferde().contains(this))
                users.getPferde().remove(this);
        }

        plugin.getPferde().remove(uuid);


        File file = new File(plugin.getDataFolder() + "//pferde//" + uuid.toString() + ".yml");
        file.delete();

    }
    public boolean checkHorse() {
        if(horse == null) {
            for (Entity e : world.getEntities())
            {
                if (e.getUniqueId().toString().equalsIgnoreCase(uuid.toString()))
                    this.horse = (Horse) e;
            }
        }
        return horse != null;
    }
}

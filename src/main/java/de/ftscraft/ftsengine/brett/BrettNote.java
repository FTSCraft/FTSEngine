package de.ftscraft.ftsengine.brett;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BrettNote {

    public int page;
    private String creator;
    private String rpName;
    private String title;
    private String content;
    private boolean create;
    private int id;
    public int invslot;
    private ItemStack item;
    private long time;
    private Brett brett;

    public BrettNote(Brett brett, String title, String content, String creator, int id, long time, Engine plugin) {
        this.title = title;
        this.brett = brett;
        this.content = content;
        this.time = time;
        this.creator = creator;
        create = false;
        this.id = id;

        getRPName();
        this.item = generateItem();
        addToBrett();

    }

    public BrettNote(Brett brett, String creator, boolean create) {
        if (create) {
            this.brett = brett;
            this.creator = creator;
            this.time = System.currentTimeMillis();
            this.create = true;

            YamlConfiguration cfg = brett.getCfg();

            for (String keys : cfg.getConfigurationSection("brett.note").getKeys(false))
                if (cfg.getString("brett.note." + keys + ".title").equalsIgnoreCase("null")) {
                    this.id = Integer.parseInt(keys);
                    break;
                }
        }
    }

    private ItemStack generateItem() {
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName("§6" + StringUtils.abbreviate(title, 30));
        List<String> lore = new ArrayList<>();
        lore.add("§7" + StringUtils.abbreviate(content, 30));
        lore.add("§8Von: §2" + rpName + " (" + creator + ")");
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }


    public void addToBrett() {
        if (create) {
            brett.getCfg().set("brett.note." + id + ".title", title);
            brett.getCfg().set("brett.note." + id + ".content", content);
            brett.getCfg().set("brett.note." + id + ".creator", creator);
            brett.getCfg().set("brett.note." + id + ".creation", time);
            brett.saveCfg();
            item = generateItem();
        }
        brett.getGui().addNote(item, this);
        brett.getNotes().add(this);
    }

    public String getCreator() {
        return creator;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public Brett getBrett() {
        return brett;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTitle() {
        return title != null;
    }

    public boolean isContent() {
        return content != null;
    }


    public void remove() {
        brett.getNotes().remove(this);
        brett.getGui().removeNote(invslot, page);
        brett.getCfg().set("brett.note." + id + ".title", "null");
        brett.getCfg().set("brett.note." + id + ".content", "null");
        brett.getCfg().set("brett.note." + id + ".creator", "null");
        brett.getCfg().set("brett.note." + id + ".creation", "null");
        brett.saveCfg();
    }

    private void getRPName() {
        Ausweis a = Engine.getInstance().getAusweis(UUIDFetcher.getUUID(creator));
        rpName = (a == null ? "???" : a.getFirstName() + " " + a.getLastName());
    }

}

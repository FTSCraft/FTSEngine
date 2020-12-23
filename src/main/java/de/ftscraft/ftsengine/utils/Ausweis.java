package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class Ausweis {

    private String UUID;
    private String firstName,
            lastName,
            spitzname;

    private Gender gender;

    private String race;
    private String nation;
    private String desc;
    private String religion;
    private Calendar birthday;
    public final Integer id;

    private Engine plugin;

    public Ausweis(Engine plugin, String UUID, String firstName, String lastName, String spitzname, Gender gender, String race, String nation, String desc, String religion, Calendar cal, Integer id) {
        this.plugin = plugin;
        this.UUID = UUID;
        this.firstName = firstName;
        this.spitzname = spitzname;
        this.lastName = lastName;
        this.gender = gender;
        this.race = race;
        this.nation = nation;
        this.birthday = cal;
        this.desc = desc;
        this.religion = religion;
        this.id = id;
        plugin.addAusweis(this);
        checkBirthday();
    }

    public Ausweis(Engine plugin, Player player) {
        plugin.highestId++;
        this.id = plugin.highestId;
        this.UUID = player.getUniqueId().toString();
        this.plugin = plugin;
        plugin.addAusweis(this);

    }

    private boolean todayBirthday() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH) == birthday.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) == birthday.get(Calendar.DAY_OF_MONTH);
    }

    private void checkBirthday() {
        if (todayBirthday()) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user " + Bukkit.getOfflinePlayer(getUUID()).getName() + " parent addtemp premium 2d");
        }
    }

    public boolean save() {

        File file = new File(plugin.getDataFolder() + "//ausweise//" + getUUID() + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("firstName", firstName);
        cfg.set("lastName", lastName);
        if (gender != null)
            cfg.set("gender", gender.toString());
        cfg.set("spitzname", spitzname);
        cfg.set("race", race);
        cfg.set("nation", nation);
        cfg.set("desc", desc);
        cfg.set("id", id);
        cfg.set("religion", religion);
        if (birthday != null)
            cfg.set("birthday", birthday.getTime().getTime());

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String getUUID() {
        return UUID;
    }

    public String getFirstName() {
        if(firstName == null)
            return null;
        return firstName.replace('_', ' ');
    }

    public String getLastName() {
        if(lastName == null)
            return null;
        return lastName.replace('_', ' ');
    }

    public Gender getGender() {
        if(gender == null)
            return null;
        return gender;
    }

    public String getRace() {
        if(race == null)
            return null;
        return race.replace('_', ' ');
    }

    public String getNation() {
        if(nation == null)
            return null;
        return nation.replace('_', ' ');
    }

    public String getDesc() {
        if(desc == null)
            return null;
        return desc;
    }

    public String getReligion() {
        if(religion == null)
            return null;
        return religion.replace('_', ' ');
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public ItemStack getAsItem() {
        ItemStack is = new ItemStack(Material.FLOWER_BANNER_PATTERN, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§6Personalausweis " + lastName + " #" + id);
        //im.addEnchant(Enchantment.LUCK, 0, true);
        //im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }

    public void setBirthday(Calendar birthday) {
        this.birthday = birthday;
        checkBirthday();
    }

    public String getBirthdayString() {
        String s;
        if (birthday == null)
            return "N/A";
        s = birthday.get(Calendar.DAY_OF_MONTH) + "." + (birthday.get(Calendar.MONTH) + 1);
        return s;
    }

    public boolean birthdaySetuped() {

        if(birthday == null)
            return false;

        if(birthday.get(Calendar.MILLISECOND) == 0) {
            return false;
        }

        return birthday != null;
    }

    public void setSpitzname(String spitzname) {
        this.spitzname = spitzname;
    }

    public String getSpitzname() {
        if(spitzname == null)
            return null;
        return spitzname;
    }
}

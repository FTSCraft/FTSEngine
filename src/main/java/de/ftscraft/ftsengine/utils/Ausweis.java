package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
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

public class Ausweis
{

    private String UUID;
    private String firstName,
                   lastName;

    private Gender gender;

    private String race;
    private String nation;
    private String desc;
    private String religion;
    private Calendar birthday;
    public final Integer id;

    private Engine plugin;

    public Ausweis(Engine plugin, String UUID, String firstName, String lastName, Gender gender, String race, String nation, String desc, String religion, Calendar cal, Integer id)
    {
        this.plugin = plugin;
        this.UUID = UUID;
        this.firstName = firstName;
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

    public Ausweis(Engine plugin, Player player)
    {
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

    private void checkBirthday()
    {
        if(todayBirthday())
        {
            if (gender == Gender.FEMALE)
            {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user Spielername group addtemp walkuere 2d");
            } else if (gender == Gender.MALE)
            {
                plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user Spielername group addtemp einherjer 2d");
            }
        }
    }

    public boolean safe() {

        File file = new File(plugin.getDataFolder() + "//ausweise//"+getUUID()+".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("firstName", firstName);
        cfg.set("lastName", lastName);
        if(gender != null)
            cfg.set("gender", gender.toString());
        cfg.set("race", race);
        cfg.set("nation", nation);
        cfg.set("desc", desc);
        cfg.set("id", id);
        cfg.set("religion", religion);
        if(birthday != null)
            cfg.set("birthday", birthday.getTime().getTime());

        try
        {
            cfg.save(file);
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String getUUID()
    {
        return UUID;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public Gender getGender()
    {
        return gender;
    }

    public String getRace()
    {
        return race;
    }

    public String getNation()
    {
        return nation;
    }

    public String getDesc()
    {
        return desc;
    }

    public String getReligion()
    {
        return religion;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public void setRace(String race)
    {
        this.race = race;
    }

    public void setNation(String nation)
    {
        this.nation = nation;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public void setReligion(String religion)
    {
        this.religion = religion;
    }

    public ItemStack getAsItem() {
        ItemStack is = new ItemStack(Material.PAPER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§6Personalausweis "+lastName+" #"+id);
        im.addEnchant(Enchantment.LUCK, 0, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(im);
        return is;
    }

    public void setBirthday(Calendar birthday)
    {
        this.birthday = birthday;
        checkBirthday();
    }

    public String getBirthdayString()
    {
        String s;
        if(birthday == null)
            return "N/A";
        s = birthday.get(Calendar.DAY_OF_MONTH) + "." + (birthday.get(Calendar.MONTH)+1);
        return s;
    }

    public boolean birthdaySetuped() {
        return birthday != null;
    }

}

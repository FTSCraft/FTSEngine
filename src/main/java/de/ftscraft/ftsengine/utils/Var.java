package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Var
{

    private UUIDFetcher uF;
    private Engine plugin;

    public Var(Engine plugin)
    {
        this.plugin = plugin;
        this.uF = new UUIDFetcher();
    }

    public void sendAusweisMsg(Player p, Ausweis a)
    {
        p.sendMessage(" ");
        p.sendMessage("§c------ Ausweis von §e" + uF.getName(UUID.fromString(a.getUUID())) + " §c------");
        p.sendMessage("§cNachname: §e" + a.getLastName());
        p.sendMessage("§cVorname: §e" + a.getFirstName());
        p.sendMessage("§cSpitzname: §e" + (a.getSpitzname() != null ? a.getSpitzname().replace('_', ' ') : "N/A"));
        p.sendMessage("§cGeschlecht: §e" + (a.getGender() == Gender.MALE ? "Mann" : a.getGender() == Gender.FEMALE ? "Frau" : a.getGender() == Gender.DIVERS ? "Divers" : "N/A"));
        p.sendMessage("§cGeburtstag: §e" + a.getBirthdayString());
        p.sendMessage("§cRasse: §e" + (a.getRace() != null ? a.getRace() : "N/A"));
        p.sendMessage("§cNation: §e" + (a.getNation() != null ? a.getNation() : "N/A"));
        p.sendMessage("§cBeschreibung: §e" + (a.getDesc() != null ? a.getDesc() : "N/A"));
        p.sendMessage("§cReligion: §e" + (a.getReligion() != null ? a.getReligion() : "N/A"));
        p.sendMessage("§7ID: #" + a.id);
    }

    public void sendHelpMsg(Player p)
    {
        p.sendMessage("§c----- §e/ausweis §c-----");
        p.sendMessage("§e/ausweis name [Vorname] [Nachname] §bÄndert deinen Namen und erstellt beim 1. Mal einen Ausweis");
        p.sendMessage("§e/ausweis alter [DD-MM] §bSetzt das Geburtsdatum");
        p.sendMessage("§e/ausweis geschlecht [m/f] §bSetzt das Geschlecht (m - Mann | f - Frau | d - Divers)");
        p.sendMessage("§e/ausweis spitzname [Spitzname] §bGebe dir einen Spitznamen");
        p.sendMessage("§e/ausweis rasse [Rasse] §bSetzt deine Rasse");
        p.sendMessage("§e/ausweis nation [Nation] §bSetzt deine Nation");
        p.sendMessage("§e/ausweis beschreibung [Beschr.] §bSetzt deine kurze Beschreibung von dir (Mind. 4 Wörter)");
        p.sendMessage("§e/ausweis religion [Religion] §bSetzt deine Religion");
        p.sendMessage("§e/ausweis kopieren [Spielername] §bKopiert den Ausweis eines anderen Spielers");
        p.sendMessage("§6FTSEngine von §5halberfan §6für FTS-Craft");
    }

    public void sendPferdHelpMsg(Player p)
    {
        p.sendMessage("§c----- §e/pferd §c-----");
        p.sendMessage("§e/pferd beanspruchen [Name] §bBeansprucht ein Pferd");
        p.sendMessage("§e/pferd teleportieren §bTeleportiert dein Ausgewähltes Pferd");
        p.sendMessage("§e/pferd verkaufen §bSetzt ein Pferd zum Verkauf (was du reitest)");
        p.sendMessage("§e/pferd kaufen §bKauft ein Pferd");
        p.sendMessage("§e/pferd abstellen §bStellt dein Pferd ab (was du reitest)");
        p.sendMessage("§e/pferd list §bListet deine Pferd auf");
        p.sendMessage("§e/pferd auswählen [Name] §bSetzt dein aufgewähltes Pferd");
        p.sendMessage("§6FTSEngine von §5halberfan §6für FTS-Craft");
    }

    public void sendReisepunkHelpMsg(Player p) {
        p.sendMessage("§c--- §e/reisepunk §c---");
        p.sendMessage("§e/reisepunkt setzen [Name] [Dauer] §bSetzt ein Reisepunk");
        p.sendMessage("§e/reisepunkt ziel [Name] §bSetzt das Ziel eines Reisepunkts");
        p.sendMessage("§e/reisepunkt list §bListet alle Reisepunkte auf");
        p.sendMessage("§e/reisepunk löschen [Name] §bEntfernt ein Reisepunkt");
    }

    public void ChatInRoleplay(Player p, String msg)
    {

    }

    public void ChatInHandel(Player p, String msg)
    {

    }

    public void ChatInFlüster(Player p, String msg)
    {

    }

    public void ChatInRufen(Player p, String msg)
    {

    }

    public float getYawByBlockFace(BlockFace bf)
    {
        float yaw = 0;

        if (bf == BlockFace.NORTH)
        {
            return -180;
        } else if (bf == BlockFace.EAST)
        {
            return -90;
        } else if (bf == BlockFace.SOUTH)
        {
            return 0;
        } else if (bf == BlockFace.WEST)
        {
            return 90;
        }

        return yaw;
    }


    public int getBackpackID(ItemStack backpack)
    {

        String lore = backpack.getItemMeta().getLore().get(1);
        String idS = lore.replaceAll(".*#", "");

        Integer id = -1;

        try
        {
            id = Integer.valueOf(idS);
        } catch (NumberFormatException e)
        {
            return -1;
        }

        return id;
    }

    public static String millisToWeek(int millis) {
        int seconds = 0;

        int minutes = millis;
        int hours = 0;
        int days = 0;
        int weeks = 0;


        while (minutes > 60) {
            minutes = minutes - 60;
            hours++;
        }
        while (hours > 24) {
            hours = hours - 24;
            days++;
        }
        while (days > 7) {
            days = days - 7;
            weeks++;
        }

        String a = "§c"+weeks+" Wochen, "+days+" Tage, "+hours+" Stunden, "+minutes+" Minuten und "+seconds+" Sekunden";
        return a;
    }

    private List<Material> carpets = new ArrayList<>();

    public List<Material> getCarpets() {

        if(carpets.isEmpty()) {

            for (Material value : Material.values()) {
                if(value.toString().contains("CARPET"))
                    carpets.add(value);
            }

            carpets.remove(Material.LEGACY_CARPET);

        }

        return carpets;

    }

    private List<Material> noStairs = new ArrayList<>();

    public List<Material> getNoStairs() {

        if(noStairs.isEmpty()) {

            noStairs.addAll(Arrays.asList(

                    Material.DARK_PRISMARINE_STAIRS, Material.PRISMARINE_STAIRS, Material.PRISMARINE_BRICK_STAIRS,
                    Material.POLISHED_ANDESITE_STAIRS, Material.POLISHED_DIORITE_STAIRS, Material.POLISHED_GRANITE_STAIRS,
                    Material.SMOOTH_RED_SANDSTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS,
                    Material.END_STONE_BRICK_STAIRS, Material.STONE_STAIRS, Material.SMOOTH_SANDSTONE_STAIRS,
                    Material.SMOOTH_QUARTZ_STAIRS, Material.GRANITE_STAIRS, Material.ANDESITE_STAIRS, Material.RED_NETHER_BRICK_STAIRS,
                    Material.DIORITE

            ));

        }

        return noStairs;
    }
}

package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Var
{

    public static void sendAusweisMsg(Player p, Ausweis a)
    {
        p.sendMessage(" ");
        p.sendMessage("§c------ Ausweis von §e" + UUIDFetcher.getName(UUID.fromString(a.getUUID())) + " §c------");
        p.sendMessage("§cNachname: §e" + a.getLastName());
        p.sendMessage("§cVorname: §e" + a.getFirstName());
        p.sendMessage("§cGeschlecht: §e" + (a.getGender() == Ausweis.Gender.MALE ? "Mann" : a.getGender() == Ausweis.Gender.FEMALE ? "Frau" : a.getGender() == Ausweis.Gender.DIVERS ? "Divers" : "N/A"));
        p.sendMessage("§cRasse: §e" + (a.getRace() != null ? a.getRace() : "N/A"));
        p.sendMessage("§cAussehen: §e" + (a.getDesc() != null ? a.getDesc() : "N/A"));
        if(a.getForumLink() != null ){
            ComponentBuilder componentBuilder = new ComponentBuilder("§e[KLICK]");
            componentBuilder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, a.getForumLink()));
            p.sendMessage(new ComponentBuilder("§cVorstellung: ").append(componentBuilder.create()).create());
        } else {
            ComponentBuilder componentBuilder = new ComponentBuilder("§e[NICHT VORHANDEN]");
            p.sendMessage(new ComponentBuilder("§cVorstellung: ").append(componentBuilder.create()).create());
        }

        p.sendMessage("§7ID: #" + a.id);
    }

    public static void sendHelpMsg(Player p)
    {
        p.sendMessage("§c----- §e/ausweis §c-----");
        p.sendMessage("§e/ausweis name [Vorname] [Nachname] §bÄndert deinen Namen und erstellt beim 1. Mal einen Ausweis");
        p.sendMessage("§e/ausweis geschlecht [m/f] §bSetzt die Ansprache (m - Männliche | f - Weibliche)");
        p.sendMessage("§e/ausweis rasse [Rasse] §bSetzt deine Rasse");
        p.sendMessage("§e/ausweis aussehen [Beschr.] §bSetzt dein Aussehen (Mind. 4 Wörter)");
        p.sendMessage("§e/ausweis link [Link] §bSetzt den Link zu deiner Charvorstellung im Forum");
        p.sendMessage("§e/ausweis anschauen [Spieler] §bSchau den Ausweis eines Spielers an");
    }

    public static void sendReisepunkHelpMsg(Player p) {
        p.sendMessage("§c--- §e/reisepunk §c---");
        p.sendMessage("§e/reisepunkt setzen [Name] [Dauer] §bSetzt ein Reisepunk");
        p.sendMessage("§e/reisepunkt ziel [Name] §bSetzt das Ziel eines Reisepunkts");
        p.sendMessage("§e/reisepunkt list §bListet alle Reisepunkte auf");
        p.sendMessage("§e/reisepunk löschen [Name] §bEntfernt ein Reisepunkt");
    }

    public static float getYawByBlockFace(BlockFace bf)
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

    public static void sendMessageToPlayersNear(Location loc, int radius, String message) {
        for (Entity nearbyEntity : loc.getNearbyEntities(radius, radius, radius)) {
            if(nearbyEntity instanceof Player) {
                Player p = (Player) nearbyEntity;
                p.sendMessage(message);
            }
        }
    }


    public static int getBackpackID(ItemStack backpack)
    {

        String lore = backpack.getItemMeta().getLore().get(1);
        String idS = lore.replaceAll(".*#", "");

        int id = -1;

        try
        {
            id = Integer.parseInt(idS);
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

        return "§c"+weeks+" Wochen, "+days+" Tage, "+hours+" Stunden, "+minutes+" Minuten und "+seconds+" Sekunden";
    }

    private static final List<Material> carpets = new ArrayList<>();

    public static List<Material> getCarpets() {

        if(carpets.isEmpty()) {

            for (Material value : Material.values()) {
                if(value.toString().contains("CARPET"))
                    carpets.add(value);
            }

            carpets.remove(Material.LEGACY_CARPET);

        }

        return carpets;

    }

    private static final List<Material> noStairs = new ArrayList<>();

    public static List<Material> getNoStairs() {

        if(noStairs.isEmpty()) {

            noStairs.addAll(Arrays.asList(

                    Material.DARK_PRISMARINE_STAIRS, Material.PRISMARINE_STAIRS, Material.PRISMARINE_BRICK_STAIRS,
                    Material.POLISHED_ANDESITE_STAIRS, Material.POLISHED_DIORITE_STAIRS, Material.POLISHED_GRANITE_STAIRS,
                    Material.SMOOTH_RED_SANDSTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS, Material.MOSSY_STONE_BRICK_STAIRS,
                    Material.END_STONE_BRICK_STAIRS, Material.STONE_STAIRS, Material.SMOOTH_SANDSTONE_STAIRS,
                    Material.SMOOTH_QUARTZ_STAIRS, Material.GRANITE_STAIRS, Material.ANDESITE_STAIRS, Material.RED_NETHER_BRICK_STAIRS,
                    Material.DIORITE

            ));

            for (Material value : Material.values()) {
                if(value.toString().contains("STAIRS"))
                    noStairs.add(value);
            }

        }

        return noStairs;
    }
}

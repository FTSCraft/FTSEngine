package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsutils.items.ItemReader;
import de.ftscraft.ftsutils.misc.MiniMsg;
import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class Var {

    public static void sendAusweisMsg(Player p, Ausweis a) {
        p.sendMessage(" ");
        p.sendMessage("§c------ Ausweis von §e" + UUIDFetcher.getName(a.getUuid()) + " §c------");
        p.sendMessage("§cNachname: §e" + a.getLastName());
        p.sendMessage("§cVorname: §e" + a.getFirstName());
        p.sendMessage("§cGeschlecht: §e" + (a.getGender() == Ausweis.Gender.MALE ? "Mann" : a.getGender() == Ausweis.Gender.FEMALE ? "Frau" : "Kein Eintrag"));
        p.sendMessage("§cRasse: §e" + (a.getRace() != null ? a.getRace() : "Kein Eintrag"));
        p.sendMessage("§cAussehen: §e" + (a.getDesc() != null ? a.getDesc() : "Kein Eintrag"));
        p.sendMessage("§cDeckname: §e" + (a.getSpitzname() != null ? a.getSpitzname() : "Kein Eintrag"));
        p.sendMessage("§cGröße: §e" + (a.getHeight() != -1 ? a.getHeight() : "Kein Eintrag"));
        if (a.getForumLink() != null) {
            p.sendMessage(MiniMsg.c("<red>Vorstellung: <yellow><click:open_url:'" + a.getForumLink() + "'>[KLICK]</click></yellow></red>"));
        } else {
            p.sendMessage(MiniMsg.c("<red>Vorstellung: <yellow>[Kein Eintrag]</yellow></red>"));
        }
    }

    public static void sendReisepunkHelpMsg(Player p) {
        p.sendMessage("§c--- §e/reisepunk §c---");
        p.sendMessage("§e/reisepunkt setzen [Name] [Dauer] §bSetzt ein Reisepunk");
        p.sendMessage("§e/reisepunkt ziel [Name] §bSetzt das Ziel eines Reisepunkts");
        p.sendMessage("§e/reisepunkt list §bListet alle Reisepunkte auf");
        p.sendMessage("§e/reisepunk löschen [Name] §bEntfernt ein Reisepunkt");
    }


    public static void sendMessageToPlayersNear(Location loc, int radius, String message) {
        for (Entity nearbyEntity : loc.getNearbyEntities(radius, radius, radius)) {
            if (nearbyEntity instanceof Player p) {
                p.sendMessage(message);
            }
        }
    }

    public static void setBackpackId(ItemStack backpack, int id) {
        ItemReader.addPDC(backpack, "BACKPACK_ID", id, PersistentDataType.INTEGER);
        var lore = backpack.lore();
        lore.set(1, Component.text("ID: " + id)
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.GRAY));
        backpack.lore(lore);
    }

    public static Integer getBackpackID(ItemStack backpack) {
        return ItemReader.getPDC(backpack, "BACKPACK_ID", PersistentDataType.INTEGER);
    }

    public static int getLegacyBackpackID(ItemStack backpack) {

        String lore = backpack.getItemMeta().getLore().get(1);
        String idS = lore.replaceAll(".*#", "");

        int id;

        try {
            id = Integer.parseInt(idS);
        } catch (NumberFormatException e) {
            throw new RuntimeException("We got a backpack that has no PDC and no legacy id. Lore: " + lore);
        }

        return id;
    }

}

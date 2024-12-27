package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            Component click = Component.text("[CLICK]")
                    .color(NamedTextColor.YELLOW)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, a.getForumLink()));
            p.sendMessage(Component.text("Vorstellung: ").color(NamedTextColor.RED).append(click));
        } else {
            Component noEntry = Component.text("[Kein Eintrag]").color(NamedTextColor.YELLOW);
            p.sendMessage(Component.text("Vorstellung: ").color(NamedTextColor.RED).append(noEntry));
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


    public static int getBackpackID(ItemStack backpack) {

        String lore = backpack.getItemMeta().getLore().get(1);
        String idS = lore.replaceAll(".*#", "");

        int id;

        try {
            id = Integer.parseInt(idS);
        } catch (NumberFormatException e) {
            return -1;
        }

        return id;
    }

}

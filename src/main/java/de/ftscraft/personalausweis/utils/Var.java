package de.ftscraft.personalausweis.utils;

import de.ftscraft.personalausweis.main.Engine;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;
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

    public void sendAusweisMsg(Player p, Ausweis a) {
        p.sendMessage(" ");
        p.sendMessage("§c------ Ausweis von §e"+uF.getName(UUID.fromString(a.getUUID()))+" §c------");
        p.sendMessage("§cNachname: §e"+a.getLastName());
        p.sendMessage("§cVorname: §e"+a.getFirstName());
        p.sendMessage("§cGeschlecht: §e"+(a.getGender() == Gender.MALE ? "Mann" : a.getGender() == Gender.FEMALE ? "Frau" : "N/A"));
        p.sendMessage("§cGeburtstag: §e"+a.getBirthdayString());
        p.sendMessage("§cRasse: §e"+(a.getRace() != null ? a.getRace() : "N/A"));
        p.sendMessage("§cNation: §e"+(a.getNation() != null ? a.getNation() : "N/A"));
        p.sendMessage("§cBeschreibung: §e"+(a.getDesc() != null ? a.getDesc() : "N/A"));
        p.sendMessage("§cReligion: §e"+(a.getReligion() != null ? a.getReligion() : "N/A"));
        p.sendMessage("§7ID: #"+a.id);
    }

    public void sendHelpMsg(Player p) {
        p.sendMessage("§c----- §e/ausweis §c-----");
        p.sendMessage("§e/ausweis name [Vorname] [Nachname] §bÄndert deinen Namen und erstellt beim 1. Mal einen Ausweis");
        p.sendMessage("§e/ausweis alter [DD-MM] §bSetzt das Geburtsdatum");
        p.sendMessage("§e/ausweis geschlecht [m/f] §bSetzt das Geschlecht (m - Mann | f - Frau)");
        p.sendMessage("§e/ausweis rasse [Rasse] §bSetzt deine Rasse");
        p.sendMessage("§e/ausweis nation [Nation] §bSetzt deine Nation");
        p.sendMessage("§e/ausweis beschreibung [Beschr.] §bSetzt deine kurze Beschreibung von dir (Mind. 4 Wörter)");
        p.sendMessage("§e/ausweis religion [Religion] §bSetzt deine Religion");
        p.sendMessage("§e/ausweis kopieren [Spielername] §bKopiert den Ausweis eines anderen Spielers - Kosten: 5 Taler");
        p.sendMessage("§6FTSEngine von §5halberfan §6für FTS-Craft");
    }

    public void sendPferdHelpMsg(Player p) {
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

    public void ChatInRoleplay(Player p, String msg) {

    }

    public void ChatInHandel(Player p, String msg) {

    }

    public void ChatInFlüster(Player p, String msg) {

    }

    public void ChatInRufen(Player p, String msg) {

    }

    public float getYawByBlockFace(BlockFace bf) {
        float yaw = 0;

        if(bf == BlockFace.NORTH) {
            return -180;
        } else if(bf == BlockFace.EAST) {
            return -90;
        } else if(bf == BlockFace.SOUTH) {
            return 0;
        } else if(bf == BlockFace.WEST) {
            return 90;
        }

        return yaw;
    }

}

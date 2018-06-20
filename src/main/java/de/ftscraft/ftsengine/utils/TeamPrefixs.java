package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public enum TeamPrefixs
{

    ADMIN("§bAdmin", "ftsengine.admin"),
    MODERATOR("§bModerator", "ftsengine.moderator"),
    HELFER("§bHelder", "ftsengine.helfer"),
    WALKURE("§cWalküre", "ftsengine.walkure"),
    EINHERJER("§cEinherjer", "ftsengine.einherjer"),
    ARCHITEKT("§eArchitekt", "ftsengine.architekt"),
    EHRENBURGER("§eEhrenbürger", "ftsengine.ehrenburger"),
    RAUBER("§9Räuber", "ftsengine.rauber"),
    RICHTER("§9Richter", "ftsengine.richter"),
    MEJSTER("§9Mejster", "ftsengine.mejster"),
    KONIG("§2König", "ftsengine.konig"),
    HERZOG("§2Herzog", "ftsengine.herzog"),
    FURST("§2Fürst", "ftsengine.furst"),
    GRAF("§2Graf", "ftsengine.graf"),
    BURGHERR("§2Burgherr", "ftsengine.burgherr"),
    RITTER("§2Ritter", "ftsengine.ritter"),
    INTENDANT("§2Intendant", "ftsengine.intendant"),
    KURATOR("§2Kurator", "ftsengine.kurator"),
    KAUFMANN("§2Kaufmann", "ftsengine.kaufmann"),
    GILDENHERR("§2Gildenherr", "ftsengine.gildenherr"),
    STADTHERR("§2Stadtherr", "ftsengine.stadtherr"),
    MEISTER("§2Meister", "ftsengine.burgermeister"),
    SIEDLER("§2Siedler", "ftsengine.siedler"),
    VOGT("§6Vogt", "ftsengine.vogt"),
    HEROLD("§6Herold", "ftsengine.herold"),
    KNAPPE("§6Knappe", "ftsengine.knappe"),
    SCHAUSPIELER("§6Schauspieler", "ftsengine.schauspieler"),
    MUSIKER("§6Musiker", "ftsengine.musiker"),
    SCHREIBER("§6Schreiber", "ftsengine.schreiber"),
    SEEFAHRER("§6Seefahrer", "ftsengine.seefahrer"),
    HAFENMEISTER("§6Hafenmeister", "ftsengine.hafenmeister"),
    HANDLER("§6Händler", "ftsengine.handler"),
    BURGER("§6Bürger", "ftsengine.burger"),
    REISENDER("§6Reisender", "");

    String prefix;
    String permission;

    private TeamPrefixs(String prefix, String permission)
    {
        this.prefix = prefix;
        this.permission = permission;
    }

    public String getPermission()
    {
        return permission;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public static TeamPrefixs setPlayerPrefix(Player p) {
        for(TeamPrefixs a : TeamPrefixs.values()) {
            if(p.hasPermission(a.getPermission()))
            {
                p.setPlayerListName(a.getPrefix() + " §7| §r"+p.getName());
                return a;
            }
        }
        p.setPlayerListName(REISENDER.getPrefix() + " §7| §r"+p.getName());
        return REISENDER;
    }



}

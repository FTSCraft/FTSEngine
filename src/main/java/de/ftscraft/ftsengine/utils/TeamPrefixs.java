package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public enum TeamPrefixs
{

    ADMIN("§bAdmin", "ftsengine.admin", "Admin"),
    MODERATOR("§bModerator", "ftsengine.moderator", "Moderator"),
    HELFER("§bHelfer", "ftsengine.helfer", "Helfer"),
    WALKURE("§cWalküre", "ftsengine.walkure", "Walkuere"),
    PREMIUM("§cPremium", "ftsengine.premium", "Premium"),
    ARCHITEKT("§eArchitekt", "ftsengine.architekt", "Architekt"),
    EHRENBURGER("§eEhrenbürger", "ftsengine.ehrenburger", "EBuerger"),
    PAPST("§9Papst","ftsengine.papst", "Papst"),
    RAUBER("§9Söldner", "ftsengine.rauber", "Rauber"),
    RICHTER("§9Richter", "ftsengine.richter", "Richter"),
    MEJSTER("§9Medikus", "ftsengine.mejster", "Mejster"),
    KAUFMANN("§9Kaufmann", "ftsengine.kaufmann", "Kaufmann"),
    BRAUMEISTER("§9Braumeister", "ftsengine.braumeister", "BMeister"),
    KURATOR("§2Kurator", "ftsengine.kurator", "Kurator"),
    KONIG("§2König", "ftsengine.konig", "Konig"),
    HERZOG("§2Herzog", "ftsengine.herzog", "Herzog"),
    FURST("§2Fürst", "ftsengine.furst", "Furst"),
    GRAF("§2Graf", "ftsengine.graf", "Graf"),
    BURGHERR("§2Burgherr", "ftsengine.burgherr", "Burgherr"),
    RITTER("§2Ritter", "ftsengine.ritter", "Ritter"),
    INTENDANT("§2Intendant", "ftsengine.intendant", "Intendant"),
    GILDENHERR("§2Gildenherr", "ftsengine.gildenherr", "Gildenherr"),
    STADTHERR("§2Stadtherr", "ftsengine.stadtherr", "Stadtherr"),
    MEISTER("§2Meister", "ftsengine.meister", "Meister"),
    BURGERMEISTER("§2Bürgermeister","ftsengine.burgermeister", "BMeister"),
    SIEDLER("§2Siedler", "ftsengine.siedler", "Siedler"),
    VOGT("§6Vogt", "ftsengine.vogt", "Vogt"),
    HEROLD("§6Herold", "ftsengine.herold", "Herold"),
    KNAPPE("§6Knappe", "ftsengine.knappe", "Knappe"),
    SCHAUSPIELER("§6Schauspieler", "ftsengine.schauspieler", "SSpieler"),
    MUSIKER("§6Musiker", "ftsengine.musiker", "Musiker"),
    SCHREIBER("§6Schreiber", "ftsengine.schreiber", "Schreiber"),
    SEEFAHRER("§6Seefahrer", "ftsengine.seefahrer", "Seefahrer"),
    HAFENMEISTER("§6Hafenmeister", "ftsengine.hafenmeister", "HMeister"),
    HANDLER("§6Händler", "ftsengine.handler", "Handler"),
    BURGER("§6Bürger", "ftsengine.burger", "Burger"),
    REISENDER("§6Reisender", "", "Reisender");

    String prefix;
    String permission;
    String teamName;
    TeamPrefixs(String prefix, String permission, String teamName)
    {
        this.teamName = teamName;
        this.prefix = prefix;
        this.permission = permission;
    }

    public String getTeamName() {
        return teamName;
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

package de.ftscraft.ftsengine.utils;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public enum TeamPrefixs {

    /*ADMIN("§bAdmin", "§bAdmin", "ftsengine.admin", "Admin"),
    MODERATOR("§bModerator", "§bModeratorin", "ftsengine.moderator", "Moderator"),
    HELFER("§bHelfer", "§bHelferin", "ftsengine.helfer", "Helfer"),
    WALKURE("§cWalküre", "§cWalkürin", "ftsengine.walkure", "Walkuere"),
    PREMIUM("§cPremium", "§cPremium", "ftsengine.premium", "Premium"),
    ARCHITEKT("§eArchitekt", "§eArchitektin", "ftsengine.architekt", "Architekt"),
    EHRENBURGER("§eEhrenbürger", "§eEhrenbürgerin", "ftsengine.ehrenburger", "EBuerger"),
    PAPST("§9Papst", "§9Päpstin", "ftsengine.papst", "Papst"),
    RAUBER("§9Räuber", "§9Räuberin", "ftsengine.rauber", "Rauber"),
    RICHTER("§9Richter", "§9Richterin", "ftsengine.richter", "Richter"),
    MEJSTER("§9Medikus", "§9Medika", "ftsengine.mejster", "Mejster"),
    KAUFMANN("§9Kaufmann", "§9Kauffrau", "ftsengine.kaufmann", "Kaufmann"),
    BRAUMEISTER("§9Braumeister", "§9Braumeisterin", "ftsengine.braumeister", "BMeister"),
    KURATOR("§2Kurator", "§2Kuratorin", "ftsengine.kurator", "Kurator"),
    KONIG("§2König", "§2Königin", "ftsengine.konig", "Konig"),
    HERZOG("§2Herzog", "§2Herzogin", "ftsengine.herzog", "Herzog"),
    FURST("§2Fürst", "§2Fürstin", "ftsengine.furst", "Furst"),
    GRAF("§2Graf", "§2Gräfin", "ftsengine.graf", "Graf"),
    BURGHERR("§2Burgherr", "§2Burgherrin", "ftsengine.burgherr", "Burgherr"),
    RITTER("§2Ritter", "§2Ritterin", "ftsengine.ritter", "Ritter"),
    INTENDANT("§2Intendant", "§2Intendantin", "ftsengine.intendant", "Intendant"),
    GILDENHERR("§2Gildenherr", "§2Gildenherrin", "ftsengine.gildenherr", "Gildenherr"),
    STADTHERR("§2Stadtherr", "§2Stadtherrin", "ftsengine.stadtherr", "Stadtherr"),
    MEISTER("§2Meister", "§2Meisterin", "ftsengine.meister", "Meister"),
    BURGERMEISTER("§2Bürgermeister", "§2Bürgermeisterin", "ftsengine.burgermeister", "BMeister"),
    SIEDLER("§2Siedler", "§2Siedlerin", "ftsengine.siedler", "Siedler"),
    VOGT("§6Vogt", "§6Vögtin", "ftsengine.vogt", "Vogt"),
    HEROLD("§6Herold", "§6Heroldin", "ftsengine.herold", "Herold"),
    KNAPPE("§6Knappe", "§6Knappe", "ftsengine.knappe", "Knappe"),
    SCHAUSPIELER("§6Schauspieler", "§6Schauspielerin", "ftsengine.schauspieler", "SSpieler"),
    MUSIKER("§6Musiker", "§6Musikerin", "ftsengine.musiker", "Musiker"),
    SCHREIBER("§6Schreiber", "§6Schreiberin", "ftsengine.schreiber", "Schreiber"),
    SEEFAHRER("§6Seefahrer", "§6Seefahrerin", "ftsengine.seefahrer", "Seefahrer"),
    HAFENMEISTER("§6Hafenmeister", "§6Hafenmeisterin", "ftsengine.hafenmeister", "HMeister"),
    HANDLER("§6Händler", "§6Händlerin", "ftsengine.handler", "Handler"),
    BURGER("§6Bürger", "§6Bürgerin", "ftsengine.burger", "Burger"),
    REISENDER("§6Reisender", "§6Reisende", "", "Reisender");

    String mprefix;
    String fprefix;
    String permission;
    String teamName;

    TeamPrefixs(String mprefix, String fprefix, String permission, String teamName) {
        this.teamName = teamName;
        this.mprefix = mprefix;
        this.fprefix = fprefix;
        this.permission = permission;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPermission() {
        return permission;
    }

    public String getPrefix(Gender gender) {
        if (gender == null || gender == Gender.MALE || gender == Gender.DIVERS) {
            return mprefix;
        } else if (gender == Gender.FEMALE) {
            return fprefix;
        } else
            return null;
    }

    public static TeamPrefixs getPlayerPrefix(Gender gender, Player p) {

        for (TeamPrefixs a : TeamPrefixs.values()) {
            if (p.hasPermission(a.getPermission())) {
                //p.setPlayerListName(a.getPrefix(gender) + " §7| §r" + p.getName());
                return a;
            }
        }
        //p.setPlayerListName(REISENDER.getPrefix(gender) + " §7| §r" + p.getName());
        return REISENDER;

    }*/


}

package de.ftscraft.ftsengine.commands.ausweis;

import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.misc.MiniMsg;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AusweisMessageFormatter {

    /**
     * Sends an overview of the Ausweis with edit links.
     */
    public void sendAusweisEditMessage(Player player, Ausweis ausweis) {
        MiniMsg.msg(player, "<gray>----- <red>Dein Ausweis \uD83C\uDFB4</red> -----</gray>");
        MiniMsg.msg(player, getFieldEditMessage("Name", "name",
                ausweis.getFirstName() + " " + ausweis.getLastName()));
        MiniMsg.msg(player, getFieldEditMessage("Geschlecht", "geschlecht",
                ausweis.getGender() != null ? (ausweis.getGender() == Ausweis.Gender.MALE ? "Mann" : "Frau") : "Nicht gesetzt"));
        MiniMsg.msg(player, getFieldEditMessage("Rasse", "rasse",
                ausweis.getRace() != null ? ausweis.getRace() : "Nicht gesetzt"));
        MiniMsg.msg(player, getFieldEditMessage("Aussehen", "aussehen",
                ausweis.getDesc() != null ? ausweis.getDesc() : "Nicht gesetzt"));
        MiniMsg.msg(player, getFieldEditMessage("Deckname", "deckname",
                ausweis.getSpitzname() != null ? ausweis.getSpitzname() : "Nicht gesetzt"));
        MiniMsg.msg(player, getFieldEditMessage("Größe", "größe",
                String.valueOf(ausweis.getHeight())));
        MiniMsg.msg(player, getFieldEditMessage("Charaktervorstellung", "link",
                ausweis.getForumLink() != null ? ausweis.getForumLink() : "Nicht gesetzt"));
        MiniMsg.msg(player, "<gray>[<click:run_command:/ausweis wechseln><green>Wechseln ⇔</green></click>] " +
                "[<red><hover:show_text:'<aqua>Spielernamen angeben, dessen Skin beim Spielen dieses Charakters übernommen wird (auch dein eigener).</aqua>'><click:suggest_command:/ausweis skin >Skin ändern ✎</click></red>]</gray>");
    }

    /**
     * Sends the help message.
     */
    public void sendHelpMessage(Player player) {
        MiniMsg.msg(player, Messages.MINI_PREFIX + "<gray>----- <red>/ausweis</red> -----</gray>");
        MiniMsg.msg(player, "<gray>/ausweis neu [Vorname] [Nachname] <red>Erstellt einen neuen Ausweis</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis wechseln <red>Zeigt alle deine Ausweise und ermöglicht das Wechseln</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis name [Vorname] [Nachname] <red>Ändert deinen Namen und erstellt beim 1. Mal einen Ausweis - Mit Unterstrichen könnt ihr Leerzeichen im Namen haben</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis geschlecht [m/f] <red>Setzt die Ansprache (m - Männliche | f - Weibliche)</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis rasse [Ork/Zwerg/Mensch/Elf] <red>Setzt deine Rasse</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis aussehen [Beschr.] <red>Setzt dein Aussehen (Mind. 4 Wörter)</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis größe [Größe in cm] <red>Setzt deine Größe</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis deckname [Deckname] <red>Setzt deinen Decknamen</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis link [Link] <red>Setzt den Link zu deiner Charvorstellung im Forum</red></gray>");
        MiniMsg.msg(player, "<gray>/ausweis anschauen [Spieler] <red>Schau den Ausweis eines Spielers an</red></gray>");
    }

    /**
     * Sends a list of all Ausweise with switch buttons.
     */
    public void sendAusweiseList(Player player, java.util.List<Ausweis> ausweise, Ausweis activeAusweis) {
        player.sendMessage(" ");
        MiniMsg.msg(player, Messages.MINI_PREFIX + "<gray>----- Deine Ausweise -----</gray>");
        MiniMsg.msg(player, Messages.MINI_PREFIX + "<gray>Klicke auf einen Ausweis, um zu diesem zu wechseln:</gray>");
        player.sendMessage(" ");

        for (Ausweis ausweis : ausweise) {
            String message = getAusweisSwitchMessage(ausweis, activeAusweis);
            player.sendMessage(MiniMsg.c(message));
        }

        MiniMsg.msg(player, "<green>  <click:suggest_command:/ausweis neu >[➕ Neu]</click> </green>");
        player.sendMessage(" ");
    }

    /**
     * Sends a list of all Ausweise for a specific player (for admins).
     */
    public void sendPlayerAusweiseList(Player viewer, String playerName, java.util.List<Ausweis> ausweise) {
        viewer.sendMessage(" ");
        MiniMsg.msg(viewer, Messages.MINI_PREFIX + "<gray>----- Ausweise von <yellow>" + playerName + "</yellow> -----</gray>");
        viewer.sendMessage(" ");

        for (Ausweis ausweis : ausweise) {
            String message = getAusweisInfoMessage(ausweis);
            viewer.sendMessage(MiniMsg.c(message));
        }

        viewer.sendMessage(" ");
        MiniMsg.msg(viewer, Messages.MINI_PREFIX + "<gray>Gesamt: <yellow>" + ausweise.size() + "</yellow> Ausweis(e)</gray>");
        viewer.sendMessage(" ");
    }

    // ===== Private helper methods =====

    private String getFieldEditMessage(String field, String subcommand, String value) {
        return "<gray>%s: <red>%s</red> %s</gray>".formatted(field, value, getEditMiniMessage(subcommand));
    }

    private String getEditMiniMessage(String subcommand) {
        return "<yellow><click:suggest_command:/ausweis " + subcommand + " >[✎]</click></yellow>";
    }

    private @NotNull String getAusweisSwitchMessage(Ausweis ausweis, Ausweis activeAusweis) {
        boolean isActive = activeAusweis != null && activeAusweis.getId() == ausweis.getId();

        String partPrefix = isActive ? "<green>  ➤ </green>" : "<gray>  </gray>";
        String partName = isActive ? "<yellow><bold>" + ausweis.getFirstName() + " " + ausweis.getLastName() + "</bold></yellow>"
                : "<white><italic>" + ausweis.getFirstName() + " " + ausweis.getLastName() + "</italic></white>";
        String partAction;
        if (isActive) {
            partAction = "<green>(Aktiv)</green>";
        } else {
            partAction = "<click:run_command:'/ausweis switchausweis " + ausweis.getId() + "'>"
                    + "<hover:show_text:'<yellow>Zu " + ausweis.getFirstName() + " " + ausweis.getLastName() + " wechseln</yellow>'>"
                    + "<aqua>[Wechseln]</aqua></hover></click> " +
                    "<click:run_command:'/ausweis löschen " + ausweis.getId() + "'>" +
                    "<hover:show_text:'<red>Löscht den Ausweis von " + ausweis.getFirstName() + " " + ausweis.getLastName() + "</red>'>" +
                    "<red>[Löschen]</red></hover></click>";
        }

        return partPrefix + partName + " " + partAction;
    }

    private @NotNull String getAusweisInfoMessage(Ausweis ausweis) {
        String name = ausweis.getFirstName() + " " + ausweis.getLastName();
        String gender = ausweis.getGender() != null ? (ausweis.getGender() == Ausweis.Gender.MALE ? "Mann" : "Frau") : "?";
        String race = ausweis.getRace() != null ? ausweis.getRace() : "?";

        return "<gray>  <yellow>ID " + ausweis.getId() + "</yellow>: " +
                "<click:run_command:/ausweis anschauen " + ausweis.getId() + ">" +
                "<hover:show_text:'<green>Klicke um diesen Ausweis anzuschauen</green>'>" +
                "<white>" + name + "</white></hover></click>" +
                " - <aqua>" + gender + "</aqua>, <green>" + race + "</green>, <gold>" + ausweis.getHeight() + "cm</gold></gray>";
    }
}


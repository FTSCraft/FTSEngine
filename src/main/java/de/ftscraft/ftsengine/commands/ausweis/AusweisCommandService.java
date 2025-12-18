package de.ftscraft.ftsengine.commands.ausweis;

import de.ftscraft.ftsengine.feature.roleplay.ausweis.AusweisStorageManager;
import de.ftscraft.ftsengine.feature.roleplay.ausweis.SkinService;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.EngineUser;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsengine.utils.Var;
import de.ftscraft.ftsutils.misc.MiniMsg;
import de.ftscraft.ftsutils.uuidfetcher.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class AusweisCommandService {

    private final Engine plugin;
    private final AusweisValidator validator;
    private final AusweisMessageFormatter formatter;
    private static final double HEIGHT_COOLDOWN = 1000 * 60 * 60; // 1 hour

    public AusweisCommandService(Engine plugin, AusweisMessageFormatter formatter) {
        this.plugin = plugin;
        this.validator = new AusweisValidator();
        this.formatter = formatter;
    }

    // ===== Ausweis creation and management =====

    /**
     * Creates a new Ausweis for a player.
     */
    public void createNewAusweis(Player player, String firstName, String lastName) {
        Ausweis ausweis = new Ausweis(player.getUniqueId());
        ausweis.setFirstName(firstName);
        ausweis.setLastName(lastName);
        plugin.addAusweis(ausweis);

        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Du hast erfolgreich einen neuen Ausweis mit dem Namen <yellow>"
                + firstName + " " + lastName + "</yellow> erstellt!</green>");
    }

    public void sendAusweisDeleteConfirmation(Player player, int ausweisId) {
        MiniMsg.msg(player, Messages.MINI_PREFIX +
                "<gray>Bist du sicher, dass du diesen Ausweis löschen möchtest? " +
                "Dies kann nicht rückgängig gemacht werden! " +
                "[<red><hover:show_text:'<aqua>Ja, ich möchte diesen Ausweis löschen.</aqua>'>" +
                "<click:run_command:/ausweis löschen " + ausweisId + " confirm>Löschen ✖</click></red>] ");
    }

    public void deleteAusweis(Player player, int ausweisId) {
        AusweisStorageManager ausweisStorageManager = plugin.getDatabaseHandler()
                .getAusweisStorageManager();

        Ausweis targetAusweis = ausweisStorageManager.getAusweisById(ausweisId);

        if (!validator.ausweisExists(targetAusweis)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Dieser Ausweis existiert nicht!</red>");
            return;
        }

        if (!validator.isAusweisOwner(targetAusweis, player.getUniqueId())) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Dieser Ausweis gehört dir nicht!</red>");
            return;
        }

        if (plugin.getAusweis(player).getId() == ausweisId) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Du kannst deinen aktiven Ausweis nicht löschen.</red>");
            return;
        }

        ausweisStorageManager.deleteAusweis(targetAusweis);
        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Dein Ausweis von <yellow>"
                + targetAusweis.getFirstName() + " " + targetAusweis.getLastName() + "</yellow> wurde erfolgreich gelöscht!</green>");
    }

    /**
     * Changes the name of an existing Ausweis or creates a new one.
     */
    public void setName(Player player, String firstName, String lastName) {
        if (plugin.hasAusweis(player)) {
            Ausweis ausweis = plugin.getAusweis(player);
            ausweis.setFirstName(firstName);
            ausweis.setLastName(lastName);
            plugin.saveAusweis(ausweis);
        } else {
            createNewAusweis(player, firstName, lastName);
            return;
        }
        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Name wurde erfolgreich auf <yellow>"
                + firstName + " " + lastName + "</yellow> gesetzt!</green>");
    }

    /**
     * Sets the gender of an Ausweis.
     */
    public void setGender(Player player, String genderStr) {
        if (!ensureHasAusweis(player)) return;

        if (!validator.isValidGender(genderStr)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Bitte benutze den Befehl so: <yellow>/ausweis geschlecht [m/f]</yellow></red>");
            return;
        }

        Ausweis.Gender gender = genderStr.equalsIgnoreCase("m") ? Ausweis.Gender.MALE : Ausweis.Gender.FEMALE;
        Ausweis ausweis = plugin.getAusweis(player);
        ausweis.setGender(gender);
        plugin.saveAusweis(ausweis);

        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Geschlecht wurde erfolgreich auf <yellow>"
                + (gender == Ausweis.Gender.MALE ? "Mann" : "Frau") + "</yellow> gesetzt!</green>");
    }

    /**
     * Sets the race of an Ausweis.
     */
    public void setRace(Player player, String raceStr) {
        if (!ensureHasAusweis(player)) return;

        if (!validator.isValidRace(raceStr)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Bitte benutze den Befehl so: <yellow>/ausweis rasse [Ork/Zwerg/Mensch/Elf]</yellow></red>");
            return;
        }

        String race = validator.normalizeRace(raceStr);
        Ausweis ausweis = plugin.getAusweis(player);
        ausweis.setRace(race);
        plugin.saveAusweis(ausweis);

        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Rasse wurde erfolgreich auf <yellow>" + race + "</yellow> gesetzt!</green>");
    }

    /**
     * Sets the appearance description of an Ausweis.
     */
    public void setAppearance(Player player, String description) {
        if (!ensureHasAusweis(player)) return;

        if (!validator.isValidAppearance(description)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Bitte benutze den Befehl so: <yellow>/ausweis aussehen [Aussehen (mind. "
                    + validator.getMinAppearanceWords() + " Wörter)]</yellow></red>");
            return;
        }

        Ausweis ausweis = plugin.getAusweis(player);
        ausweis.setDesc(description);
        plugin.saveAusweis(ausweis);

        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Aussehen wurde erfolgreich auf <yellow>" + description + "</yellow> gesetzt!</green>");
    }

    /**
     * Sets the height of an Ausweis with cooldown check.
     */
    public void setHeight(Player player, int height) {
        if (!ensureHasAusweis(player)) return;

        if (!validator.isValidHeight(height)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Die Größe muss zwischen <yellow>"
                    + validator.getMinHeight() + "</yellow> und <yellow>" + validator.getMaxHeight() + "</yellow> cm liegen.</red>");
            return;
        }

        Ausweis ausweis = plugin.getAusweis(player);

        // Cooldown check
        if (!canChangeHeight(player, ausweis)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Du darfst deine Größe jede Stunde ändern</red>");
            return;
        }

        ausweis.setHeight(height);
        ausweis.setLastHeightChange(System.currentTimeMillis());
        plugin.saveAusweis(ausweis);

        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Größe wurde erfolgreich auf <yellow>" + height + "</yellow> gesetzt!</green>");

        // Apply scale attribute
        applyHeightScale(player, height);
    }

    /**
     * Sets the link to the character presentation.
     */
    public void setForumLink(Player player, String link) {
        if (!ensureHasAusweis(player)) return;

        if (!validator.isValidUrl(link)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Das ist kein gültiger Link! Bitte überprüfe den Link und versuche es erneut.</red>");
            return;
        }

        if (!validator.isForumUrl(link)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Der Link muss mit unserer URL des Forums anfangen! ("
                    + validator.getForumUrlPrefix() + ")</red>");
            return;
        }

        Ausweis ausweis = plugin.getAusweis(player);
        ausweis.setForumLink(link);
        plugin.saveAusweis(ausweis);

        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Charaktervorstellung wurde erfolgreich auf <yellow>" + link + "</yellow> gesetzt!</green>");
    }

    /**
     * Sets the cover name of an Ausweis.
     */
    public void setCoverName(Player player, String coverName) {
        if (!ensureHasAusweis(player)) return;

        String normalized = coverName.replace("_", " ");
        Ausweis ausweis = plugin.getAusweis(player);
        ausweis.setSpitzname(normalized);
        plugin.saveAusweis(ausweis);

        MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Du hast deinen Decknamen als <yellow>" + normalized + "</yellow> gesetzt!</green>");
    }

    // ===== Skin management =====

    /**
     * Changes the player's skin asynchronously.
     */
    public void changeSkin(Player player, String targetName) {
        if (!ensureHasAusweis(player)) return;

        MiniMsg.msg(player, Messages.MINI_PREFIX + "<gray>Dein Skin wird nun geändert, dies kann einen Moment dauern...</gray>");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            UUID targetUUID = UUIDFetcher.getUUID(targetName);
            SkinService.SkinData skinData = SkinService.getSkinData(targetUUID);

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (skinData == null) {
                    MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Der angegebene Spielername ist ungültig oder der Spieler existiert nicht!</red>");
                    return;
                }

                Ausweis ausweis = plugin.getAusweis(player);
                if (ausweis == null) {
                    MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Dein Ausweis konnte nicht gefunden werden!</red>");
                    return;
                }

                ausweis.setSkinData(skinData);
                plugin.saveAusweis(ausweis);
                ausweis.applySkinToPlayer(player);

                MiniMsg.msg(player, Messages.MINI_PREFIX + "<green>Dein Skin wurde erfolgreich auf den von <yellow>"
                        + targetName + "</yellow> geändert!</green>");
            });
        });
    }

    // ===== Ausweis display =====

    /**
     * Displays the player's own Ausweis.
     */
    public void showOwnAusweis(Player player) {
        if (!plugin.hasAusweis(player)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Du hast keinen Ausweis!</red>");
            return;
        }
        Var.sendAusweisMsg(player, plugin.getAusweis(player));
    }

    /**
     * Displays another player's Ausweis.
     */
    public void showOtherAusweis(Player viewer, String targetName) {
        UUID targetUUID = UUIDFetcher.getUUID(targetName);
        Ausweis ausweis = plugin.getAusweis(targetUUID);

        if (ausweis != null) {
            Var.sendAusweisMsg(viewer, ausweis);
        } else {
            MiniMsg.msg(viewer, Messages.MINI_PREFIX + "<red>Dieser Spieler hat keinen Ausweis!</red>");
        }
    }

    /**
     * Displays an Ausweis by ID (for admins with list permission).
     */
    public void showAusweisByID(Player viewer, int ausweisId) {
        AusweisStorageManager storageManager = plugin.getDatabaseHandler().getAusweisStorageManager();
        Ausweis ausweis = storageManager.getAusweisById(ausweisId);

        if (ausweis != null) {
            Var.sendAusweisMsg(viewer, ausweis);
        } else {
            MiniMsg.msg(viewer, Messages.MINI_PREFIX + "<red>Ein Ausweis mit der ID <yellow>" + ausweisId + "</yellow> existiert nicht!</red>");
        }
    }

    /**
     * Returns all Ausweise of a player.
     */
    public List<Ausweis> getPlayerAusweise(Player player) {
        return plugin.getDatabaseHandler()
                .getAusweisStorageManager()
                .getAusweiseByUUID(player.getUniqueId());
    }

    /**
     * Returns the active Ausweis of a player.
     */
    public Ausweis getActiveAusweis(Player player) {
        return plugin.getAusweis(player);
    }

    public void sendSwitchAusweisList(Player player) {
        List<Ausweis> ausweise = getPlayerAusweise(player);

        if (ausweise == null || ausweise.isEmpty()) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Du hast keine Ausweise! Erstelle einen <blue><click:suggest_command:/ausweis neu >[hier]</click></blue>");
            return;
        }

        Ausweis activeAusweis = getActiveAusweis(player);
        formatter.sendAusweiseList(player, ausweise, activeAusweis);
    }

    /**
     * Switches the player's active Ausweis to the one with the given ID.
     * @return true if the switch was successful, false otherwise
     */
    public boolean switchAusweis(Player player, int ausweisId) {
        Ausweis targetAusweis = plugin.getDatabaseHandler()
                .getAusweisStorageManager()
                .getAusweisById(ausweisId);

        if (!validator.ausweisExists(targetAusweis)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Dieser Ausweis existiert nicht!</red>");
            return false;
        }

        if (!validator.isAusweisOwner(targetAusweis, player.getUniqueId())) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Dieser Ausweis gehört dir nicht!</red>");
            return false;
        }

        // Ausweis wechseln
        EngineUser user = plugin.getPlayer().get(player.getUniqueId());
        if (user == null) {
            user = plugin.getDatabaseHandler().getUserStorageManager().getOrCreateUser(player.getUniqueId());
        }

        if (user != null) {
            user.setActiveAusweis(targetAusweis);
            plugin.getDatabaseHandler().getUserStorageManager().saveUser(user);

            sendSwitchAusweisList(player);
            targetAusweis.applySkinToPlayer(player);
            applyHeightScale(player, targetAusweis.getHeight());
            return true;
        } else {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Es ist ein Fehler aufgetreten!</red>");
            return false;
        }
    }

    // ===== Cooldown management =====

    /**
     * Resets the height cooldown for all players.
     */
    public int resetAllHeightCooldowns() {
        int count = 0;
        for (Ausweis ausweis : plugin.getDatabaseHandler().getAusweisStorageManager().getAusweisCache().values()) {
            ausweis.setLastHeightChange(0);
            plugin.saveAusweis(ausweis);
            count++;
        }
        return count;
    }

    /**
     * Resets the height cooldown for a specific player.
     */
    public boolean resetHeightCooldown(Player target) {
        if (!plugin.hasAusweis(target)) {
            return false;
        }

        Ausweis ausweis = plugin.getAusweis(target);
        ausweis.setLastHeightChange(0);
        plugin.saveAusweis(ausweis);
        return true;
    }

    /**
     * Notifies all online players about the cooldown reset.
     */
    public void notifyAllOnlinePlayersAboutCooldownReset() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (plugin.hasAusweis(onlinePlayer)) {
                MiniMsg.msg(onlinePlayer, Messages.MINI_PREFIX +
                        "<gray>Dein Größen-Cooldown wurde von einem Admin zurückgesetzt. " +
                        "Du kannst deine Größe jetzt wieder ändern!</gray>");
            }
        }
    }

    /**
     * Lists all Ausweise of a specific player.
     */
    public void listPlayerAusweise(Player sender, String targetPlayerName) {
        UUID targetUUID;
        try {
            targetUUID = UUIDFetcher.getUUID(targetPlayerName);
        } catch (Exception e) {
            MiniMsg.msg(sender, Messages.MINI_PREFIX + "<red>Spieler <yellow>" + targetPlayerName + "</yellow> wurde nicht gefunden!</red>");
            return;
        }

        if (targetUUID == null) {
            MiniMsg.msg(sender, Messages.MINI_PREFIX + "<red>Spieler <yellow>" + targetPlayerName + "</yellow> wurde nicht gefunden!</red>");
            return;
        }

        AusweisStorageManager storageManager = plugin.getDatabaseHandler().getAusweisStorageManager();
        List<Ausweis> ausweise = storageManager.getAusweiseByUUID(targetUUID);

        if (ausweise == null || ausweise.isEmpty()) {
            MiniMsg.msg(sender, Messages.MINI_PREFIX + "<red>Spieler <yellow>" + targetPlayerName + "</yellow> hat keine Ausweise!</red>");
            return;
        }

        formatter.sendPlayerAusweiseList(sender, targetPlayerName, ausweise);
    }

    // ===== Private helper methods =====

    private boolean ensureHasAusweis(Player player) {
        if (!plugin.hasAusweis(player)) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Du hast noch keinen Ausweis! Erstelle einen mit <yellow>/ausweis neu [Vorname] [Nachname]</yellow></red>");
            return false;
        }
        return true;
    }

    private boolean canChangeHeight(Player player, Ausweis ausweis) {
        if (player.hasPermission("ftssurvival.bypass") || player.hasPermission("ftsengine.mod")) {
            return true;
        }
        return ausweis.getLastHeightChange() + HEIGHT_COOLDOWN < System.currentTimeMillis();
    }

    private void applyHeightScale(Player player, int height) {
        AttributeInstance scaleAttr = player.getAttribute(Attribute.SCALE);
        if (scaleAttr == null) {
            MiniMsg.msg(player, Messages.MINI_PREFIX + "<red>Da ist was schiefgelaufen.</red>");
            return;
        }
        scaleAttr.setBaseValue(height / 200d);
    }
}


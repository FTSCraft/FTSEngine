package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.feature.brett.Brett;
import de.ftscraft.ftsengine.feature.brett.BrettNote;
import de.ftscraft.ftsengine.feature.items.instruments.Instrument;
import de.ftscraft.ftsengine.feature.items.instruments.SimpleInstrument;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.EngineUser;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

public class CMDftsengine implements CommandExecutor {

    final Engine plugin;

    public CMDftsengine(Engine plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(
                        plugin.getCommand("ftsengine"),
                        "tried registering ftsengine command but does exist")
                .setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (args.length > 0) {
            // Versteckter Command zum Wechseln von Ausweisen
            if (args[0].equals("_switchausweis")) {
                if (!(cs instanceof Player p)) {
                    return true;
                }

                if (args.length < 2) {
                    return true;
                }

                int ausweisId;

                try {
                    ausweisId = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return true;
                }

                Ausweis targetAusweis = plugin.getDatabaseHandler()
                        .getAusweisStorageManager()
                        .getAusweisById(ausweisId);

                if (targetAusweis == null) {
                    p.sendPlainMessage(Messages.PREFIX + "§cDieser Ausweis existiert nicht!");
                    return true;
                }

                // Prüfen ob der Ausweis dem Spieler gehört
                if (!targetAusweis.getUuid().equals(p.getUniqueId())) {
                    p.sendPlainMessage(Messages.PREFIX + "§cDieser Ausweis gehört dir nicht!");
                    return true;
                }

                // Ausweis wechseln
                de.ftscraft.ftsengine.main.EngineUser user = plugin.getPlayer().get(p.getUniqueId());
                if (user == null) {
                    user = plugin.getDatabaseHandler().getUserStorageManager().getOrCreateUser(p.getUniqueId());
                }

                if (user != null) {
                    user.setActiveAusweis(targetAusweis);
                    plugin.getDatabaseHandler().getUserStorageManager().saveUser(user);

                    p.sendPlainMessage(Messages.PREFIX + "§aDu hast zu dem Ausweis von §e" +
                            targetAusweis.getFirstName() + " " + targetAusweis.getLastName() + " §agewechselt!");
                } else {
                    p.sendPlainMessage(Messages.PREFIX + "§cEs ist ein Fehler aufgetreten!");
                }

                return true;
            }

            // Konvertiere alte YAML-Ausweise zu neuer DB-Struktur
            if (args[0].equalsIgnoreCase("convertoldausweise")) {
                if (!cs.hasPermission("ftsengine.admin")) {
                    cs.sendMessage(Messages.PREFIX + "§cKeine Berechtigung!");
                    return true;
                }

                convertOldAusweise(cs);
                return true;
            }

            if (args[0].equals("debug")) {
                if (args[1].equals("instrument")) {
                    Player p = (Player) cs;
                    Instrument instrument = new SimpleInstrument(Sound.BLOCK_NOTE_BLOCK_GUITAR, "Gitarre");
                    p.openInventory(instrument.getInventory());
                }
            } else if (args[0].equals("reload")) {
                if (!cs.hasPermission("ftsengine.reload")) {
                    return true;
                }
                // Config wird automatisch aus der JSON-Datei geladen
                Engine.getInstance().getStorage().loadStorages(de.ftscraft.ftsengine.utils.EngineConfig.class);
                cs.sendMessage(Messages.PREFIX + "Config neu geladen");
            }
        }

        if (args.length == 4) {
            if (args[0].equals("brett")) {
                if (args[1].equals("delete")) {
                    String idS = args[2];
                    String name = args[3].replace("_", " ");

                    int id = Integer.parseInt(idS);

                    Brett brett = null;

                    for (Brett value : plugin.bretter.values()) {
                        if (value.getName().equals(name)) {
                            brett = value;
                            break;
                        }
                    }

                    if (brett == null) {
                        cs.sendMessage(Messages.PREFIX + "Da hat was nicht geklappt! Ich habe das Brett nicht gefunden");
                        return true;
                    }

                    BrettNote note = null;
                    for (BrettNote notes : brett.getNotes()) {
                        if (notes.getId() == id) {
                            note = notes;
                            break;
                        }
                    }

                    if (note == null) {
                        cs.sendMessage(Messages.PREFIX + "Diese Notiz wurde nicht gefunden.");
                        return true;
                    }

                    if (note.getCreator().equals(cs.getName()) || cs.hasPermission("ftsengine.brett.delete")) {
                        note.remove();
                        cs.sendMessage(Messages.PREFIX + "Die Notiz wurde entfernt.");
                    } else {
                        cs.sendMessage(Messages.PREFIX + "Du bist nicht dazu in der Lage diese Notiz zu löschen. Tut mir leid, aber so sind die Regeln");
                    }
                    return true;
                }
            }

        }

        return false;
    }

    /**
     * Konvertiert alte YAML-basierte Ausweise in die neue Datenbank-Struktur
     */
    private void convertOldAusweise(CommandSender sender) {
        sender.sendMessage(Messages.PREFIX + "§eStarte Konvertierung der alten Ausweise...");

        File ausweiseFolder = new File(plugin.getDataFolder(), "ausweise");

        if (!ausweiseFolder.exists() || !ausweiseFolder.isDirectory()) {
            sender.sendMessage(Messages.PREFIX + "§cKein 'ausweise' Ordner gefunden! Nichts zu konvertieren.");
            return;
        }

        File[] files = ausweiseFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (files == null || files.length == 0) {
            sender.sendMessage(Messages.PREFIX + "§cKeine alten Ausweis-Dateien gefunden!");
            return;
        }

        sender.sendMessage(Messages.PREFIX + "§7Gefunden: §e" + files.length + " §7alte Ausweis-Dateien");

        int converted = 0;
        int skipped = 0;
        int errors = 0;

        for (File file : files) {
            try {
                String fileName = file.getName().replace(".yml", "");
                UUID uuid;

                try {
                    uuid = UUID.fromString(fileName);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Messages.PREFIX + "§cÜberspringe ungültige Datei: §e" + file.getName());
                    errors++;
                    continue;
                }

                // Lade alte YAML-Daten
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

                // Prüfe ob bereits ein Ausweis in der DB existiert
                java.util.List<Ausweis> existingAusweise = plugin.getDatabaseHandler()
                        .getAusweisStorageManager()
                        .getAusweiseByUUID(uuid);

                if (existingAusweise != null && !existingAusweise.isEmpty()) {
                    sender.sendMessage(Messages.PREFIX + "§7Überspringe §e" + fileName + " §7(bereits in DB vorhanden)");
                    skipped++;
                    continue;
                }

                // Erstelle neuen Ausweis
                Ausweis ausweis = new Ausweis(uuid);

                // Übertrage Daten
                String firstName = cfg.getString("firstName");
                String lastName = cfg.getString("lastName");
                String spitzname = cfg.getString("spitzname");
                String genderStr = cfg.getString("gender");
                String race = cfg.getString("race");
                String desc = cfg.getString("desc");
                int height = cfg.getInt("height", 200);
                String link = cfg.getString("link");
                double lastHeightChange = cfg.getDouble("lastHeightChange", 0);

                if (firstName != null) ausweis.setFirstName(firstName);
                if (lastName != null) ausweis.setLastName(lastName);
                if (spitzname != null) ausweis.setSpitzname(spitzname);
                if (race != null) ausweis.setRace(race);
                if (desc != null) ausweis.setDesc(desc);
                if (link != null) ausweis.setForumLink(link);
                ausweis.setHeight(height);
                ausweis.setLastHeightChange(lastHeightChange);

                // Gender konvertieren
                if (genderStr != null) {
                    try {
                        Ausweis.Gender gender = Ausweis.Gender.valueOf(genderStr);
                        ausweis.setGender(gender);
                    } catch (IllegalArgumentException e) {
                        // Ignoriere ungültige Gender-Werte
                    }
                }

                // Speichere in Datenbank
                plugin.getDatabaseHandler().getAusweisStorageManager().saveAusweis(ausweis);

                // Setze als aktiven Ausweis für den Spieler
                EngineUser user = plugin.getDatabaseHandler().getUserStorageManager().getOrCreateUser(uuid);
                if (user != null && user.getActiveAusweis() == null) {
                    user.setActiveAusweis(ausweis);
                    plugin.getDatabaseHandler().getUserStorageManager().saveUser(user);
                }

                converted++;
                sender.sendMessage(Messages.PREFIX + "§a✓ Konvertiert: §e" + firstName + " " + lastName + " §7(" + fileName + ")");

            } catch (Exception e) {
                sender.sendMessage(Messages.PREFIX + "§cFehler bei Datei §e" + file.getName() + "§c: " + e.getMessage());
                plugin.getLogger().severe("Fehler beim Konvertieren von " + file.getName() + ": " + e.getMessage());
                errors++;
            }
        }

        // Zusammenfassung
        sender.sendMessage(Messages.PREFIX + "§7§m-----------------------------------");
        sender.sendMessage(Messages.PREFIX + "§eKonvertierung abgeschlossen!");
        sender.sendMessage(Messages.PREFIX + "§a✓ Erfolgreich konvertiert: §e" + converted);
        sender.sendMessage(Messages.PREFIX + "§7⊘ Übersprungen (bereits vorhanden): §e" + skipped);
        sender.sendMessage(Messages.PREFIX + "§c✗ Fehler: §e" + errors);
        sender.sendMessage(Messages.PREFIX + "§7§m-----------------------------------");

        // Optional: Backup-Hinweis
        if (converted > 0) {
            sender.sendMessage(Messages.PREFIX + "§7Hinweis: Die alten YAML-Dateien wurden nicht gelöscht.");
            sender.sendMessage(Messages.PREFIX + "§7Du kannst den 'ausweise' Ordner als Backup behalten oder manuell löschen.");
        }
    }

}

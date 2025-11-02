package de.ftscraft.ftsengine.signs;

import com.jeff_media.morepersistentdatatypes.DataType;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsutils.FTSUtils;
import de.ftscraft.ftsutils.items.ItemBuilder;
import de.ftscraft.ftsutils.items.ItemReader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class TeachingBoardManager {
    private static final Map<Player, Sign> editingPlayers = new HashMap<>();
    public static void create(Player creator, Sign sign) {
        List<String> owners = new ArrayList<>();
        owners.add(creator.getUniqueId().toString());
        List<String> lines = new ArrayList<>();
        save(sign, new TeachingBoard(lines, owners));
    }

    public static TeachingBoard fetch(Sign sign) {
        if(!isTeachingBoard(sign)) {
            return null;
        }
        String[] lines = fetchPDCList(sign, "teaching-board-lines");
        String[] owners = fetchPDCList(sign, "teaching-board-owners");

        return new TeachingBoard(new ArrayList<>(Arrays.asList(lines)), new ArrayList<>(Arrays.asList(owners)));
    }

    public static void save(Sign sign, TeachingBoard teachingBoard) {
        writePDCList(sign, "teaching-board-owners", teachingBoard.getOwners().toArray(new String[0]));
        writePDCList(sign, "teaching-board-lines", teachingBoard.getLines().toArray(new String[0]));
        sign.update(true, false);
    }

    public static void copyPDCToItemStack(Sign sign, ItemStack itemStack) {
        ItemReader.addPDC(itemStack, "teaching-board-lines", fetchPDCList(sign, "teaching-board-lines"), DataType.STRING_ARRAY);
    }

    public static boolean copyPDCToSign(Player player, ItemStack itemStack, Sign sign) {
        String[] lines = ItemReader.getPDC(itemStack, "teaching-board-lines", DataType.STRING_ARRAY);
        String[] owners = new String[]{player.getUniqueId().toString()};
        if(lines == null) {
            return false;
        }
        TeachingBoard teachingBoard = new TeachingBoard(new ArrayList<>(Arrays.asList(lines)), new ArrayList<>(Arrays.asList(owners)));
        save(sign, teachingBoard);
        return true;
    }

    public static boolean near(Sign sign, Location location, int radius) {
        return location.distance(sign.getLocation()) <= radius;
    }

    public static boolean mainOwner(Sign sign, Player player) {
        TeachingBoard teachingBoard = fetch(sign);
        if(teachingBoard == null) {
            return false;
        }
        if(teachingBoard.getOwners().isEmpty()) {
            return false;
        }
        return teachingBoard.getOwners().get(0).equalsIgnoreCase(player.getUniqueId().toString());
    }

    public static void showLines(Player player, Sign sign, boolean editMode, boolean saveNeeded) {
        Bukkit.getScheduler().runTaskLater(Engine.getInstance(), () -> {
            TeachingBoard teachingBoard = fetch(sign);
            if(teachingBoard == null) {
                return;
            }
            showLines(player, teachingBoard, editMode, saveNeeded);
        }, 2L);
    }

    public static void showLines(Player player, TeachingBoard teachingBoard, boolean editMode, boolean saveNeeded) {
        if(teachingBoard == null) {
            return;
        }
        if(editMode) {
            player.sendMessage("§7_______ §6Lehrtafel Bearbeiten §7_______");
            player.sendMessage(" ");

            if(teachingBoard.getLines().isEmpty()) {
                player.sendMessage("§fKein Eintrag §7(Füge über die 'Add' Schaltfläche Zeilen hinzu)");
            } else {
                for(int i = 0; i < teachingBoard.getLines().size(); i++) {
                    Component lineIndex = Component.text("#" + i)
                            .color(NamedTextColor.WHITE)
                            .hoverEvent(HoverEvent.showText(Component.text("Index der aktuellen Zeile")));
                    Component lineInsertBtn = Component.text("⤵")
                            .color(NamedTextColor.AQUA)
                            .decorate(TextDecoration.BOLD)
                            .hoverEvent(HoverEvent.showText(Component.text("Klicke zum Einfügen einer Zeile, an dieser Position")))
                            .clickEvent(ClickEvent.runCommand("/lehrtafel insert " + i));
                    Component lineEditBtn = Component.text("✏")
                            .color(NamedTextColor.GOLD)
                            .decorate(TextDecoration.BOLD)
                            .hoverEvent(HoverEvent.showText(Component.text("Klicke zum Bearbeiten dieser Zeile")))
                            .clickEvent(ClickEvent.runCommand("/lehrtafel edit " + i));
                    Component lineDeleteBtn = Component.text("🚫")
                            .color(NamedTextColor.RED)
                            .decorate(TextDecoration.BOLD)
                            .hoverEvent(HoverEvent.showText(Component.text("Klicke zum Löschen dieser Zeile")))
                            .clickEvent(ClickEvent.runCommand("/lehrtafel delete " + i));
                    Component editModeLine = Component.empty()
                            .append(lineIndex)
                            .append(Component.space())
                            .append(lineInsertBtn)
                            .append(lineEditBtn)
                            .append(lineDeleteBtn)
                            .append(Component.space())
                            .append(LegacyComponentSerializer.legacyAmpersand().deserialize(teachingBoard.getLines().get(i)));
                    player.sendMessage(editModeLine);
                }
            }
            player.sendMessage(" ");

            Component addBtn = Component.text("➕ Neue Zeile")
                    .color(NamedTextColor.GOLD)
                    .decorate(TextDecoration.BOLD)
                    .hoverEvent(HoverEvent.showText(Component.text("Klicke zum Hinzufügen einer Zeile")))
                    .clickEvent(ClickEvent.runCommand("/lehrtafel add"));
            Component saveBtn = Component.text("✅ Speichern & Schließen")
                    .color(NamedTextColor.GREEN)
                    .decorate(TextDecoration.UNDERLINED)
                    .hoverEvent(HoverEvent.showText(Component.text("Klicke um die Änderungen zu speichern")))
                    .clickEvent(ClickEvent.runCommand("/lehrtafel save"));
            Component cancelBtn = Component.text("♻ Verwerfen")
                    .color(NamedTextColor.RED)
                    .decorate(TextDecoration.BOLD)
                    .hoverEvent(HoverEvent.showText(Component.text("Klicke um die Änderungen zu verwerfen")))
                    .clickEvent(ClickEvent.runCommand("/lehrtafel cancel"));
            Component bottomEditLine = Component.empty()
                    .append(addBtn);

            if(saveNeeded) {
                bottomEditLine = bottomEditLine
                        .append(Component.space())
                        .append(saveBtn);
            }

            player.sendMessage(bottomEditLine);
            if(saveNeeded) {
                player.sendMessage(cancelBtn);
            }
            return;
        }
        for(String line : teachingBoard.getLines()) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(line));
        }
    }

    public static void destroy(Sign sign) {
        if(!isTeachingBoard(sign)) {
            return;
        }
        sign.getPersistentDataContainer().remove(new NamespacedKey(FTSUtils.getInstance(), "teaching-board-owners"));
        sign.getPersistentDataContainer().remove(new NamespacedKey(FTSUtils.getInstance(), "teaching-board-lines"));
        sign.getPersistentDataContainer().remove(ItemBuilder.getSignKey());

        for(int i = 0; i < 4; i++) {
            sign.setLine(i, "");
        }

        sign.update(true, false);

        for(Map.Entry<Player, Sign> signEdits : editingPlayers.entrySet()) {
            if(signEdits.getValue().equals(sign)) {
                editingPlayers.remove(signEdits.getKey());
            }
        }
    }

    public static Map<Player, Sign> getEditingPlayers() {
        return editingPlayers;
    }

    public static boolean isTeachingBoard(Sign sign) {
        return "teaching-board".equals(sign.getPersistentDataContainer().get(ItemBuilder.getSignKey(), PersistentDataType.STRING));
    }

    public static void setLine(Sign sign, String line, int index) {
        TeachingBoard teachingBoard = fetch(sign);
        if(teachingBoard == null) {
            return;
        }
        if(index < 0 || index > teachingBoard.getLines().size()) {
            return;
        }
        if("%space%".equalsIgnoreCase(line)) {
            teachingBoard.getLines().set(index, " ");
        } else {
            teachingBoard.getLines().set(index, line);
        }

        save(sign, teachingBoard);
    }

    public static void insertLine(Sign sign, String line, int index) {
        TeachingBoard teachingBoard = fetch(sign);
        if(teachingBoard == null) {
            return;
        }
        if(index < 0 || index > teachingBoard.getLines().size()) {
            return;
        }
        if("%space%".equalsIgnoreCase(line)) {
            teachingBoard.getLines().add(index, " ");
        } else {
            teachingBoard.getLines().add(index, line);
        }

        save(sign, teachingBoard);
    }

    public static void addLine(Sign sign, String line) {
        TeachingBoard teachingBoard = fetch(sign);
        if(teachingBoard == null) {
            return;
        }
        if("%space%".equalsIgnoreCase(line)) {
            teachingBoard.getLines().add(" ");
        } else {
            teachingBoard.getLines().add(line);
        }

        save(sign, teachingBoard);
    }

    public static void removeLine(Sign sign, int index) {
        TeachingBoard teachingBoard = fetch(sign);
        if(teachingBoard == null) {
            return;
        }

        teachingBoard.getLines().remove(index);
        save(sign, teachingBoard);
    }

    public static void addOwner(Sign sign, String newOwnerUUID) {
        TeachingBoard teachingBoard = fetch(sign);
        if(teachingBoard == null) {
            return;
        }

        if(teachingBoard.getOwners().contains(newOwnerUUID)) {
            return;
        }
        teachingBoard.getOwners().add(newOwnerUUID);
        save(sign, teachingBoard);
    }

    public static void removeOwner(Sign sign, String ownerUUID) {
        TeachingBoard teachingBoard = fetch(sign);
        if(teachingBoard == null) {
            return;
        }

        if(!teachingBoard.getOwners().contains(ownerUUID)
            || teachingBoard.getOwners().get(0).equalsIgnoreCase(ownerUUID)
            || teachingBoard.getOwners().size() == 1) {
            return;
        }
        teachingBoard.getOwners().remove(ownerUUID);
        save(sign, teachingBoard);
    }

    private static String[] fetchPDCList(Sign sign, String key) {
        return sign.getPersistentDataContainer().get(new NamespacedKey(FTSUtils.getInstance(), key), DataType.STRING_ARRAY);
    }

    private static void writePDCList(Sign sign, String key, String[] list) {
        sign.getPersistentDataContainer().set(new NamespacedKey(FTSUtils.getInstance(), key), DataType.STRING_ARRAY, list);
    }
}

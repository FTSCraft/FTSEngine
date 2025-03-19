package de.ftscraft.ftsengine.feature.durchsuchen;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.ItemStacks;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.items.ItemReader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DurchsuchenManager {
    private static final HashMap<Player, Player> searchRequests = new HashMap<>(); //Player target, Player requester
    private static final HashMap<Player, Integer> searchTasks = new HashMap<>();
    private static final HashMap<Player, Integer> hideTasks = new HashMap<>();
    private static final HashMap<Player, Inventory> hideInventorys = new HashMap<>();
    private static final HashMap<Player, ItemStack[]> originalInventorys = new HashMap<>();
    private static final List<Inventory> searchInventorys = new ArrayList<>();
    private static final int SEARCH_DISTANCE = 5;
    private static final Engine plugin = Engine.getInstance();

    private static final int HIDE_CHANCE_PLAIN = 50;
    private static final int HIDE_CHANCE_BUNDLE = 90;

    public static void request(Player target, Player requester) {
        if (targetBlocked(target)) {
            requester.sendMessage(Messages.PREFIX + "§6" + target.getName() + " §cwird bereits durchsucht!");
            return;
        }

        if (requesterBlocked(requester)) {
            requester.sendMessage(Messages.PREFIX + "§cDu durchsuchst bereits einen anderen Spieler!");
            return;
        }

        if (!inSearchDistance(target, requester)) {
            requester.sendMessage(Messages.PREFIX + "§cDas Ziel ist zu weit entfernt um durchsucht zu werden!");
            return;
        }

        searchRequests.put(target, requester);

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new DurchsuchenRunner(target, DurchsuchenRunner.RunnerType.SEARCH_RUNNER), 20L, 20L);
        searchTasks.put(target, taskId);

        requester.sendMessage(Messages.PREFIX + "Anfrage zum Durchsuchen von §6" + target.getName() + " §7gesendet!");

        TextComponent message1 = new TextComponent(Messages.PREFIX + "§6" + requester.getName() + " §7möchte dich durchsuchen! Klicke hier zum ");
        TextComponent reaction1 = new TextComponent("§a[Annehmen]");
        reaction1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/searchreact accept"));
        reaction1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAnnehmen").create()));
        TextComponent message2 = new TextComponent(" §7oder ");
        TextComponent reaction2 = new TextComponent("§c[Ablehnen]§7.");
        reaction2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/searchreact deny"));
        reaction2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cAblehnen").create()));
        TextComponent message3 = new TextComponent(" Du kannst aber auch versuchen etwas zu ");
        TextComponent reaction3 = new TextComponent("§6[Verstecken]§7.");
        reaction3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/searchreact hide"));
        reaction3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Verstecken - Jedes Item hat eine 50% Chance entdeckt zu werden.").create()));
        TextComponent text = message1;
        text.addExtra(reaction1);
        text.addExtra(message2);
        text.addExtra(reaction2);
        text.addExtra(message3);
        text.addExtra(reaction3);
        target.sendMessage(text);
    }

    public static void accept(Player target) {
        accept(target, null, null, 0);
    }

    public static void accept(Player target, ItemStack[] originalContents, ItemStack[] hiddenInventoryContents, int hiddenItemCount) {
        Player requester = searchRequests.get(target);
        if (!inSearchDistance(target, requester)) {
            requester.sendMessage(Messages.PREFIX + "§cDas Ziel ist zu weit entfernt um durchsucht zu werden!");
            target.sendMessage(Messages.PREFIX + "§cDu bist zu weit entfernt um durchsucht zu werden!");
            return;
        }

        target.sendMessage(Messages.PREFIX + "Du hast die Durchsuchung angenommen. §6" + requester.getName() + " §7sieht jetzt dein Inventar");

        Inventory targetInventoryCopy = Bukkit.createInventory(null, 45,
                "Inventar von " + target.getName());

        boolean hideFailed = false;
        boolean hideTried = false;

        if (hiddenItemCount > 0) {
            hideTried = true;
            for (int i = 0; i < hiddenItemCount; i++) {
                int randValue = ThreadLocalRandom.current().nextInt(0, 100);
                if (randValue > HIDE_CHANCE_PLAIN) {
                    hideFailed = true;
                }
            }
            if (hideFailed) {
                targetInventoryCopy.setContents(originalContents);
            } else {
                targetInventoryCopy.setContents(hiddenInventoryContents);
            }
        } else {
            targetInventoryCopy.setContents(target.getInventory().getContents());
        }

        List<Integer> bundles = new ArrayList<>(targetInventoryCopy.all(Material.BUNDLE).keySet());
        for (int slot : bundles) {
            ItemStack stack = targetInventoryCopy.getItem(slot);
            if (ItemReader.getSign(stack).equals("HIDDEN_BUNDLE")) {
                hideTried = true;
                int randValue = ThreadLocalRandom.current().nextInt(0, 100);
                if (randValue < HIDE_CHANCE_BUNDLE) {
                    targetInventoryCopy.clear(slot);
                } else {
                    hideFailed = true;
                }
            }
        }

        ItemStack checkPane = hideFailed ? getCaughtFrame() : getClearFrame();

        if (hideFailed)
            target.sendMessage(Messages.PREFIX + "§6Anscheinend wurdest du dabei erwischt etwas zu verstecken.");
        else if (hideTried)
            target.sendMessage(Messages.PREFIX + "§7Du wurdest beim Verstecken nicht bemerkt.");

        targetInventoryCopy.setItem(44, checkPane);
        searchRequests.remove(target);
        Bukkit.getScheduler().cancelTask(searchTasks.get(target));
        searchInventorys.add(targetInventoryCopy);

        requester.openInventory(targetInventoryCopy);
    }

    public static void deny(Player target) {
        Player requester = searchRequests.get(target);
        requester.sendMessage(Messages.PREFIX + "§6" + target.getName() + " §chat die Durchsuchung abgelehnt!");
        target.sendMessage(Messages.PREFIX + "§cDu hast die Durchsuchung abgelehnt!");
        searchRequests.remove(target);
        Bukkit.getScheduler().cancelTask(searchTasks.get(target));
    }

    public static void hide(Player target) {
        Player requester = searchRequests.get(target);
        if (!inSearchDistance(target, requester)) {
            requester.sendMessage(Messages.PREFIX + "§cDas Ziel ist zu weit entfernt um durchsucht zu werden!");
            target.sendMessage(Messages.PREFIX + "§cDu bist zu weit entfernt um durchsucht zu werden!");
            return;
        }

        Bukkit.getScheduler().cancelTask(searchTasks.get(target));

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new DurchsuchenRunner(target, DurchsuchenRunner.RunnerType.HIDE_RUNNER), 20L, 20L);
        hideTasks.put(target, taskId);

        Inventory targetHideInventory = Bukkit.createInventory(null, 45, "Versteck-Inventar");
        hideInventorys.put(target, targetHideInventory);

        originalInventorys.put(target, copyInventory(target.getInventory()));

        target.openInventory(targetHideInventory);
    }

    public static void closeHideInventory(Player target) {
        Inventory hideInventory = hideInventorys.get(target);
        if (hideInventory == null) return;
        hideInventory.close();
    }

    public static void handleHideInventory(Player target, Inventory hideInventory) {
        if (!hideInventorys.containsValue(hideInventory)) return;

        Bukkit.getScheduler().cancelTask(hideTasks.get(target));

        ItemStack[] hiddenInventory = copyInventory(target.getInventory());

        target.getInventory().setContents(originalInventorys.get(target));
        target.updateInventory();

        int itemCount = 0;
        for (
                ItemStack itemStack : hideInventory.getContents()) {
            if (itemStack != null)
                itemCount++;
        }

        accept(target, originalInventorys.get(target), hiddenInventory, itemCount);

        originalInventorys.remove(target);
        hideInventorys.remove(target);
    }

    public static boolean isSearched(Player target) {
        return searchRequests.containsKey(target);
    }

    public static boolean isSearchInventory(Inventory inventory) {
        return searchInventorys.contains(inventory);
    }

    public static boolean isHideInventory(Inventory inventory) {
        return hideInventorys.containsValue(inventory);
    }

    public static void removeSearchInventory(Inventory inventory) {
        searchInventorys.remove(inventory);
    }

    private static boolean requesterBlocked(Player requester) {
        return searchRequests.containsValue(requester);
    }

    private static boolean targetBlocked(Player target) {
        return searchRequests.containsKey(target);
    }

    private static boolean inSearchDistance(Player target, Player requester) {
        int range = SEARCH_DISTANCE;
        return requester.getLocation().getWorld().getNearbyEntities(requester.getLocation(), range, range, range).contains(target);
    }

    private static ItemStack[] copyInventory(Inventory inventory) {
        ItemStack[] originalContents = inventory.getContents();
        ItemStack[] copiedContents = new ItemStack[originalContents.length];
        for (int i = 0; i < originalContents.length; i++) {
            copiedContents[i] = (originalContents[i] != null) ? originalContents[i].clone() : null;
        }
        return copiedContents;
    }

    private static ItemStack getClearFrame() {
        ItemStack clearFrame = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta meta = clearFrame.getItemMeta();
        String name = "§aDer Spieler scheint nichts zu verstecken!";
        meta.displayName(Component.text(name));
        clearFrame.setItemMeta(meta);
        return clearFrame;
    }

    private static ItemStack getCaughtFrame() {
        ItemStack clearFrame = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = clearFrame.getItemMeta();
        String name = "§cDer Spieler scheint etwas zu verstecken!";
        meta.displayName(Component.text(name));
        String loreText = "§cDu siehst dennoch das gesamte Inventar.";
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(loreText));
        meta.lore(lore);
        clearFrame.setItemMeta(meta);
        return clearFrame;
    }

}


package de.ftscraft.ftsengine.feature.durchsuchen;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DurchsuchenManager {
    private static HashMap <Player, Player> searchRequests = new HashMap<>(); //Player target, Player requester
    private static HashMap <Player, Integer> searchTasks = new HashMap<>();
    private static List<Inventory> searchInventorys = new ArrayList<>();
    private static final int SEARCH_DISTANCE = 5;
    private static Engine plugin = Engine.getInstance();

    public static void request(Player target, Player requester) {
        if (targetBlocked(target)) {
            requester.sendMessage(Messages.PREFIX + "§6" + target.getName() + " §cwird bereits durchsucht!");
            return;
        }

        if(requesterBlocked(requester)) {
            requester.sendMessage(Messages.PREFIX + "§cDu durchsuchst bereits einen anderen Spieler!");
            return;
        }

        if(!inSearchDistance(target, requester)) {
            requester.sendMessage(Messages.PREFIX + "§cDas Ziel ist zu weit entfernt um durchsucht zu werden!");
            return;
        }

        searchRequests.put(target, requester);

        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new DurchsuchenRunner(target), 20L, 20L);
        searchTasks.put(target, taskId);

        requester.sendMessage(Messages.PREFIX + "Anfrage zum Durchsuchen von §6" + target.getName() + " §7gesendet!");

        TextComponent message1 = new TextComponent(Messages.PREFIX + "§6" + requester.getName() + " §7möchte dich durchsuchen! Klicke hier zum ");
        TextComponent reaction1 = new TextComponent("§a[Annehmen]");
        reaction1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/searchreact accept"));
        reaction1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAnnehmen").create()));
        TextComponent message2 = new TextComponent(" §7oder ");
        TextComponent reaction2 = new TextComponent("§c[Ablehnen]");
        reaction2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/searchreact deny"));
        reaction2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cAblehnen").create()));
        TextComponent text = message1;
        text.addExtra(reaction1);
        text.addExtra(message2);
        text.addExtra(reaction2);
        target.sendMessage(text);
    }

    public static void accept(Player target) {
        Player requester = searchRequests.get(target);
        if(!inSearchDistance(target, requester)) {
            requester.sendMessage(Messages.PREFIX + "§cDas Ziel ist zu weit entfernt um durchsucht zu werden!");
            target.sendMessage(Messages.PREFIX + "§cDu bist zu weit entfernt um durchsucht zu werden!");
            return;
        }

        target.sendMessage(Messages.PREFIX + "Du hast die Durchsuchung angenommen. §6" + requester.getName() + " §7sieht jetzt dein Inventar");

        Inventory targetInventoryCopy = Bukkit.createInventory(null, 45,
                "Inventar von " + target.getName());
        targetInventoryCopy.setContents(target.getInventory().getContents());

        searchRequests.remove(target);
        Bukkit.getScheduler().cancelTask(searchTasks.get(target));
        searchInventorys.add(targetInventoryCopy);

        requester.openInventory(targetInventoryCopy);
    }

    public static void deny(Player target) {
        Player requester = searchRequests.get(target);
        requester.sendMessage(Messages.PREFIX + "§6" + target.getName() +" §chat die Durchsuchung abgelehnt!");
        target.sendMessage(Messages.PREFIX + "§cDu hast die Durchsuchung abgelehnt!");
        searchRequests.remove(target);
        Bukkit.getScheduler().cancelTask(searchTasks.get(target));
    }

    public static void hide(Player target) {
        //TODO
        Player requester = searchRequests.get(target);
    }

    public static boolean isSearched(Player target) {
        return searchRequests.containsKey(target);
    }

    public static boolean isSearchInventory(Inventory inventory) {
        return searchInventorys.contains(inventory);
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
}


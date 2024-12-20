package de.ftscraft.ftsengine.commands;

import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Ausweis;
import de.ftscraft.ftsengine.utils.Messages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMDbuch implements CommandExecutor {

    private final Engine plugin;
    private final String IDENTIFIER = "§B§U§C§H§F§T§S";

    public CMDbuch(Engine plugin) {
        this.plugin = plugin;
        plugin.getCommand("buch").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(cs instanceof Player p)) {
            cs.sendMessage(Messages.ONLY_PLAYER);
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        PlayerInventory inv = p.getInventory();

        if (!inv.contains(Material.PAPER) || !inv.contains(Material.INK_SAC)) {
            p.sendMessage(Messages.PREFIX + "Du brauchst Papier und Tinte im Inventar.");
            return true;
        }

        if (isBookByPlugin(p.getInventory().getItemInMainHand())) {
            ItemStack book = p.getInventory().getItemInMainHand();
            BookMeta bookMeta = (BookMeta) book.getItemMeta();
            String title = bookMeta.getDisplayName();
            if (title.length() > 12)
                title = title.substring(12);
            else title = "";
            if(!title.equals(p.getName())) {
                p.sendMessage(Messages.PREFIX + "Das ist nicht dein Brief!");
                return true;
            }
            if(bookMeta.getPageCount() == 50) {
                p.sendMessage(Messages.PREFIX + "Das Buch hat bereits 50 Seiten. Mehr geht net!");
                return true;
            }
            bookMeta.addPages(Component.text(message.toString()));
            book.setItemMeta(bookMeta);
            p.sendMessage(Messages.PREFIX + "Die Seite wurde hinzugefügt");
            removeItemsFromInventory(inv);
            return true;
        }

        ItemStack bookItemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) bookItemStack.getItemMeta();
        bookMeta.displayName(Component.text("Brief von " + p.getName()).color(NamedTextColor.YELLOW));
        Ausweis ausweis = plugin.getAusweis(p);
        bookMeta.lore(List.of(Component.text(IDENTIFIER)));
        bookMeta.addPages(Component.text(message.toString()));
        bookMeta.setAuthor(ausweis.getFirstName() + " " + ausweis.getLastName());
        bookItemStack.setItemMeta(bookMeta);
        inv.addItem(bookItemStack);
        p.sendMessage(Messages.PREFIX + "Das Buch sollte nun in deinem Inventar sein. Um weitere Seiten dort zu schreiben halte es in deiner (Haupt-)Hand");
        removeItemsFromInventory(inv);
        return false;
    }

    private boolean isBookByPlugin(ItemStack is) {

        if (is.getType() != Material.WRITTEN_BOOK)
            return false;

        BookMeta meta = (BookMeta) is.getItemMeta();
        List<String> lore;
        if ((lore = meta.getLore()) != null) {
            if (!lore.isEmpty()) {
                return lore.getFirst().equals(IDENTIFIER);
            }
        }

        return false;
    }

    private void removeItemsFromInventory(PlayerInventory inv) {

        boolean removedPaper = false;
        boolean removedInk = false;
        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack item = inv.getContents()[i];
            if (item != null) {
                if (!removedPaper && item.getType() == Material.PAPER) {
                    item.setAmount(item.getAmount() - 1);

                    removedPaper = true;

                    if(removedInk)
                        break;

                } else if (!removedInk && item.getType() == Material.INK_SAC) {
                    removedInk = true;

                    if (Math.random() <= 0.05) {
                        item.setAmount(item.getAmount() - 1);
                    }

                    if(removedPaper)
                        break;
                }
            }

        }

    }

}

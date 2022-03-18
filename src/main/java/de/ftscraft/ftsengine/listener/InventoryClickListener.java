package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import de.ftscraft.ftsengine.utils.Messages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryClickListener implements Listener {

    private final Engine plugin;

    public InventoryClickListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /*@EventHandler(priority = EventPriority.NORMAL)
    public void onCraft(PrepareItemCraftEvent event) {

        if(event.getRecipe() == null)
            return;

        if(event.getRecipe().getResult().getType() == Material.CHEST) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
            while (Bukkit.recipeIterator().hasNext()) {
                if(Bukkit.recipeIterator().next())
            }
        }

    }*/

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        //SCHWAZES BRETT

        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();


            if (event.getView().getTitle().startsWith("§4Schwarzes-Brett")) {
                event.setCancelled(true);
                ItemStack clickeditem = event.getCurrentItem();
                String name = event.getView().getTitle().replace("§4Schwarzes-Brett ", "");
                //TODO: Schwarzes Brett im Invnetar Name zueweisen, BrettNote anpassen

                if (clickeditem == null)
                    return;

                if (Objects.requireNonNull(clickeditem.getItemMeta()).getDisplayName().equalsIgnoreCase("§cErstelle Notiz")) {

                    Brett brett = null;
                    for (Brett bretter : plugin.bretter.values()) {
                        if (bretter.getName().equalsIgnoreCase(name)) {
                            brett = bretter;
                            break;
                        }
                    }

                    int price;
                    price = 0;
                    if (!plugin.getEcon().has(p, price)) {
                        p.sendMessage("§cDu hast nicht genug Geld!");
                        return;
                    }

                    //Check if Brett is full
                    if (brett.getGui().isFull()) {
                        p.sendMessage("§7[§bSchwarzes Brett§7] Es gibt keine freien Plätze mehr!");
                        p.sendMessage("§7[§bSchwarzes Brett§7] Warte bis ein Platz frei ist");
                        return;
                    }

                    //Check if player has more than 4 Notes
                    int i = 0;
                    for (BrettNote note : brett.getNotes()) {
                        if (note.getCreator().equals(p.getName())) {
                            i++;
                        }
                    }
                    if (i > 4) {
                        p.sendMessage("§7[§bSchwarzes Brett§7] Du hast bereits (mehr als) 5 Notizen geschriben. Es reicht!");
                        if (p.hasPermission("brett.admin")) {
                            p.sendMessage("§7[§bSchwarzes Brett§7] Aber du hast die Rechte also darfst du das");
                        } else
                            return;
                    }
                    //

                    p.closeInventory();
                    p.sendMessage("§cBitte achte auf einen RPlichen Schreibstil \n §7[§bSchwarzes Brett§7] §bBitte gebe jetzt den Titel ein. §c(Max. 50 Ziechen)");
                    ArrayList<String> al = new ArrayList<String>();
                    al.add(" ");
                    BrettNote brettNote = new BrettNote(brett, p.getName(), true);
                    plugin.playerBrettNote.put(p, brettNote);
                } else {
                    String item_name = clickeditem.getItemMeta().getDisplayName();

                    if (item_name.startsWith("§cSeite")) {
                        String pageS = item_name.substring(8);
                        if (!clickeditem.getType().equals(Material.FLOWER_BANNER_PATTERN))
                            return;
                        int page;
                        try {
                            page = Integer.valueOf(pageS);
                        } catch (NumberFormatException e) {
                            return;
                        }

                        FTSUser user = plugin.getPlayer().get(p);

                        user.getBrett().getGui().open(p, page);

                    }

                    if (!(item_name.equalsIgnoreCase("&7Pinnwand") || item_name.equalsIgnoreCase("&8Leere Notiz"))) {
                        Brett brett = null;
                        int inv_slot = event.getSlot();
                        int page = 0;

                        if (event.getInventory().getItem(36).getType() == Material.WHITE_STAINED_GLASS_PANE)
                            page = 1;
                        if (event.getInventory().getItem(37).getType() == Material.WHITE_STAINED_GLASS_PANE)
                            page = 2;
                        if (event.getInventory().getItem(38).getType() == Material.WHITE_STAINED_GLASS_PANE)
                            page = 3;
                        if (event.getInventory().getItem(39).getType() == Material.WHITE_STAINED_GLASS_PANE)
                            page = 4;
                        if (event.getInventory().getItem(40).getType() == Material.WHITE_STAINED_GLASS_PANE)
                            page = 5;

                        for (Brett bretter : plugin.bretter.values())
                            if (bretter.getName().equalsIgnoreCase(name)) {
                                brett = bretter;
                                break;
                            }
                        BrettNote note = null;
                        for (BrettNote notes : brett.getNotes())
                            if (notes.invslot == inv_slot && notes.page == page) {
                                note = notes;
                                break;
                            }
                        if (note == null) {
                            return;
                        }
                        String note_title = note.getTitle();
                        String note_cont = note.getContent();
                        String note_creator = note.getCreator();


                        p.sendMessage("§7**********************************");
                        p.sendMessage("§6" + note_title);
                        p.sendMessage(note_cont);
                        p.sendMessage(" ");
                        p.sendMessage("§7§nNotiz von " + note_creator);
                        if (p.hasPermission("brett.admin") || note_creator.equals(p.getName())) {
                            ComponentBuilder componentBuilder = new ComponentBuilder("§4Löschen");
                            componentBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftsengine brett delete " + inv_slot + " " + page + " " + brett.getName().replace(" ", "_")));
                            p.sendMessage(componentBuilder.create());
                        }
                        p.sendMessage("§7**********************************");

                    }
                }
            }
        }

        //Anti Backpack
        if (event.getCurrentItem() != null) {

            //Check if inv is enderchest or shulkerbox
            if (event.getInventory().getType() == InventoryType.ENDER_CHEST || event.getInventory().getType() == InventoryType.SHULKER_BOX) {
                //if player uses number keys, cancel
                if(event.getClick() == ClickType.NUMBER_KEY) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Leider kannst du hier nicht deine Nummern benutzen.");
                    return;
                }
                //check by raw slot if player is navigating in his inv or in chests
                if (event.getRawSlot() >= 27) {

                    //check if he clicked on backpack
                    if (event.getCurrentItem().getItemMeta() != null) {

                        if (BackpackType.getBackpackByName(event.getCurrentItem().getItemMeta().getDisplayName()) != null) {

                            //cancel
                            event.getWhoClicked().sendMessage(Messages.PREFIX + "Leider kannst du keine Rucksäcke in Enderchests oder Shulkerchests packen.");
                            event.setCancelled(true);
                            return;

                        }

                    }

                }
            }

            if(BackpackType.getBackpackByName(event.getWhoClicked().getOpenInventory().getTitle()) != null) {
                if(event.getClick() == ClickType.NUMBER_KEY) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Leider kannst du hier nicht deine Nummern benutzen.");
                    return;
                }
                if (BackpackType.getBackpackByName(event.getCurrentItem().getItemMeta().getDisplayName()) != null) {
                    if (!event.getWhoClicked().hasPermission("ftsengine.backpack.move")) {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage("§3Du kannst kein Rucksack in ein Rucksack packen!");
                    }
                }
            }


        }
    }

}

package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.main.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class InventoryClickListener implements Listener {

    private Engine plugin;

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
        //FULL


        //\FUll


        //SCHWAZES BRETT

        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player) event.getWhoClicked();


            if (event.getView().getTitle().startsWith("§4Schwarzes-Brett")) {
                event.setCancelled(true);
                ItemStack clickeditem = event.getCurrentItem();
                String name = event.getView().getTitle().replace("§4Schwarzes-Brett ", "");
                //TODO: Schwarzes Brett im Invnetar Name zueweisen, BrettNote anpassen

                if (Objects.requireNonNull(clickeditem.getItemMeta()).getDisplayName().equalsIgnoreCase("§cErstelle Notitz")) {
                    if (!plugin.getEcon().has(p, 1)) {
                        p.sendMessage("§cDu hast nicht genug Geld!");
                        return;
                    }
                    Brett brett = null;
                    for (Brett bretter : plugin.bretter.values()) {
                        if (bretter.getName().equalsIgnoreCase(name)) {
                            brett = bretter;
                            break;
                        }
                    }

                    if (brett.getGui().isFull()) {
                        p.sendMessage("§7[§bSchwarzes Brett§7] Es gibt keine freien Plätze mehr!");
                        p.sendMessage("§7[§bSchwarzes Brett§7] Warte bis ein Platz frei ist");
                        return;
                    }

                    p.closeInventory();
                    p.sendMessage("§7[§bSchwarzes Brett§7] §bBitte gebe jetzt den Titel ein. §c(Max. 50 Ziechen)");
                    ArrayList<String> al = new ArrayList<>();
                    al.add(" ");
                    BrettNote brettNote = new BrettNote(brett, p.getName(), true);
                    plugin.playerBrettNote.put(p, brettNote);
                } else {
                    String item_name = clickeditem.getItemMeta().getDisplayName();
                    if (!(item_name.equalsIgnoreCase("&7Pinnwand") || item_name.equalsIgnoreCase("&8Leere Notitz"))) {
                        Brett brett = null;
                        int inv_slot = event.getSlot();
                        for (Brett bretter : plugin.bretter.values())
                            if (bretter.getName().equalsIgnoreCase(name)) {
                                brett = bretter;
                                break;
                            }
                        BrettNote note = null;
                        for (BrettNote notes : brett.getNotes())
                            if (notes.invslot == inv_slot) {
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
                        p.sendMessage("§7§nNotitz von " + note_creator);
                        p.sendMessage("§7**********************************");

                    }
                }
            }
        }

        //Anti Backpack
        if (event.getCurrentItem() != null) {
            if (BackpackType.getBackpackByName(event.getView().getTitle()) == null) {
                return;
            }
            if (BackpackType.getBackpackByName(event.getWhoClicked().getOpenInventory().getTitle()) != null) {
                if (BackpackType.getBackpackByName(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName()) != null) {
                    if (!event.getWhoClicked().hasPermission("ftsengine.backpack.move")) {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage("§3Du kannst kein Rucksack in ein Rucksack packen!");
                    }
                }
            }

        }
    }

}

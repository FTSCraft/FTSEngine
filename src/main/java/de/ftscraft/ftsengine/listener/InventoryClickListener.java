package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.backpacks.BackpackType;
import de.ftscraft.ftsengine.brett.Brett;
import de.ftscraft.ftsengine.brett.BrettNote;
import de.ftscraft.ftsengine.feature.durchsuchen.DurchsuchenManager;
import de.ftscraft.ftsengine.feature.instruments.CustomInstrument;
import de.ftscraft.ftsengine.feature.instruments.Instrument;
import de.ftscraft.ftsengine.feature.instruments.SimpleInstrument;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.main.FTSUser;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.items.ItemReader;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class InventoryClickListener implements Listener {

    private final Engine plugin;

    public InventoryClickListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        handleInstrument(event);

        //SCHWAZES BRETT

        if (handleSchwarzesBrett(event)) return;

        //Anti Backpack
        if (event.getCurrentItem() != null) {

            //Check if inv is enderchest or shulkerbox
            if (event.getInventory().getType() == InventoryType.ENDER_CHEST || event.getInventory().getType() == InventoryType.SHULKER_BOX) {
                //if player uses number keys, cancel
                if (event.getClick() == ClickType.NUMBER_KEY) {
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

            if (BackpackType.getBackpackByName(event.getWhoClicked().getOpenInventory().getTitle()) != null) {
                if (event.getClick() == ClickType.NUMBER_KEY) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Leider kannst du hier nicht deine Nummern benutzen.");
                    return;
                }
                if (BackpackType.getBackpackByName(event.getCurrentItem().getItemMeta().getDisplayName()) != null) {
                    if (!event.getWhoClicked().hasPermission("ftsengine.backpack.move")) {
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Messages.PREFIX + "Du kannst kein Rucksack in ein Rucksack packen!");
                    }
                }
            }

            if (event.getInventory().getType() == InventoryType.ANVIL) {
                ItemStack itemToRepair = event.getInventory().getItem(0);
                ItemStack repairMaterial = event.getInventory().getItem(1);

                if (itemToRepair != null && repairMaterial != null) {
                    if ("EMERALDPICKAXE".equals(ItemReader.getSign(itemToRepair))) {
                        if (!"EMERALDPICKAXE".equals(ItemReader.getSign(repairMaterial))) {
                            event.setCancelled(true);
                            event.getWhoClicked().sendMessage(Messages.PREFIX + "Du kannst die Smaragdspitzhacke nur mit einer anderen Smaragdspitzhacke reparieren!");
                        }
                    }
                }
            }

            ItemStack stack = event.getCurrentItem();
            if (ItemReader.getSign(stack).equals("HIDDEN_BUNDLE")) {
                if(DurchsuchenManager.isHideInventory(event.getInventory())) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Du brauchst das versteckte Bündel nicht verstecken, es ist schon versteckt!");
                }
            }
        }

        Inventory inventory = event.getInventory();
        if (DurchsuchenManager.isSearchInventory(inventory)) {
            event.setCancelled(true);
        }
    }

    private boolean handleSchwarzesBrett(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player p)) {
            return false;
        }

        if (event.getView().getTitle().startsWith("§4Schwarzes-Brett")) {
            event.setCancelled(true);
            ItemStack clickeditem = event.getCurrentItem();
            String name = event.getView().getTitle().replace("§4Schwarzes-Brett ", "");

            if (clickeditem == null)
                return true;

            if (handleCreateNode(p, clickeditem, name)) return true;

            String item_name = clickeditem.getItemMeta().getDisplayName();

            if (item_name.startsWith("§cSeite")) {
                String pageS = item_name.substring(8);
                if (!clickeditem.getType().equals(Material.FLOWER_BANNER_PATTERN))
                    return true;
                int page;
                try {
                    page = Integer.parseInt(pageS);
                } catch (NumberFormatException e) {
                    return true;
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
                    return true;
                }

                sendNoteMessage(p, note, inv_slot, page, brett);

            }
        }
        return false;
    }

    private boolean handleCreateNode(Player p, ItemStack clickeditem, String name) {
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
                p.sendMessage(Messages.PREFIX + "Du hast nicht genug Geld!");
                return true;
            }

            if (brett.getGui().isFull()) {
                p.sendMessage("§7[§bSchwarzes Brett§7] Es gibt keine freien Plätze mehr!");
                p.sendMessage("§7[§bSchwarzes Brett§7] Warte bis ein Platz frei ist");
                return true;
            }

            if (isNotAbleToWriteMoreNodes(p, brett)) return true;

            p.closeInventory();
            p.sendMessage(Messages.PREFIX + "Bitte achte auf einen RPlichen Schreibstil \n §7[§bSchwarzes Brett§7] §bBitte gebe jetzt den Titel ein. §c(Max. 50 Zeichen)");
            p.sendMessage(Messages.PREFIX + "Um die Erstellung abzubrechen gebe 'exit' ein!");

            BrettNote brettNote = new BrettNote(brett, p.getName(), true);
            plugin.playerBrettNote.put(p, brettNote);
            return true;
        }
        return false;
    }

    private static boolean isNotAbleToWriteMoreNodes(Player p, Brett brett) {
        //Check if player has more than 4 Notes
        int i = 0;
        for (BrettNote note : brett.getNotes()) {
            if (note.getCreator().equals(p.getName())) {
                i++;
            }
        }
        if (i >= 4) {
            p.sendMessage("§7[§bSchwarzes Brett§7] Du hast bereits (mehr als) 4 Notizen geschriben. Es reicht!");
            if (p.hasPermission("brett.admin")) {
                p.sendMessage("§7[§bSchwarzes Brett§7] Aber du hast die Rechte also darfst du das");
            } else
                return true;
        }
        return false;
    }

    private static void sendNoteMessage(Player p, BrettNote note, int inv_slot, int page, Brett brett) {
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

    private void handleInstrument(InventoryClickEvent event) {

        if (!(event.getInventory().getHolder() instanceof Instrument instrument))
            return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null)
            return;

        Integer a = ItemReader.getPDC(event.getCurrentItem(), "noteindex", PersistentDataType.INTEGER);
        if (a == null)
            return;

        if (instrument instanceof SimpleInstrument simpleInstrument) {
            event.getWhoClicked().getWorld().playSound(event.getWhoClicked(), simpleInstrument.sound, 10, SimpleInstrument.notes[a]);
            return;
        }

        if (instrument instanceof CustomInstrument customInstrument) {

        }

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if (DurchsuchenManager.isSearchInventory(inventory)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        DurchsuchenManager.removeSearchInventory(inventory);
        DurchsuchenManager.handleHideInventory((Player) event.getPlayer(), event.getInventory());
    }

}

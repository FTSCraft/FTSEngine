package de.ftscraft.ftsengine.listener;

import de.ftscraft.ftsengine.feature.items.backpacks.BackpackType;
import de.ftscraft.ftsengine.feature.items.instruments.CustomInstrument;
import de.ftscraft.ftsengine.feature.items.instruments.Instrument;
import de.ftscraft.ftsengine.feature.items.instruments.SimpleInstrument;
import de.ftscraft.ftsengine.feature.roleplay.durchsuchen.DurchsuchenManager;
import de.ftscraft.ftsengine.main.Engine;
import de.ftscraft.ftsengine.utils.Messages;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClickListener implements Listener {

    private final Engine plugin;

    public InventoryClickListener(Engine plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {

        handleInstrument(event);


        //Anti Backpack
        if (event.getCurrentItem() != null) {

            if (event.getInventory().getType() == InventoryType.ENDER_CHEST || event.getInventory().getType() == InventoryType.SHULKER_BOX) {
                if (event.getClick() == ClickType.NUMBER_KEY) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Leider kannst du hier nicht deine Nummern benutzen.");
                    return;
                }
                if (event.getRawSlot() >= 27) {

                    if (event.getCurrentItem().getItemMeta() != null) {

                        if (BackpackType.getBackpackByName(event.getCurrentItem().getItemMeta().getDisplayName()) != null) {

                            event.getWhoClicked().sendMessage(Messages.PREFIX + "Leider kannst du keine Rucksäcke in Enderchests oder Shulkerchests packen.");
                            event.setCancelled(true);
                            return;
                        }

                        if (isBundle(event.getCurrentItem())) {
                            event.getWhoClicked().sendMessage(Messages.PREFIX + "Du kannst kein Bündel in deiner Enderchest oder Shulkerchests verstauen!");
                            event.setCancelled(true);
                            return;
                        }

                    }

                }
            }

            InventoryView view = event.getView();
            String title = view.getTitle();
            if (BackpackType.getBackpackByName(title) != null) {

                Inventory topInv = view.getTopInventory();
                int rawSlot = event.getRawSlot();

                if (event.getClick() == ClickType.NUMBER_KEY && rawSlot < topInv.getSize()) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Leider kannst du hier nicht deine Nummern benutzen.");
                    return;
                }

                if (rawSlot >= topInv.getSize()) {
                    ItemStack current = event.getCurrentItem();
                    if (isProhibitedItem(current)) {
                        String msg = isBundle(current)
                                ? "Du kannst hier kein Bündel hineinpacken!"
                                : "Du kannst keinen Rucksack in deinem Rucksack verstauen!";
                        event.setCancelled(true);
                        event.getWhoClicked().sendMessage(Messages.PREFIX + msg);
                        return;
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
            if ("HIDDEN_BUNDLE".equals(ItemReader.getSign(stack))) {
                if (DurchsuchenManager.isHideInventory(event.getInventory())) {
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

    private boolean isProhibitedItem(ItemStack item) {
        if (item == null) return false;
        if (item.hasItemMeta()
                && BackpackType.getBackpackByName(item.getItemMeta().getDisplayName()) != null) {
            return true;
        }
        return isBundle(item);
    }

    private boolean isBundle(ItemStack item) {
        Material material = item.getType();
        return (material.name().endsWith("BUNDLE"));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if (DurchsuchenManager.isSearchInventory(inventory)) {
            event.setCancelled(true);
        }

        //Für Rucksäcke
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (BackpackType.getBackpackByName(title) != null) {

            ItemStack cursor = event.getCursor();
            if (!isProhibitedItem(cursor)) return;

            Inventory topInv = view.getTopInventory();
            for (int slot : event.getRawSlots()) {
                if (slot < topInv.getSize()) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(Messages.PREFIX + "Du kannst hier nichts hineinziehen!");
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        DurchsuchenManager.removeSearchInventory(inventory);
        DurchsuchenManager.handleHideInventory((Player) event.getPlayer(), event.getInventory());
    }

}

package ru.jok1r.diverseScavenger.listener;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftInventoryCustom;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.jok1r.diverseScavenger.Main;

public class PlayerInventoryListener implements Listener {

    private final Main plugin;

    public PlayerInventoryListener(Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority= EventPriority.HIGHEST)
    public void invokeClick(InventoryClickEvent e) {
        if (e.getWhoClicked().isOp()) {
            return;
        }

        Inventory inventory = e.getInventory();
        InventoryType type = inventory.getType();
        if (!inventory.getTitle().equalsIgnoreCase("chest") &&
                type != InventoryType.PLAYER &&
                type != InventoryType.CRAFTING &&
                type != InventoryType.ENDER_CHEST &&
                e.getClick() == ClickType.NUMBER_KEY) {
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
            return;
        }

        ItemStack itemCursor = e.getCursor();
        ItemStack currentItem = e.getCursor();
        if ((itemCursor != null && this.plugin.itemsToSave.contains(itemCursor.getTypeId())) ||
                (currentItem != null && this.plugin.itemsToSave.contains(currentItem.getTypeId()))) {
            if (type == InventoryType.PLAYER || type == InventoryType.ENDER_CHEST || type == InventoryType.CRAFTING) {
                return;
            }

            if (inventory instanceof CraftInventoryCustom && inventory.getTitle().equalsIgnoreCase("chest")) {
                return;
            }

            this.plugin.sendMessage((CommandSender) e.getWhoClicked(), this.plugin.tryMoveItem);
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
        }
    }
}

package ru.jok1r.diverseScavenger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public class DiverseDeathListener implements Listener {

    private final Main plugin;

    public DiverseDeathListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(!this.plugin.data.containsKey(player.getName())) {
            if (player.hasPermission("diverseScavenger.admin") || player.isOp()) {
                List<ItemStack> itemsToSave = new ArrayList(event.getDrops());
                this.plugin.data.put(player.getName(), itemsToSave);
                event.getDrops().clear();
            } else {
                if (this.plugin.isDebugging)
                    this.plugin.getLogger().severe("DS: Player " + player.getName() + " is dead :(");
                    this.plugin.getLogger().severe("DS (" + player.getName() + "): Inventory: " + event.getDrops().toString());

                List<ItemStack> itemsToSave = new ArrayList();
                for (ItemStack itemStack : event.getDrops()) {
                    if (this.plugin.itemsToSave.contains(itemStack.getTypeId())) {
                        itemsToSave.add(itemStack);
                        if (this.plugin.isDebugging)
                            this.plugin.getLogger().severe("DS (" + player.getName() + "): " + itemStack.getItemMeta().getDisplayName() + " is saved");

                    }
                }

                for (ItemStack itemStack : itemsToSave) {
                    if (event.getDrops().contains(itemStack)) {
                        event.getDrops().remove(itemStack);
                        if (this.plugin.isDebugging) {
                            this.plugin.getLogger().severe("DS (" + player.getName() + "): " + itemStack.getItemMeta().getDisplayName() + " remove from drop list");
                        }
                    }
                }

                this.plugin.data.put(player.getName(), itemsToSave);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if(this.plugin.data.containsKey(player.getName())) {
            List<ItemStack> itemsToSave = this.plugin.data.get(player.getName());
            if(this.plugin.isDebugging)
                this.plugin.getLogger().severe("DS: Player" + player.getName() + " is alive! Backing items");
                this.plugin.getLogger().severe("DS (" + player.getName() + "): items to save = " + itemsToSave.toString());

            PlayerInventory inventory = player.getInventory();
            if(!itemsToSave.isEmpty()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e>> После смерти вы сохранили некоторые вещи"));
            }

            int i = 0;
            for(ItemStack item: itemsToSave) {
                inventory.setItem(i, item);
                i++;

                if(this.plugin.isDebugging)
                    this.plugin.getLogger().severe("DS (" + player.getName() + "): " + item.getItemMeta().getDisplayName() + " set to inventory in slot = " + i);
            }
            this.plugin.data.remove(player.getName());
        }
    }
}

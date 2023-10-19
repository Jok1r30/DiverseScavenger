package ru.jok1r.diverseScavenger.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import ru.jok1r.diverseScavenger.Main;

public class PlayerDropItemListener implements Listener {

    private final Main plugin;

    public PlayerDropItemListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority= EventPriority.MONITOR)
    public void invokeDrop(PlayerDropItemEvent e) {
        boolean isPlayerDead = true;
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            if (!stackTraceElement.getClassName().equalsIgnoreCase("net.minecraftforge.common.ForgeHooks") || !stackTraceElement.getMethodName().equalsIgnoreCase("onPlayerTossEvent")) continue;
            isPlayerDead = false;
            break;
        }

        if (isPlayerDead) {
            return;
        }

        if (e.getPlayer().isOp() || e.getPlayer().hasPermission("diverseScavenger.admin")) {
            return;
        }

        if(!this.plugin.itemsToSave.contains(e.getItemDrop().getItemStack().getTypeId())) {
            return;
        }

        e.setCancelled(true);
        this.plugin.sendMessage(e.getPlayer(), this.plugin.tryDropItem);

/*        PersonalManager personalManager = PersonalManager.INSTANCE;
        if (personalManager.isPersonal(e.getItemDrop().getItemStack())) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)personalManager.getDropDeny()));
            e.setCancelled(true);
        }*/
    }
}

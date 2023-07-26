package ru.jok1r.diverseScavenger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class DiverseCommandScavengerExecutor implements CommandExecutor {

    private final Main plugin;

    public DiverseCommandScavengerExecutor(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.isOp() && !sender.hasPermission("diverseScavenger.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c>> Недостаточно прав для использования данной команды"));
            return false;
        }

        if(args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2>> DiverseScavenger: &a/dsaver reload/add"));
            return false;
        }

        if(args[0].equals("reload")) {
            this.plugin.register();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e>> Успешно перезагружено!"));
            return false;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c>> Для использования этой команды нужно находиться на сервере!"));
            return false;
        }

        if(args[0].equals("add")) {
            ItemStack stack = ((Player) sender).getItemInHand();
            if(stack == null || stack.getType().equals(Material.AIR)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c>> Для использования этой команды нужно взять предмет в руки!"));
                return false;
            }

            int id = stack.getTypeId();
            this.plugin.itemsToSave.add(id);
            this.plugin.configuration.set("items", this.plugin.itemsToSave);
            try {
                this.plugin.configuration.save(this.plugin.config);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e>> Предмет был успешно добавлен!"));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
        return false;
    }
}

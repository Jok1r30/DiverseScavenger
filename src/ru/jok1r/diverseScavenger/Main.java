package ru.jok1r.diverseScavenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.jok1r.diverseScavenger.listener.PlayerDeathListener;
import ru.jok1r.diverseScavenger.listener.PlayerDropItemListener;
import ru.jok1r.diverseScavenger.listener.PlayerInventoryListener;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Main extends JavaPlugin {

    public Map<String, List<ItemStack>> data;

    public boolean isDebugging;

    public String loreCantDrop;
    public String lorePersonal;

    public String tryDropItem;
    public String tryMoveItem;

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInventoryListener(this), this);

        this.getCommand("dscavenger").setExecutor(new DiverseCommandScavengerExecutor(this));

        final File file = new File(this.getDataFolder(), "recovery.data");
        if(file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                this.data = (HashMap<String, List<ItemStack>>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        this.register();
    }

    public boolean isToSaveItem(ItemStack stack) {
        if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasLore()) {
            for (String lore : stack.getItemMeta().getLore()) {
                if(lore.contains(this.loreCantDrop)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isPersonalItem(ItemStack stack) {
        if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasLore()) {
            for (String lore : stack.getItemMeta().getLore()) {
                if(lore.contains(this.lorePersonal)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void onDisable() {
        final File file = new File(this.getDataFolder(), "recovery.data");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.data);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void register() {
        File config = new File(getDataFolder(), "config.yml");
        if(!config.exists()) {
            saveResource("config.yml", false);
        }

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(config);
        this.isDebugging = configuration.getBoolean("debug");

        this.loreCantDrop = configuration.getString("lore.cantDrop");
        this.lorePersonal = configuration.getString("lore.personal");

        this.tryDropItem = configuration.getString("messages.tryDropItem");
        this.tryMoveItem = configuration.getString("messages.tryMoveItem");
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}

package ru.jok1r.diverseScavenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.jok1r.diverseScavenger.listener.PlayerDeathListener;
import ru.jok1r.diverseScavenger.listener.PlayerDropItemListener;
import ru.jok1r.diverseScavenger.listener.PlayerInventoryListener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {

    public File config;
    public FileConfiguration configuration;
    public List<Integer> itemsToSave;
    public Map<String, List<ItemStack>> data;

    public boolean isDebugging;
    public String tryDropItem;
    public String tryMoveItem;

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInventoryListener(this), this);

        this.getCommand("dsaver").setExecutor(new DiverseCommandScavengerExecutor(this));

        final File file = new File(this.getDataFolder(), "regeneration.data");
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
        config = new File(getDataFolder(), "config.yml");
        if(!config.exists()) {
            saveResource("config.yml", false);
        }

        configuration = YamlConfiguration.loadConfiguration(config);
        this.isDebugging = configuration.getBoolean("debug");

        this.tryDropItem = configuration.getString("messages.tryDropItem");
        this.tryMoveItem = configuration.getString("messages.tryMoveItem");

        this.data = new HashMap<String, List<ItemStack>>();

        this.itemsToSave = new ArrayList<Integer>();
        this.itemsToSave = (List<Integer>) configuration.getList("items");
    }

    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}

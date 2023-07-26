package ru.jok1r.diverseScavenger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new DiverseDeathListener(this), this);
        this.getCommand("dsaver").setExecutor(new DiverseCommandScavengerExecutor(this));
        this.register();
    }

    public void register() {
        config = new File(getDataFolder(), "config.yml");
        if(!config.exists()) {
            saveResource("config.yml", false);
        }

        configuration = YamlConfiguration.loadConfiguration(config);
        this.isDebugging = configuration.getBoolean("debug");
        this.data = new HashMap<String, List<ItemStack>>();

        this.itemsToSave = new ArrayList<Integer>();
        this.itemsToSave = (List<Integer>) configuration.getList("items");
    }
}

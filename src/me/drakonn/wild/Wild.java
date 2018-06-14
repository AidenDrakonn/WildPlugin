package me.drakonn.wild;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.drakonn.wild.command.Command;
import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.datamanager.FileManager;
import me.drakonn.wild.datamanager.ItemManager;
import me.drakonn.wild.datamanager.MessageManager;
import me.drakonn.wild.gui.MainInvHandler;
import me.drakonn.wild.gui.RangeInvHandler;
import me.drakonn.wild.teleportation.TeleportationCooldown;
import me.drakonn.wild.teleportation.TeleportationDelay;
import me.drakonn.wild.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Wild extends JavaPlugin
{
    private static Wild instance;
    public TeleportationDelay teleportationDelay = new TeleportationDelay();
    public TeleportationCooldown teleportationCooldown = new TeleportationCooldown();
    private FileManager fileManager = new FileManager(this);
    private ItemManager itemManager = new ItemManager(this);
    private ConfigManager configManager = new ConfigManager(this);
    private MessageManager messageManager = new MessageManager(this);
    private Command command;
    public HashMap<UUID, Integer> minRanges = new HashMap<>();
    public HashMap<UUID, Integer> maxRanges = new HashMap<>();
    public List<UUID> teleporting = new ArrayList<>();
    public WorldEditPlugin worldEdit;
    private Economy economy;
    public File itemConfigFile;
    public FileConfiguration itemConfig;

    public void onEnable()
    {
        instance = this;
        saveDefaultConfig();
        loadEconomy();
        loadWorldEdit();
        configManager.loadData();
        fileManager.setupFile();
        fileManager.loadData();
        createCustomConfig();
        itemManager.loadItems();
        messageManager.loadMessages();
        registerListeners();
        command = new Command();
        getCommand("wild").setExecutor(command);

    }

    public void onDisable()
    {
        fileManager.saveData();
    }

    private void registerListeners()
    {
        getServer().getPluginManager().registerEvents(new MainInvHandler(), this);
        getServer().getPluginManager().registerEvents(new RangeInvHandler(), this);
        getServer().getPluginManager().registerEvents(teleportationDelay, this);
    }

    private void createCustomConfig() {
        itemConfigFile = new File(getDataFolder(), "guiitems.yml");
        if (!itemConfigFile.exists()) {
            itemConfigFile.getParentFile().mkdirs();
            saveResource("guiitems.yml", false);
        }

        itemConfig = new YamlConfiguration();
        try {
            itemConfig.load(itemConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadEconomy()
    {
        if(getServer().getPluginManager().getPlugin("Vault") != null) {
            economy = Util.setupEconomy(this);
            return;
        }
        economy = null;
    }

    private void loadWorldEdit()
    {
        if(getServer().getPluginManager().getPlugin("WorldEdit") != null) {
            worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
            return;
        }
        worldEdit = null;
    }

    public Economy getEconomy() { return economy; }

    public WorldEditPlugin getWorldEdit() { return worldEdit; }

    public FileConfiguration getItemConfig() { return itemConfig; }

    public static Wild getInstance() { return instance; }
}

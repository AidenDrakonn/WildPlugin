package me.drakonn.wild;

import me.drakonn.wild.command.Command;
import me.drakonn.wild.datamanager.ConfigManager;
import me.drakonn.wild.datamanager.FileManager;
import me.drakonn.wild.datamanager.ItemManager;
import me.drakonn.wild.datamanager.MessageManager;
import me.drakonn.wild.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Wild extends JavaPlugin
{
    private static Economy economy;
    private static Wild instance;
    private FileManager fileManager = new FileManager(this);
    private ItemManager itemManager = new ItemManager(this);
    private ConfigManager configManager = new ConfigManager(this);
    private MessageManager messageManager = new MessageManager(this);
    private HashMap<UUID, Integer> minRanges = new HashMap<>();

    public void onEnable()
    {
        instance = this;
        saveDefaultConfig();
        fileManager.setupFile();
        fileManager.loadData();
        itemManager.loadItems();
        configManager.loadData();
        messageManager.loadMessages();
        loadEconomy();
        registerListeners();
        getCommand("wild").setExecutor(new Command());
    }

    private void registerListeners()
    {

    }

    private void loadEconomy()
    {
        if(getServer().getPluginManager().getPlugin("Vault") != null)
            economy = Util.setupEconomy(this);
    }

    public static Economy getEconomy() {
        return economy;
    }

    public static Wild getInstance() {
        return instance;
    }
}

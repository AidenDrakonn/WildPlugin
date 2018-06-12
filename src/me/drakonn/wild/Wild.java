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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Wild extends JavaPlugin
{
    private static Wild instance;
    private FileManager fileManager = new FileManager(this);
    private ItemManager itemManager = new ItemManager(this);
    private ConfigManager configManager = new ConfigManager(this);
    private MessageManager messageManager = new MessageManager(this);
    public TeleportationDelay teleportationDelay = new TeleportationDelay();
    public TeleportationCooldown teleportationCooldown = new TeleportationCooldown();
    public HashMap<UUID, Integer> minRanges = new HashMap<>();
    public HashMap<UUID, Integer> maxRanges = new HashMap<>();
    public WorldEditPlugin worldEdit;
    private Economy economy;

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
        loadWorldEdit();
        registerListeners();
        getCommand("wild").setExecutor(new Command());
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

    public Economy getEconomy() {
        return economy;
    }

    public WorldEditPlugin getWorldEdit()
    {
        return worldEdit;
    }

    public static Wild getInstance() {
        return instance;
    }
}
